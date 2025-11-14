package com.mmikata.product_service.controller;

import com.mmikata.product_service.dto.ProductResponse;
import com.mmikata.product_service.exception.ProductNotFoundException;
import com.mmikata.product_service.service.ProductService;
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

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        log.info("Get All Products from Referential");
        return productService.getAll();
    }

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
