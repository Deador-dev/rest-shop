package com.deador.restshop.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private static final String ABSENT_TOKEN = "Token is empty: %s";
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilter(JwtUtils jwtUtils,
                     CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtUtils.getJwtFromRequest(request);
            if (StringUtils.hasText(accessToken) && jwtUtils.isAccessTokenValid(accessToken)) {
                String email = jwtUtils.getEmailFromAccessToken(accessToken);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("User with email {} successfully authenticate with token {}", email, accessToken);
            } else {
                log.debug(String.format(ABSENT_TOKEN, request.getRequestURI()));
            }

        } catch (Exception e) {
            logger.error("Could not authenticate user", e);
        }
        filterChain.doFilter(request, response);
    }
}