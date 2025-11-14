package com.mmikata.cart_service.service;

import com.mmikata.cart_service.dto.CartRequest;
import com.mmikata.cart_service.dto.CartResponse;
import com.mmikata.cart_service.dto.ProductLineRequest;
import com.mmikata.cart_service.exception.CartWithNoProductException;
import com.mmikata.cart_service.model.Cart;
import com.mmikata.cart_service.model.ProductLine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CartService {

    private final String NoProductExceptionMessage = "Cart should contain at least 1 product !";

    public CartResponse createCart(CartRequest cartRequest) throws CartWithNoProductException {
        if (cartRequest.getProductsLinesRequest().isEmpty()) {
            throw new CartWithNoProductException(NoProductExceptionMessage);
        }

        Cart cart = new Cart();
        cart.setCartReference(UUID.randomUUID().toString());
        
        List<ProductLine> productLines = cartRequest.getProductsLinesRequest()
                .stream()
                .map(this::mapToEntity)
                .toList();

        cart.setProductsLines(productLines);
        this.computeTaxes(cart);

        return this.mapToCartResponse(cart);
    }

    /**
     * Map ProductLineRequest to ProductLine for computation
     * @param productLineRequest object we want to compute
     * @return Entity ready for Computation
     */
    private ProductLine mapToEntity(ProductLineRequest productLineRequest) {
        ProductLine productLine = new ProductLine();
        productLine.setId(productLineRequest.getId());
        productLine.setProductName(productLineRequest.getProductName());
        productLine.setType(ProductLine.ProductLineType.valueOf(productLineRequest.getType().toString()));
        productLine.setImported(productLineRequest.isImported());
        productLine.setQuantity(productLineRequest.getQuantity());
        productLine.setFreeTaxPrice(productLineRequest.getFreeTaxPrice());
        return productLine;
    }

    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse cartResponse = CartResponse.builder()
                .cartReference(cart.getCartReference())
                .productsLines(cart.getProductsLines())
                .vat(cart.getVat())
                .includingTaxPrice(cart.getIncludingTaxPrice())
                .build();

        return cartResponse;
    }

    /**
     * Compute Taxes following specifics rules
     * @param cart with list of Products
     */
    private void computeTaxes(Cart cart) {
        log.info("Computing Taxes for cart");
        List<ProductLine> productsLines = cart.getProductsLines();

        productsLines.forEach(
                productLine -> {
                    String name = productLine.getProductName();
                    BigDecimal vat = getVat(productLine);
                    productLine.setVat(vat);
                    productLine.setTotalPrice(
                            productLine.getFreeTaxPrice()
                                    .add(vat)
                                    .multiply(BigDecimal.valueOf(productLine.getQuantity())));
                }
        );

        cart.setVat(this.getTotalVat(productsLines));
        cart.setIncludingTaxPrice(this.getTotalPrice(productsLines));
    }

    /**
     * Computation of Total VAT
     * @param productsLines given products in a Cart
     * @return productLines with Total VAT
     */
    private BigDecimal getTotalVat(List<ProductLine> productsLines) {
        return productsLines.stream()
                .map(line -> line.getVat().multiply(BigDecimal.valueOf(line.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Computation of TotalPrice
     * @param productsLines given products in a Cart
     * @return productLines with Total Price Including Taxes
     */
    private BigDecimal getTotalPrice(List<ProductLine> productsLines) {
        return productsLines.stream()
                .map(productLine -> productLine.getFreeTaxPrice()
                        .add(productLine.getVat())
                        .multiply(BigDecimal.valueOf(productLine.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getVat(ProductLine productLine) {
        BigDecimal rate = setVatRules(productLine.getType(), productLine.isImported());
        BigDecimal totalVat = productLine.getFreeTaxPrice().multiply(rate);
        return totalVat.setScale(2, RoundingMode.UP);
    }

    /**
     * Set VAT rules - Détermine la TVA à appliquer en fonction du type de produit.
     * @param type of the product
     * @param isImported product provenance
     * @return value of VAT
     */
    private BigDecimal setVatRules(ProductLine.ProductLineType type, boolean isImported) {
        double vat;
        switch (type) {
            case BOOK: vat = 0.1;
            break;
            case OTHER: vat = 0.2;
            break;
            default: vat = 0;
        }

        return isImported
                ? BigDecimal.valueOf(vat).add(BigDecimal.valueOf(0.5))
                : BigDecimal.valueOf(vat);
    }
}
