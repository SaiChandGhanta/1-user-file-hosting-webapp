package com.example.demo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/healthz")
    @ResponseStatus( HttpStatus.OK )
    public String getHealthz() {
        return "Hello from healthz endpoint";
    }
}