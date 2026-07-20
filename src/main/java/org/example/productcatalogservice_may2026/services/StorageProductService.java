package org.example.productcatalogservice_may2026.services;

import org.example.productcatalogservice_may2026.dtos.UserDto;
import org.example.productcatalogservice_may2026.models.Product;
import org.example.productcatalogservice_may2026.models.State;
import org.example.productcatalogservice_may2026.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("sps")
@Primary
public class StorageProductService implements IProductService {

    @Autowired
    private ProductRepo productRepo;


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if(optionalProduct.isPresent()) {
            return optionalProduct.get();
        }

        return null;
     }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }


    @Override
    public List<Product> getAllActiveProducts() {
        List<Product> activeProducts = new ArrayList<>();
        List<Product> products = productRepo.findAll();
        for(Product product : products) {
            if(product.getState().equals(State.ACTIVE))
            activeProducts.add(product);
        }

        return activeProducts;
    }

    @Override
    public Product getProductBasedOnUserRole(Long productId, Long userId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if(optionalProduct.isEmpty()) return null;

        UserDto userDto = restTemplate.getForEntity("http://userservice/users/{userId}", UserDto.class, userId).getBody();
        if(userDto != null) {
            System.out.println(userDto.getEmail());
            System.out.println(userDto.getName());
            return optionalProduct.get();
        }

      return null;
    }

    @Override
    public Product createProduct(Product product) {
        Optional<Product> productOptional = productRepo.findById(product.getId());
        if (productOptional.isPresent()) {
            throw new RuntimeException("Product with id "+product.getId()+" already exist");
        }

        return productRepo.save(product);
    }

    //ToDo : For students
    @Override
    public Product replaceProduct(Product product, Long id) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> productOptional = productRepo.findById(id);
        if (productOptional.isEmpty()) {
            throw new RuntimeException("Product with id "+id+" not available");
        }

        Product product = productOptional.get();
        if (product.getState().equals(State.ACTIVE)) {
            //Soft Delete
            System.out.println("Soft Deleting product with id"+id);
           product.setState(State.INACTIVE);
           product.setLastUpdatedAt(new Date());
           productRepo.save(product);
        } else {
            //Hard delete
            System.out.println("Hard Deleting product with id"+id);
            productRepo.deleteById(id);
        }
    }
}
