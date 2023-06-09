package com.acgawade.reduca.controller;

import com.acgawade.reduca.entity.Product;
import com.acgawade.reduca.model.ResponseModel;
import com.acgawade.reduca.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(Principal principal) {
        return new ResponseEntity<>(productService.fetchProducts(), HttpStatus.OK);
    }

    @GetMapping("/myProducts")
    public ResponseEntity<List<Product>> getListedProducts(Principal principal) {
        return new ResponseEntity<>(productService.fetchMyProducts(principal.getName()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseModel> postProperty(@RequestBody Product product, Principal principal) {
        return new ResponseEntity<>(productService.saveProduct(product, principal.getName()), HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ResponseModel> updateProperty(@PathVariable UUID productId,
                                                        @RequestBody Product property,
                                                        Principal principal) {
        return new ResponseEntity<>(productService.updateProduct(productId, property, principal.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseModel> deleteProperty(@PathVariable UUID productId, Principal principal) {
        return new ResponseEntity<>(productService.inactiveProduct(productId, principal.getName()), HttpStatus.OK);
    }

    @PostMapping("/contact/{productId}")
    public ResponseEntity<ResponseModel> contactOwner(@PathVariable UUID productId, Principal principal) {
        return new ResponseEntity<>(productService.emailProductOwner(productId, principal.getName()), HttpStatus.CREATED);
    }
}
