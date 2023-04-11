package com.acgawade.reduca.controller;

import com.acgawade.reduca.entity.Product;
import com.acgawade.reduca.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class DemoController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/")
    public String demoResponse() {
        return "This is my demo Controller, Hello User !!!";
    }

    @GetMapping("/{userName}")
    public String demoResp(@PathVariable String userName) {
        return "This is also my demo Controller, Hello " + userName + " !!!";
    }
}
