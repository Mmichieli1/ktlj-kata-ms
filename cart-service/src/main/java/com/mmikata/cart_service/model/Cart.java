package com.mmikata.cart_service.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    private String cartReference;
    private List<ProductLine> productsLines;
    private BigDecimal vat;
    private BigDecimal includingTaxPrice;
}
