package com.mmikata.product_service.service;

import com.mmikata.product_service.dto.ProductResponse;
import com.mmikata.product_service.exception.ProductNotFoundException;
import com.mmikata.product_service.model.Product;
import com.mmikata.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository = new ProductRepository();
    private final String productNotFoundMessage = "No product found !";

    /**
     * Get All Products - Récupérer la liste exhaustive des produits
     * @return list de ProductResponses
     */
    public List<ProductResponse> getAll() {
        List<Product> products = productRepository.findAllProducts();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    /**
     * Get Product - Récupérer un produit via son identifiant
     * @param productId
     * @return
     */
    public ProductResponse getById(Integer productId) throws ProductNotFoundException {
        List<Product> filteredProduct = productRepository.getProductById(productId);
        if (filteredProduct.isEmpty()) {
            throw new ProductNotFoundException(this.productNotFoundMessage, productId);
        }

        return this.mapToProductResponse(filteredProduct.getFirst());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .label(product.getLabel())
                .productType(ProductResponse.ProductType.valueOf(product.getProductType().toString()))
                .isImported(product.isImported())
                .freeTaxePrice(product.getFreeTaxePrice())
                .build();
    }
}
