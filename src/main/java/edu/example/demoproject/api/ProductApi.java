package edu.example.demoproject.api;

import edu.example.demoproject.dtos.product.ProductCreateDto;
import edu.example.demoproject.dtos.product.ProductCriteriaDto;
import edu.example.demoproject.dtos.product.ProductDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;

@RequestMapping(ProductApi.DICTS_API_PATH)
@Tag(name = "Methods to work with products", description = ProductApi.DICTS_API_PATH)
@SecurityRequirement(name = "bearerAuth")
public interface ProductApi {
    String DICTS_API_PATH = "/api/products";

    @GetMapping("/main")
    public String getMainPage();

    @GetMapping("/info/{id}")
    @Operation(summary = "To get info about product")
    ProductDto getFullInfoById(@PathVariable Long id);

    @GetMapping
    @Operation(summary = "To get all products")
    List<ProductDto> getAll();

    @GetMapping("/search/title")
    @Operation(summary = "Search a match in product")
    List<ProductDto> findByTitle(@RequestParam(value = "title") String title);

    @GetMapping("/search/criteria")
    @Operation(summary = "Search a product by criteria(full match in name, description, new product, brand and category)")
    List<ProductDto> findByCriteria(@RequestBody ProductCriteriaDto dto);

    @PostMapping()
    @Operation(summary = "to add new product")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN')")
    Long create(@Valid @RequestBody ProductCreateDto newProductDto);

    @PutMapping("/{id}")
    @Operation(summary = "To update info(name, description, price, quantity, discount, brand and category)")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN')")
    Long update(@PathVariable Long id, @Valid @RequestBody ProductCreateDto newProductDto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product")
    @PreAuthorize("hasAnyRole('ADMIN')")
    ResponseEntity delete(@PathVariable Long id, Principal principal);

}
