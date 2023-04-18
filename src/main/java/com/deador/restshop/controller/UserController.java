package com.deador.restshop.controller;

import com.deador.restshop.dto.user.UserResponse;
import com.deador.restshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUserResponses());
    }

    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == #id)")
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserResponseById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{role}")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable(name = "role") String roleName) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserResponsesByRole(roleName));
    }
}
