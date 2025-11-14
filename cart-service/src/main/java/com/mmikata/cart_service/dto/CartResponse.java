package com.mmikata.cart_service.dto;

import com.mmikata.cart_service.model.ProductLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private String cartReference;
    private List<ProductLine> productsLines;
    private BigDecimal vat;
    private BigDecimal includingTaxPrice;
}
