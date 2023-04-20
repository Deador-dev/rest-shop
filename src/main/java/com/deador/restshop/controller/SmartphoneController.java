package com.deador.restshop.controller;

import com.deador.restshop.dto.smartphone.SmartphoneProfile;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import com.deador.restshop.service.SmartphoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class SmartphoneController {
    private final SmartphoneService smartphoneService;

    @Autowired
    public SmartphoneController(SmartphoneService smartphoneService) {
        this.smartphoneService = smartphoneService;
    }

    @GetMapping("/smartphones")
    public ResponseEntity<List<SmartphoneResponse>> getSmartphones() {
        return ResponseEntity.status(HttpStatus.OK).body(smartphoneService.getAllSmartphoneResponses());
    }

    @GetMapping("/smartphone/{id}")
    public ResponseEntity<SmartphoneResponse> getSmartphone(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(smartphoneService.getSmartphoneResponseById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/smartphone")
    public ResponseEntity<SmartphoneResponse> addSmartphone(@Valid @RequestBody SmartphoneProfile smartphoneProfile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(smartphoneService.addSmartphone(smartphoneProfile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/smartphone/{id}")
    public ResponseEntity<SmartphoneResponse> updateSmartphone(@PathVariable Long id,
                                                               @Valid @RequestBody SmartphoneProfile smartphoneProfile) {
        return ResponseEntity.status(HttpStatus.OK).body(smartphoneService.updateSmartphone(id, smartphoneProfile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/smartphone/{id}")
    public ResponseEntity<SmartphoneResponse> deleteSmartphone(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(smartphoneService.deleteSmartphoneById(id));
    }
}
