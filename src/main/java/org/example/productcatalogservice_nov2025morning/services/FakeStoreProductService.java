package org.example.productcatalogservice_nov2025morning.services;

import org.example.productcatalogservice_nov2025morning.dtos.FakeStoreProductDto;
import org.example.productcatalogservice_nov2025morning.models.Category;
import org.example.productcatalogservice_nov2025morning.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FakeStoreProductService implements IProductService {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public Product getProductById(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                restTemplate.getForEntity("https://fakestoreapi.com/products/{id}",
                FakeStoreProductDto.class,
                id);

        if(fakeStoreProductDtoResponseEntity.hasBody() &&
                fakeStoreProductDtoResponseEntity.getStatusCode().
                        equals(HttpStatusCode.valueOf(200))) {
            return from(fakeStoreProductDtoResponseEntity.getBody());
        }

        return null;
    }

    //ToDo : to be done by students
    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public Product createProduct(Product input) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(input.getId());
        fakeStoreProductDto.setTitle(input.getName());
        fakeStoreProductDto.setDescription(input.getDescription());
        fakeStoreProductDto.setPrice(input.getPrice());
        fakeStoreProductDto.setImage(input.getImageUrl());
        fakeStoreProductDto.setCategory(input.getCategory().getName());

        ResponseEntity<FakeStoreProductDto> response = restTemplate.postForEntity(
            "https://fakestoreapi.com/products",
            fakeStoreProductDto,
            FakeStoreProductDto.class
        );
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        if (response.hasBody() && response.getStatusCode().equals(HttpStatusCode.valueOf(201))) {
            return from(response.getBody());
        }
        return null;
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
        return product;
    }
}
