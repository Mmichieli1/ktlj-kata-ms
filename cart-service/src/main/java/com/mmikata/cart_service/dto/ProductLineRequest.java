package com.mmikata.cart_service.dto;

import com.mmikata.cart_service.model.ProductLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductLineRequest {
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
