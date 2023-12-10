package edu.example.demoproject.api;

import edu.example.demoproject.dtos.items.ItemCreateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping(edu.example.demoproject.api.ItemApi.DICTS_API_PATH)
@Tag(name = "Методы для работы сo штуками корзины", description = edu.example.demoproject.api.ItemApi.DICTS_API_PATH)
public interface ItemApi {

    String DICTS_API_PATH = "/api/items";

    @GetMapping("/{cartId}")
    @Operation(summary = "Получение продуктов в корзине по айди корзины/AUTH")
    ResponseEntity getItemsByCartId(@PathVariable Long cartId);//needs to be modified later with auth

    @PostMapping()
    @Operation(summary = "Положить в корзину новый продукт")
    @ResponseStatus(HttpStatus.OK)
    void addNewItem(@Valid @RequestBody ItemCreateDto dto);

    @PutMapping("/increment/{productId}")
    @Operation(summary = "Увеличить количество продукта в корзине по productId")
    @ResponseStatus(HttpStatus.OK)
    void incrementQtyItem(@PathVariable Long productId);

    @PutMapping("/decrement/{productId}")
    @Operation(summary = "Уменьшить количество продукта в корзине по productId")
    @ResponseStatus(HttpStatus.OK)
    void decrementQtyItem(@PathVariable Long productId);

    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "Удалить продукт из корзины по productId")
    @ResponseStatus(HttpStatus.OK)
    void delete(@PathVariable Long productId);

}
