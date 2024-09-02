package com.uevitondev.deliverybackend.domain.authentication;

import com.uevitondev.deliverybackend.domain.tokenverification.TokenRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDTO> signIn(@RequestBody @Valid SignInRequestDTO dto, HttpServletResponse response) {
        var authResponseDto = authService.signIn(dto, response);
        return ResponseEntity.ok().body(authResponseDto);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDTO dto) {
        authService.signUp(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up/verification")
    public ResponseEntity<Void> signUp(@RequestBody @Valid TokenRequestDTO dto) {
        authService.signUpVerification(dto);
        return ResponseEntity.ok().build();
    }

}