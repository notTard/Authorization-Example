package com.example.auz.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;






@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping // Дополнительная проверка
    public String apiEndpoint() {
        return "Api Access Granted!";
    }
    
}
