package com.deador.restshop.controller;

import com.deador.restshop.service.SmartphoneImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SmartphoneImageController {
    private final SmartphoneImageService smartphoneImageService;

    @Autowired
    public SmartphoneImageController(SmartphoneImageService smartphoneImageService) {
        this.smartphoneImageService = smartphoneImageService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/smartphoneImage/{id}")
    public ResponseEntity<Object> deleteSmartphoneImage(@PathVariable Long id) {
        smartphoneImageService.deleteSmartphoneImageById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
