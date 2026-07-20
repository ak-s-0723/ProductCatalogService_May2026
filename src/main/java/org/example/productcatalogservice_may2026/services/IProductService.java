package org.example.productcatalogservice_may2026.services;

import org.example.productcatalogservice_may2026.models.Product;

import java.util.List;

public interface IProductService {

    Product getProductById(Long id);

    List<Product> getAllProducts();

    Product createProduct(Product product);

    Product replaceProduct(Product product,Long id);

    void deleteProduct(Long id);

    List<Product> getAllActiveProducts();

    Product getProductBasedOnUserRole(Long productId, Long userId);
}
