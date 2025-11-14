package com.mmikata.product_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends Throwable {
    private int productId;

    public ProductNotFoundException(String msg, int productId) {
        super(msg);
        this.productId = productId;
    }

    public int getProductId() {
        return this.productId;
    }
}
