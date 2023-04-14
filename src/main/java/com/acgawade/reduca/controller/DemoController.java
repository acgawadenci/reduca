package com.acgawade.reduca.controller;

import com.acgawade.reduca.repository.ProductRepository;
import com.acgawade.reduca.repository.UserRepository;
import com.acgawade.reduca.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {
    @Autowired
    UserRepository userRepository;
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

    @GetMapping("/getUser")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
