package com.acgawade.reduca.service;

import com.acgawade.reduca.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Test
    void fetchProducts() {
        
    }

    @Test
    void fetchMyProducts() {
    }

    @Test
    void saveProduct() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void inactiveProduct() {
    }

    @Test
    void updateImageUrl() {
    }

    @Test
    void emailProductOwner() {
    }
}