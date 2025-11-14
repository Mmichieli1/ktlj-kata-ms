package com.mmikata.cart_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CartWithNoProductException extends Throwable {
    public CartWithNoProductException(String msg) {
        super(msg);
    }
}
