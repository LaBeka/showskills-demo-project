package edu.example.demoproject.services;

import edu.example.demoproject.contextHelpers.ImageMappingContext;
import edu.example.demoproject.dtos.image.ImageDto;
import edu.example.demoproject.dtos.image.ImageType;
import edu.example.demoproject.entities.image.ClientImage;
import edu.example.demoproject.entities.image.ImageEntity;
import edu.example.demoproject.entities.image.ProductImage;
import edu.example.demoproject.entities.image.UserImage;
import edu.example.demoproject.exception.ImageUploadException;
import edu.example.demoproject.mappers.ImageMapper;
import edu.example.demoproject.repos.ImageRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import io.minio.*;
import io.minio.errors.*;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final ImageMappingContext ctx;
    private final MinioClient minioClient;

    @SneakyThrows
    @Transactional
    public void updateUploadProductImage(MultipartFile newFile, Long productId)  {
        // find an existing ProductImage
        List<ProductImage> existingImages = imageRepository.findByProductId(productId);
        String bucketName = "product-" + productId + "-bucket";

        if (!existingImages.isEmpty()) {
            // update the first (or decide if you want multiple per product)
            ProductImage existing = existingImages.get(0);

            // delete old object from MinIO
            deleteObjectInBucket(bucketName, existing.getImageName());

            // upload new object
            String newName = uploadInBucket(newFile, bucketName);

            // update entity
            existing.setImageName(newName);
            imageRepository.merge(existing);

        } else {
            // no image yet → create new one
            String fileName = uploadInBucket(newFile, bucketName);
            ImageDto dto = new ImageDto(null, fileName, ImageType.PRODUCT, productId);
            ImageEntity entity = imageMapper.toEntity(dto, ctx);
            imageRepository.persist(entity);
        }
    }

    @SneakyThrows
    @Transactional
    public void updateUploadUserImage(MultipartFile newFile, Long userId)  {
        // find an existing ProductImage
        List<UserImage> existingImages = imageRepository.findByUserId(userId);
        String bucketName = "user-" + userId + "-bucket";

        if (!existingImages.isEmpty()) {
            // update the last added picture of user
            UserImage existing = existingImages.get(existingImages.size() - 1);

            // delete old object from MinIO
            deleteObjectInBucket(bucketName, existing.getImageName());

            // upload new object
            String newName = uploadInBucket(newFile, bucketName);

            // update entity
            existing.setImageName(newName);
            imageRepository.merge(existing);

        } else {
            // no image yet → create new one
            String fileName = uploadInBucket(newFile, bucketName);
            ImageDto dto = new ImageDto(null, fileName, ImageType.USER, userId);
            ImageEntity entity = imageMapper.toEntity(dto, ctx);
            imageRepository.persist(entity);
        }
    }

    @SneakyThrows
    @Transactional
    public ResponseEntity updateUploadClientImage(MultipartFile newFile, Long clientId)  {
        // find an existing
        List<ClientImage> existingImages = imageRepository.findByClientId(clientId);
        String bucketName = "client-" + clientId + "-bucket";

        ImageEntity result = new ClientImage();
        if (!existingImages.isEmpty()) {
            //1 if there is already an image get that image, delete it bucket
            result = existingImages.get(0);
            deleteObjectInBucket(bucketName, result.getImageName());
            //2 create a new name with new content of MultipartFile & upload it into bucket
            String newName = uploadInBucket(newFile, bucketName);
            // 3 update db with new name
            result.setImageName(newName);
            imageRepository.persist(result);
        } else {
            // no image yet → create new one & save in storage bucket
            // clientImage entity is empty needs to be solved the problem
            String fileName = uploadInBucket(newFile, bucketName);

            boolean imageCreated = checkExistenceInStorage(bucketName, newFile);
            if(!imageCreated) return ResponseEntity.noContent().build();

            ImageDto dto = new ImageDto(null, fileName, ImageType.CLIENT, clientId);
            result = imageMapper.toEntity(dto, ctx);
            imageRepository.persist(result);
        }
        return ResponseEntity.ok().body(result);
    }
    private String uploadInBucket(MultipartFile image, String bucketName) {
        createBucket(bucketName);
        if(image.isEmpty() || image.getOriginalFilename() == null){
            throw new ImageUploadException("Image file empty");
        }
        String filename = generateFileImageName(image);
        InputStream in;
        try {
            in = image.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .stream(in, in.available(), -1)
                .build());
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed: " + e.getMessage());
        }
        return filename;
    }

    @SneakyThrows
    private void createBucket(String bucketName) {
        boolean exists  = minioClient.bucketExists(BucketExistsArgs.builder()
            .bucket(bucketName)
            .build());
        if(!exists ){
            minioClient.makeBucket(MakeBucketArgs.builder()
                .bucket(bucketName)
                .build());
        }
    }

    public List<ImageDto> getListPictureDtoOfProduct(Long productId) {
        return imageRepository.findByProductId(productId).stream()
            .map(imageMapper::toDto)
            .toList();
    }

    private void deleteObjectInBucket(String bucket, String object) throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build());
    }

    private String generateFileImageName(MultipartFile image) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return dtf.format(LocalDateTime.now()) +
            "-" + image.getOriginalFilename().toLowerCase();
    }

    @SneakyThrows
    public ResponseEntity productImageByItsId(Long id) {
        Optional<ProductImage> entity = imageRepository.findProductImageById(id);
        if (entity.isEmpty()) return ResponseEntity.notFound().build();

        String bucketName = "product-" + id + "-bucket";
        return generateResponseImageEntity(entity.get(), bucketName);
    }

    @SneakyThrows
    public ResponseEntity userImageByItsId(Long id) {
        Optional<UserImage> entity = imageRepository.findUserImageById(id);
        if (entity.isEmpty()) return ResponseEntity.notFound().build();

        String bucketName = "user-" + id + "-bucket";
        return generateResponseImageEntity(entity.get(), bucketName);
    }

    @SneakyThrows
    public ResponseEntity clientImageByItsId(Long id) {
        List<ClientImage> existingImages = imageRepository.findClientImageById(id);
        if (existingImages.isEmpty()) return ResponseEntity.notFound().build();

        Optional<ClientImage> latestImage = Optional.of(existingImages.stream()
            .max(Comparator.comparing(ClientImage::getCreatedAt))
            .orElse(null)); // returns null if list is empty
        if(latestImage.isEmpty()) return ResponseEntity.notFound().build();

        String bucketName = "client-" + id + "-bucket";
        return generateResponseImageEntity(latestImage.get(), bucketName);
    }

    @NotNull
    private ResponseEntity generateResponseImageEntity(ImageEntity entity, String bucketName)
        throws IOException, ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException {
        String objectName = entity.getImageName().toLowerCase();
        String extension = "image/" + objectName.substring(objectName.lastIndexOf(".") + 1).toLowerCase();

        byte[] bytes = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build()).readAllBytes();

        return ResponseEntity.ok()
            .contentLength(bytes.length)
            .contentType(MediaType.parseMediaType(extension))
            .body(new InputStreamResource(new ByteArrayInputStream(bytes)));
    }

    private boolean checkExistence(String bucketName, MultipartFile image, Long clientId){
        //check image exists in db
        Optional<ClientImage> existsInDB = imageRepository.existsByImageNameAndClientId(image.getOriginalFilename(), clientId);
        if (existsInDB.isPresent()) return true;

        //check image exists in minio storage
        return checkExistenceInStorage(bucketName, image);
    }
    private boolean checkExistenceInStorage(String bucketName, MultipartFile image){
        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(image.getOriginalFilename())
                    .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            //object does not exist in minio storage
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                return false;
            } else {
                throw new RuntimeException("Error occurred while checking object existence", e);
            }
        } catch (InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | ServerException | XmlParserException |
                 IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error occurred while checking object existence", e);
        }
    }
}
