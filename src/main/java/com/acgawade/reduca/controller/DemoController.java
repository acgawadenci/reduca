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
        Product prod = new Product();
        prod.setStatus("A");
        prod.setId(UUID.randomUUID());
        prod.setName("Herculous Bike");
        prod.setAddress("Fairview, Dublin 10");
        prod.setFeatures(List.of("Strong Chassis", "New model", "Repainted colour"));
        prod.setPrice(new BigDecimal(25));
        prod.setImages(List.of("https://x22103228-cpp.s3.eu-west-1.amazonaws.com/propId/344d8c1b-5b07-4937-9283-888962b987ac.png",
                "https://x22103228-cpp.s3.eu-west-1.amazonaws.com/propId/344d8c1b-5b07-4937-9283-888962b987ac.png",
                "https://x22103228-cpp.s3.eu-west-1.amazonaws.com/propId/344d8c1b-5b07-4937-9283-888962b987ac.png",
                "https://x22103228-cpp.s3.eu-west-1.amazonaws.com/propId/344d8c1b-5b07-4937-9283-888962b987ac.png"));
        prod.setPostedBy("Backend");
        prod.setYearMade("2018");
        prod.setDescription("Very good cycle");
        prod.setPostedOn(LocalDateTime.now());
        productRepository.save(prod);
        return "This is my demo Controller, Hello User !!!";
    }

    @GetMapping("/{userName}")
    public String demoResp(@PathVariable String userName) {
        return "This is also my demo Controller, Hello " + userName + " !!!";
    }
}
