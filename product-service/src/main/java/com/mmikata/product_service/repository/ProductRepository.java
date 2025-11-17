package com.mmikata.product_service.repository;

import com.mmikata.product_service.model.Product;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Slf4j
public class ProductRepository {
    private static int count = 0;
    private List<Product> products = new ArrayList<>();

    // Setter
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Find All Products
     * @return all Products Created in local
     */
    public List<Product> findAllProducts() {
        if (products.isEmpty()) {
            this.setProducts(createProducts());
        }

        return this.products;
    }

    /**
     * Get Product By Id
     * @param productId
     * @return Requested Product
     */
    public List<Product> getProductById(int productId) {
        if (products.isEmpty()) {
            this.setProducts(createProducts());
        }

        return this.products.stream().filter(p -> p.getId() == productId).toList();
    }

    private List<Product> createProducts() {
        List<Product> products = new ArrayList<>();
        if (count != 1) {
            /* Creates 11 Products in Standalone mode */
            log.info("Creating Products !");

            Product product1 = new Product(1, "Le Roi Lion", false, Product.ProductType.BOOK, BigDecimal.valueOf(14.20));
            Product product2 = new Product(2, "Clé USB 16GB", false, Product.ProductType.OTHER, BigDecimal.valueOf(16.40));
            Product product3 = new Product(3, "Sandwich SODEBA", false, Product.ProductType.FOOD, BigDecimal.valueOf(1.99));
            Product product4 = new Product(4, "Lot de 2 paires de Chaussettes", true, Product.ProductType.OTHER, BigDecimal.valueOf(6.57));
            Product product5 = new Product(5, "Surchemise à carreaux", true, Product.ProductType.OTHER, BigDecimal.valueOf(27.42));
            Product product6 = new Product(6, "1 Kg d'aiguilletes de Poulet", true, Product.ProductType.FOOD, BigDecimal.valueOf(9.85));
            Product product7 = new Product(7, "Paracetamol", true, Product.ProductType.MEDICINE, BigDecimal.valueOf(4.57));
            Product product8 = new Product(8, "Sirop contre la toux", false, Product.ProductType.MEDICINE, BigDecimal.valueOf(4.84));
            Product product9 = new Product(9, "Le livre du Truc", false, Product.ProductType.BOOK, BigDecimal.valueOf(17.35));
            Product product10 = new Product(10, "1 Kg M&Ms", true, Product.ProductType.FOOD, BigDecimal.valueOf(7.43));
            Product product11 = new Product(11, "Germinal", true, Product.ProductType.BOOK, BigDecimal.valueOf(16.27));

            products =  List.of(product1, product2, product3, product4, product5, product6, product7, product8, product9, product10, product11);
            count++;
        }

        return products;
    }
}
