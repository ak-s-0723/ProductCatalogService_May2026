package org.example.productcatalogservice_may2026.controllers;

import org.example.productcatalogservice_may2026.dtos.CategoryDto;
import org.example.productcatalogservice_may2026.dtos.ProductDto;
import org.example.productcatalogservice_may2026.models.Category;
import org.example.productcatalogservice_may2026.models.Product;
import org.example.productcatalogservice_may2026.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//ToDo 8/6 -: Add PUT,PATCH and DELETE API
//ToDo 8/6 -: Read about RestTemplate

//ToDo : Exception Handlers to be shown by Anurag

@RestController
public class ProductController {
    //APIs

    @Autowired
    private IProductService productService;


    @GetMapping("/products")
    public List<ProductDto> getAllProducts() {
        return null;
    }


    @GetMapping("/products/{ID}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("ID") Long productId) {
        if (productId <= 0L) {
            //return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            throw new IllegalArgumentException("Please pass positive productId");
        }

        Product product = productService.getProductById(productId);
        if (product !=null) {
            ProductDto productDto = from(product);
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }


    @GetMapping("products/{productId}/{userId}")
    public ProductDto getUserDetailsBasedOnUserRole(@PathVariable Long productId, @PathVariable Long userId) {
      Product product = productService.getProductBasedOnUserRole(productId,userId);
      return from(product);
    }



    @PutMapping("/products/{productId}")
    public ProductDto replaceProduct(@PathVariable Long productId,
                                     @RequestBody ProductDto inputProductDto)
    {
      Product inputProduct = from(inputProductDto);
      Product response = productService.replaceProduct(inputProduct,productId);
      if(response !=null) {
          return from(response);
      }

      return null;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto input) {
        Product inputProduct = from(input);
        try {
            Product output = productService.createProduct(inputProduct);
            ProductDto response = from(output);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }




    private Product from(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        product.setDescription(productDto.getDescription());
        if(productDto.getCategory() != null) {
            Category category = new Category();
            category.setName(productDto.getCategory().getName());
            category.setId(productDto.getCategory().getId());
            product.setCategory(category);
        }
        return product;
    }

    private ProductDto from(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setId(product.getId());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());
        if(product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setDescription(product.getCategory().getDescription());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setId(product.getCategory().getId());
            productDto.setCategory(categoryDto);
        }

        return productDto;
    }


//    @GetMapping("/products/{catId}/{prodId}")
//    public Product getProduct(@PathVariable("catId") Long categoryId,
//                              @PathVariable("prodId") Long productId) {
//        Product product =new Product();
//        Category category = new Category();
//        category.setId(categoryId);
//        product.setCategory(category);
//        product.setId(productId);
//        return product;
//    }


}
