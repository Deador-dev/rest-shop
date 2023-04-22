package com.deador.restshop.service;

import com.deador.restshop.dto.security.RefreshTokenResponse;
import com.deador.restshop.model.User;

public interface RefreshTokenService {
    String assignRefreshToken(User user);

    void revokeRefreshToken(String refreshToken);

    RefreshTokenResponse refreshAccessToken(String refreshToken);
}
