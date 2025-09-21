package edu.example.demoproject.controllers;

import edu.example.demoproject.api.ImageApi;
import edu.example.demoproject.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class ImageController implements ImageApi {
    private final ImageService service;

    @Override
    public ResponseEntity getProductImage(Long productId) throws IOException {
        return service.productImageByItsId(productId);
    }

    @Override
    public ResponseEntity getUserImage(Long userId) throws IOException {
        return service.userImageByItsId(userId);
    }

    @Override
    public ResponseEntity getClientImage(Long clientId) throws IOException {
        return service.clientImageByItsId(clientId);
    }

    @Override
    public ResponseEntity uploadOrUpdateProductImage(MultipartFile file, Long productId) {
        service.updateUploadProductImage(file, productId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity uploadOrUpdateUserImage(MultipartFile file, Long userId){
        service.updateUploadUserImage(file, userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity uploadOrUpdateClientImage(MultipartFile file, Long clientId){
        return service.updateUploadClientImage(file, clientId);
    }

    @Override
    public ResponseEntity getListImages(Long productId) {
        return ResponseEntity.ok().body(service.getListPictureDtoOfProduct(productId));
    }
}
