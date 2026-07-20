package org.example.productcatalogservice_may2026.services;

import org.example.productcatalogservice_may2026.dtos.FakeStoreProductDto;
import org.example.productcatalogservice_may2026.models.Category;
import org.example.productcatalogservice_may2026.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service("fkps")
public class FakeStoreProductService implements IProductService {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;


    public List<Product> getAllActiveProducts() {
        return null;
    }

    @Override
    public Product getProductBasedOnUserRole(Long productId, Long userId) {
        return null;
    }

    @Override
    public Product getProductById(Long id) {
       RestTemplate restTemplate = restTemplateBuilder.build();
//       ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
//              restTemplate.getForEntity("https://fakestoreapi.com/products/{id}",
//                      FakeStoreProductDto.class,id);


        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                requestForEntity(HttpMethod.GET,
                        "https://fakestoreapi.com/products/{id}",
                        null, FakeStoreProductDto.class,id);

       if (isValidateFakeStoreResponse(fakeStoreProductDtoResponseEntity)) {
           return from(fakeStoreProductDtoResponseEntity.getBody());
       }

       return null;
    }

    //ToDo
    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    //ToDo
    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Product replaceProduct(Product inputProduct, Long id) {
        FakeStoreProductDto inputDto = from(inputProduct);
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                requestForEntity(HttpMethod.PUT,"https://fakestoreapi.com/products/{id}",
                        inputDto, FakeStoreProductDto.class,id);

        if (isValidateFakeStoreResponse(fakeStoreProductDtoResponseEntity)) {
            return from(fakeStoreProductDtoResponseEntity.getBody());
        }

        return null;
    }

    //ToDo : For Students
    @Override
    public void deleteProduct(Long id) {

    }

    private Boolean isValidateFakeStoreResponse(ResponseEntity<FakeStoreProductDto>
                                                        fakeStoreProductDtoResponseEntity) {
        if (fakeStoreProductDtoResponseEntity.hasBody() &&
                fakeStoreProductDtoResponseEntity.getStatusCode()
                        .equals(HttpStatusCode.valueOf(200))) {
            return true;
        }
        return false;
    }

    private <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod, String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    private Product from(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return  product;
    }


    private FakeStoreProductDto from(Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setTitle(product.getName());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setImage(product.getImageUrl());
        if(product.getCategory() != null) {
            fakeStoreProductDto.setCategory(product.getCategory().getName());
        }
        return fakeStoreProductDto;
    }
}
