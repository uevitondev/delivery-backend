package com.uevitondev.deliverybackend.domain.service;

import com.uevitondev.deliverybackend.domain.dto.AuthRequestDTO;
import com.uevitondev.deliverybackend.domain.dto.AuthResponseDTO;
import com.uevitondev.deliverybackend.security.jwt.JwtService;
import com.uevitondev.deliverybackend.security.userimpl.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO signIn(AuthRequestDTO dto, HttpServletResponse response) {
        var usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        var authentication = authenticationManager.authenticate(usernamePasswordAuthentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var authResponseDTO = jwtService.generateAccessToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
        log.info("[AuthService:signIn] created accessToken : {}", authResponseDTO.getAccessToken());
        log.info("[AuthService:signIn] created refreshToken : {}", refreshToken);
        //saveUserRefreshToken(user, refreshToken);
        //createRefreshTokenCookie(response, refreshToken);
        return authResponseDTO;
    }

}
