package com.deador.restshop.service.impl;

import com.deador.restshop.dto.security.RefreshTokenResponse;
import com.deador.restshop.exception.UserAuthenticationException;
import com.deador.restshop.model.RefreshToken;
import com.deador.restshop.model.User;
import com.deador.restshop.repository.RefreshTokenRepository;
import com.deador.restshop.security.JwtUtils;
import com.deador.restshop.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final String UNPROCESSED_REFRESH_TOKEN = "Refresh token is invalid or has been expired";
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   JwtUtils jwtUtils,
                                   PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String assignRefreshToken(User user) {
        String rawRefreshToken = jwtUtils.generateRefreshToken(user.getId());
        String encodedRefreshToken = passwordEncoder.encode(rawRefreshToken);
        if (user.getRefreshToken() != null) {
            user.getRefreshToken().setToken(encodedRefreshToken);
        } else {
            user.setRefreshToken(new RefreshToken().builder().user(user).token(encodedRefreshToken).build());
        }

        return rawRefreshToken;
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        RefreshToken refreshTokenFromDB = getRefreshToken(refreshToken);
        refreshTokenFromDB.revoke();
        refreshTokenRepository.delete(refreshTokenFromDB);
    }

    @Override
    public RefreshTokenResponse refreshAccessToken(String oldRefreshToken) {
        validateRefreshToken(oldRefreshToken);
        RefreshToken refreshToken = getRefreshToken(oldRefreshToken);
        String newRefreshToken = jwtUtils.generateRefreshToken(refreshToken.getId());
        refreshToken.setToken(passwordEncoder.encode(newRefreshToken));

        return RefreshTokenResponse.builder()
                .accessToken(jwtUtils.generateAccessToken(refreshToken.getUser().getEmail()))
                .refreshToken(newRefreshToken)
                .build();
    }

    private void validateRefreshToken(String rawRefreshToken) {
        if (!jwtUtils.isRefreshTokenValid(rawRefreshToken)) {
            throw new UserAuthenticationException(UNPROCESSED_REFRESH_TOKEN);
        }
    }

    private RefreshToken getRefreshToken(String rawRefreshToken) {
        Long userId = jwtUtils.getUserIdFromRefreshToken(rawRefreshToken);
        return refreshTokenRepository.findById(userId)
                .filter(refreshToken -> passwordEncoder.matches(rawRefreshToken, refreshToken.getToken()))
                .orElseThrow(() -> new UserAuthenticationException(UNPROCESSED_REFRESH_TOKEN));
    }
}
