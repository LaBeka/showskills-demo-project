package edu.example.demoproject.api;


import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequestMapping(edu.example.demoproject.api.ImageApi.DICTS_API_PATH)
@Tag(name = "Methods to work with images(product, user, client)", description = edu.example.demoproject.api.ImageApi.DICTS_API_PATH)
@SecurityRequirement(name = "bearerAuth")
public interface ImageApi {
    String DICTS_API_PATH = "/api/images";

    @PostMapping(value = "/addProductImage/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add/Update product image")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN')")
    ResponseEntity uploadOrUpdateProductImage(
            @Valid @RequestPart("file") MultipartFile file,
            @PathVariable Long productId
    ) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;


    @PostMapping(value = "/addUserImage/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add/Update user image")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN')")
    ResponseEntity uploadOrUpdateUserImage(
        @Valid @RequestPart("file") MultipartFile file,
        @PathVariable Long userId
    ) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;


    @PostMapping(value = "/addClientImage/{clientId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add/Update client image")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN')")
    ResponseEntity uploadOrUpdateClientImage(
        @Valid @RequestPart("file") MultipartFile file,
        @PathVariable Long clientId
    ) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;


    @GetMapping("/getImageProduct/{productId}")
    @Operation(summary = "Get product image by product id")
    ResponseEntity getProductImage(@PathVariable Long productId) throws IOException;

    @GetMapping("/getImageClient/{clientId}")
    @Operation(summary = "Get client image by client id")
    ResponseEntity getClientImage(@PathVariable Long clientId) throws IOException;

    @GetMapping("/getImageUser/{userId}")
    @Operation(summary = "Get user image by user id")
    ResponseEntity getUserImage(@PathVariable Long userId) throws IOException;

    @GetMapping("/getListImages/{productId}")
    @Operation(summary = "To get list of pictures by id of product")
    ResponseEntity getListImages(@PathVariable Long productId) throws IOException;

//    @DeleteMapping("/delete/{id}")
//    @Operation(summary = "To delete picture of product by id of picture")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN')")

//    void delete(@PathVariable Long id);
//
//    @PutMapping("/update/{id}")
//    @Operation(summary = "To update picture by its id")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    ResponseEntity updateProductImage(
//            @Valid @RequestPart("file")MultipartFile file,

//            @PathVariable Long id) throws IOException;


}