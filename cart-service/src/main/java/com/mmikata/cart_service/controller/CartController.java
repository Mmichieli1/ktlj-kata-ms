package com.mmikata.cart_service.controller;

import com.mmikata.cart_service.dto.CartRequest;
import com.mmikata.cart_service.dto.CartResponse;
import com.mmikata.cart_service.dto.ProductLineRequest;
import com.mmikata.cart_service.dto.ProductResponse;
import com.mmikata.cart_service.exception.CartWithNoProductException;
import com.mmikata.cart_service.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final WebClient.Builder webClientBuilder;

    @Operation(summary = "Create a cart choosing existing products. ")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Cart created successfully",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class)) })
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse createCart(@RequestBody List<String> productIds) {
        List<ProductLineRequest> selectedProducts = Arrays.stream(fetchProductsFromReferential())
                .filter(p -> productIds.contains(String.valueOf(p.getId())))
                .map(this::mapProductResponsesToProductLineRequest)
                .toList();

        return this.getCartResponse(selectedProducts);
    }

    @Operation(summary = "Create a cart containing all products.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Cart created successfully",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse[].class)) })
    })
    @GetMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public CartResponse createCartFetchingAllProducts() {
        List<ProductLineRequest> productLineRequests = Arrays.stream(fetchProductsFromReferential())
                .map(this::mapProductResponsesToProductLineRequest)
                .toList();

        return this.getCartResponse(productLineRequests);
    }

    /**
     * Fetch Products from Referential
     * @return Array of ProductResponse
     */
    private ProductResponse[] fetchProductsFromReferential() {
        // Call Product Referential to Create a valid CartRequest objects
        log.info("Call Api Product Referential ! ");
        ProductResponse[] productResponses = webClientBuilder.build().get()
                .uri("http://product-service/api/products")
                .retrieve()
                .bodyToMono(ProductResponse[].class)
                .block();


        if (productResponses == null || productResponses.length == 0) {
            throw new IllegalArgumentException("Empty list of products ! ");
        }

        return productResponses;
    }

    /**
     * Get CartResponse with computed Taxes
     * @param givenProducts list of Products fetched from Referential
     * @return the CartResponse object
     */
    private CartResponse getCartResponse(List<ProductLineRequest> givenProducts) {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductsLinesRequest(givenProducts);

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
