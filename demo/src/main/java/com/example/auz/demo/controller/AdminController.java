package com.example.auz.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String adminEndpoint() {
        return  "admin access granted!";
    }

}
