package com.mmikata.cart_service.controller;

import com.mmikata.cart_service.dto.CartRequest;
import com.mmikata.cart_service.dto.CartResponse;
import com.mmikata.cart_service.dto.ProductLineRequest;
import com.mmikata.cart_service.dto.ProductResponse;
import com.mmikata.cart_service.exception.CartWithNoProductException;
import com.mmikata.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;
    private final WebClient webClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse createCart(@RequestBody CartRequest cartRequest) {
        CartResponse cartResponse = null;
        try {
            log.info("Create a new Cart. ");
            cartResponse = cartService.createCart(cartRequest);
            log.info("Cart with reference {} created Successfully. ", cartResponse.getCartReference());
        } catch (CartWithNoProductException e) {
            log.error(e.getMessage());
        }

        return cartResponse;
    }

    @GetMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public CartResponse createCartFetchingProducts() {
        CartRequest cartRequest = new CartRequest();

        // Call Product Referential to Create a valid CartRequest objects
        log.info("Call Api Product Referential ! ");
        ProductResponse[] productResponses = webClient.get()
                .uri("http://localhost:8081/api/products")
                .retrieve()
                .bodyToMono(ProductResponse[].class)
                .block();


        if (productResponses == null || productResponses.length == 0) {
            throw new IllegalArgumentException("Empty list of products ! ");
        }

        List<ProductLineRequest> productLineRequests = Arrays.stream(productResponses)
                .map(this::mapProductResponsesToProductLineRequest)
                .toList();

        cartRequest.setProductsLinesRequest(productLineRequests);

        // Creating Bill
        CartResponse cartResponse = null;
        try {
            cartResponse = cartService.createCart(cartRequest);
            log.info("Cart with reference {} Created Successfully. ", cartResponse.getCartReference());
        } catch (CartWithNoProductException e) {
            log.error(e.getMessage());
        }

        return cartResponse;
    }

    private ProductLineRequest mapProductResponsesToProductLineRequest(ProductResponse productResponse) {
        Random r = new Random();
        return ProductLineRequest.builder()
                .id(productResponse.getId())
                .productName(productResponse.getLabel())
                .type(ProductLineRequest.ProductLineType.valueOf(productResponse.getProductType().toString()))
                .isImported(productResponse.isImported())
                .quantity(r.nextInt(7)+1)
                .freeTaxPrice(productResponse.getFreeTaxePrice())
                .build();
    }
}
