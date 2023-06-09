package com.deador.restshop.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {
    public static final String USER_ID = "user_id";
    private final String accessTokenSecret;
    private final String refreshTokenSecret;
    private final Integer accessExpirationTimeInMinutes;
    private final Integer refreshExpirationTimeInDays;

    public JwtUtils(@Value("${application.jwt.accessTokenSecret}") String accessTokenSecret,
                    @Value("${application.jwt.refreshTokenSecret}") String refreshTokenSecret,
                    @Value("${application.jwt.accessExpirationTimeInMinutes}") Integer accessExpirationTimeInMinutes,
                    @Value("${application.jwt.refreshExpirationTimeInDays}") Integer refreshExpirationTimeInDays) {
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.accessExpirationTimeInMinutes = accessExpirationTimeInMinutes;
        this.refreshExpirationTimeInDays = refreshExpirationTimeInDays;
    }

    public String generateAccessToken(String email) {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.MINUTE, accessExpirationTimeInMinutes);
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS512, accessTokenSecret)
                .compact();
    }

    public String generateRefreshToken(Long id) {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.DATE, refreshExpirationTimeInDays);
        Claims claims = Jwts.claims();
        claims.put(USER_ID, id);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS512, refreshTokenSecret)
                .compact();
    }

    private Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    public boolean isAccessTokenValid(String accessToken) {
        try {
            Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(refreshTokenSecret).parseClaimsJws(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromAccessToken(String accessToken) {
        Claims claims = Jwts.parser().setSigningKey(accessTokenSecret)
                .parseClaimsJws(accessToken).getBody();
        return claims.getSubject();
    }

    public Long getUserIdFromRefreshToken(String refreshToken) {
        Claims claims = Jwts.parser().setSigningKey(refreshTokenSecret)
                .parseClaimsJws(refreshToken).getBody();
        return claims.get(USER_ID, Long.class);
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}