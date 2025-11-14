package com.mmikata.product_service.model;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private int id;
    private String label;
    private boolean isImported;
    private ProductType productType;
    private BigDecimal freeTaxePrice;

    public enum ProductType {FOOD, MEDICINE, BOOK, OTHER};
}

