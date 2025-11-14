package com.mmikata.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private int id;
    private String label;
    private boolean isImported;
    private ProductType productType;
    private BigDecimal freeTaxePrice;

    public enum ProductType {FOOD, MEDICINE, BOOK, OTHER}
}
