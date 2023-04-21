package com.deador.restshop.controller.user;

import com.deador.restshop.dto.user.UserProfile;
import com.deador.restshop.dto.user.UserResponse;
import com.deador.restshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("!isAuthenticated()")
    @GetMapping("/verify")
    public ResponseEntity<UserResponse> verifyUser(@RequestParam String code) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.verify(code));
    }

    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserProfile userProfile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userProfile));
    }
}
