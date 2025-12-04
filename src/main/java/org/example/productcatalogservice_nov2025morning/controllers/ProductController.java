package org.example.productcatalogservice_nov2025morning.controllers;

import org.example.productcatalogservice_nov2025morning.dtos.CategoryDto;
import org.example.productcatalogservice_nov2025morning.dtos.ProductDto;
import org.example.productcatalogservice_nov2025morning.models.Product;
import org.example.productcatalogservice_nov2025morning.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/products")
    List<ProductDto> getAllProducts() {
      List<Product>products = productService.getAllProducts();
      return null;
    }


    @GetMapping("/products/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        if (productId < 1) {
           //throw new IllegalArgumentException("Please pass product id > 0");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            //throw new ProductNotFoundException("product not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductDto productDto = from(product);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    /**
     * @param productDto
     * @return ResponseEntity<ProductDto>
     */
    @PostMapping("/products")
    ResponseEntity<ProductDto> createProduct(@RequestBody
                          ProductDto productDto) {
        if (productDto == null || productDto.getName() == null || productDto.getName().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        try {
            Product productInput = to(productDto);
            Product createdProduct = productService.createProduct(productInput);
            ProductDto responseDto = from(createdProduct);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ProductDto from (Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());
        if(product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setDescription(product.getCategory().getDescription());
            productDto.setCategory(categoryDto);
        }
        return productDto;
    }

    private Product to(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        if (productDto.getCategory() != null) {
            var category = new org.example.productcatalogservice_nov2025morning.models.Category();
            category.setId(productDto.getCategory().getId());
            category.setName(productDto.getCategory().getName());
            category.setDescription(productDto.getCategory().getDescription());
            product.setCategory(category);
        }
        return product;
    }

}
