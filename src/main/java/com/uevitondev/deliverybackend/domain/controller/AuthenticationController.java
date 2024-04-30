package com.uevitondev.deliverybackend.domain.controller;

import com.uevitondev.deliverybackend.domain.dto.AuthRequestDTO;
import com.uevitondev.deliverybackend.domain.dto.AuthResponseDTO;
import com.uevitondev.deliverybackend.domain.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery/v1/api/auth")
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDTO> signIn(@RequestBody AuthRequestDTO dto, HttpServletResponse response) {
        var authResponseDto = authService.signIn(dto, response);
        return ResponseEntity.ok().body(authResponseDto);
    }
}