package com.deador.restshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/admin/hello")
    public ResponseEntity<String> viewHello() {
        return new ResponseEntity<>("Test role admin", HttpStatus.OK);
    }
}
