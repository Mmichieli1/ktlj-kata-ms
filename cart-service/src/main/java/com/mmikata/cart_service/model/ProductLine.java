package com.mmikata.cart_service.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductLine {
    private int id;
    private String productName;
    private ProductLineType type;
    private boolean isImported;
    private int quantity;
    private BigDecimal freeTaxPrice;
    private BigDecimal vat;
    private BigDecimal totalPrice;

    public enum ProductLineType {FOOD, MEDICINE, BOOK, OTHER};
}
