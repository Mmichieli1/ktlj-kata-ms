package com.mmikata.product_service.controller;

import com.mmikata.product_service.dto.ProductResponse;
import com.mmikata.product_service.exception.ProductNotFoundException;
import com.mmikata.product_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All Products Found",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse[].class)) })
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        log.info("Get All Products from Referential");
        return productService.getAll();
    }

    @Operation(summary = "Get a product by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Product Found",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)) })
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(@PathVariable("id") Integer id) {
        log.info("Get Product with id {}. ", id);
        ProductResponse productResponse = null;
        try {
            productResponse = productService.getById(id);
        } catch (ProductNotFoundException e) {
            log.error(e.getMessage());
            log.error("Unknown id {} requested. ", e.getProductId());
        }

        return productResponse;
    }
}
