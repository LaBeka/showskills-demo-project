package edu.example.demoproject.services;

import edu.example.demoproject.contextHelpers.ImageMappingContext;
import edu.example.demoproject.dtos.image.ImageDto;
import edu.example.demoproject.entities.image.ImageEntity;
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
import java.util.List;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    private final MinioClient minioClient;
    private final ImageMappingContext ctx;

    public List<ImageDto> getListPictureDtoOfProduct(Long productId) {
        return imageRepository.getImagesOfProduct(productId);
    }

    @SneakyThrows
    @Transactional
    public void update(MultipartFile newFile, Long id) throws IOException {
        deleteObjectInBucket(id);
        Long productId = imageRepository.getImageByItsId(id).getProductId();//getonlyid
        String newFileName = uploadInBucket(newFile, productId);
        ImageEntity newEntity = imageMapper.buildEntity(id, productId, newFileName);
        imageRepository.merge(newEntity);
    }

    @SneakyThrows
    @Transactional
    public void delete(Long pictureId) {
        deleteObjectInBucket(pictureId);
        imageRepository.deleteObjectInDB(pictureId);
    }

    private void deleteObjectInBucket(Long pictureId) throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        ImageEntity entity = imageRepository.getImageByItsId(pictureId);
        String bucketName = "product-" + entity.getProductId() + "-bucket";
        String objectName = entity.getImageName();

        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    @SneakyThrows
    public ResponseEntity getPictureByItsId(Long id) {
        ImageEntity entity = imageRepository.getImageByItsId(id);
        String bucketName = "product-" + entity.getProductId() + "-bucket";
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

    @Transactional
    public void uploadImage(MultipartFile image, Long productId)  {
        String imageName = uploadInBucket(image, productId);
        ImageEntity entity = imageMapper.buildEntity(null, productId, imageName);
        imageRepository.persist(entity);
    }

    private String uploadInBucket(MultipartFile image, Long productId) {
        String bucketName = "product-" + productId + "-bucket";
        createBucket(bucketName);
        if(image.isEmpty() || image.getOriginalFilename() == null){
            throw new ImageUploadException("Image file empty");
        }
        String filename = generateFileImageName(image);
        InputStream inputStream;
        try {
            inputStream = image.getInputStream();
        } catch (IOException e) {
            throw new ImageUploadException("Image upload failed: " + e.getMessage());
        }
        saveImageInBucket(inputStream, filename, bucketName);
        return filename;
    }

    @SneakyThrows
    private void createBucket(String bucketName) {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());
        if(!found){
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }
    }

    private String generateFileImageName(MultipartFile image) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String newName = dtf.format(LocalDateTime.now()) +
                "-" + image.getOriginalFilename().toLowerCase();
        return newName;
    }

    @SneakyThrows
    private void saveImageInBucket(InputStream inputStream, String filename, String bucketName) {
        minioClient.putObject(PutObjectArgs.builder()
                        .stream(inputStream, inputStream.available(), -1)
                        .bucket(bucketName)
                        .object(filename)
                .build());
    }

}
