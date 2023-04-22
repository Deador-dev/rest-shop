package com.deador.restshop.controller.user;

import com.deador.restshop.dto.security.RefreshTokenRequest;
import com.deador.restshop.dto.security.RefreshTokenResponse;
import com.deador.restshop.dto.user.SuccessLogin;
import com.deador.restshop.dto.user.UserLogin;
import com.deador.restshop.service.RefreshTokenService;
import com.deador.restshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class LoginController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public LoginController(UserService userService,
                           RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/signin")
    public ResponseEntity<SuccessLogin> signIn(@Valid @RequestBody UserLogin userLogin) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(userLogin));
    }

    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/token/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(refreshTokenService.refreshAccessToken(request.getRefreshToken()));
    }

    @PreAuthorize("!isAuthenticated()")
    @PostMapping("/token/revoke")
    public ResponseEntity<Object> revokeRefreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
