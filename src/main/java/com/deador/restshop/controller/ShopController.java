package com.deador.restshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopController {
    @GetMapping("/shop")
    public ResponseEntity<String> viewHello() {
        return new ResponseEntity<>("Test role user & admin", HttpStatus.OK);
    }
}
