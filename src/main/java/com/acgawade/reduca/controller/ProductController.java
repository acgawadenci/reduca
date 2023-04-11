package com.acgawade.reduca.controller;

import com.acgawade.reduca.entity.Product;
import com.acgawade.reduca.model.ResponseModel;
import com.acgawade.reduca.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(productService.fetchProducts(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseModel> postProperty(@RequestBody Product product) {
        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ResponseModel> updateProperty(@PathVariable UUID productId,
                                                        @RequestBody Product property) {
        return new ResponseEntity<>(productService.updateProduct(productId, property), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseModel> deleteProperty(@PathVariable UUID productId) {
        return new ResponseEntity<>(productService.inactiveProduct(productId), HttpStatus.OK);
    }
}
