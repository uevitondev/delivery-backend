package com.uevitondev.deliverybackend.domain.authentication;

import com.uevitondev.deliverybackend.config.security.jwt.TokenType;
import com.uevitondev.deliverybackend.domain.refreshtoken.RefreshTokenService;
import com.uevitondev.deliverybackend.domain.user.UserDetailsImpl;
import com.uevitondev.deliverybackend.domain.user.UserDetailsServiceImpl;
import com.uevitondev.deliverybackend.config.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService,
                                 UserDetailsServiceImpl userDetailsService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthResponseDTO signIn(AuthRequestDTO dto, HttpServletResponse response) {
        var usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        var authentication = authenticationManager.authenticate(usernamePasswordAuthentication);
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var jwtToken = jwtService.generateJwtToken(userDetails.getUsername());
        var refreshToken = refreshTokenService.generateRefreshTokenAndSave(userDetails.getUsername()).getToken();
        createRefreshTokenCookie(response, refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return buildAuthResponseDto(userDetails, jwtToken);
    }

    private AuthResponseDTO buildAuthResponseDto(UserDetailsImpl userDetails, String jwtToken) {
        return new AuthResponseDTO(
                TokenType.Bearer.name(),
                jwtToken,
                jwtService.getExpirationJwtToken(),
                userDetails.user().getFirstName(),
                userDetails.getUsername(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }

    public AuthResponseDTO getJwtTokenUsingRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        var refreshToken = extractCookieRefreshToken(request);
        var username = refreshTokenService.getUsernameRefreshTokenByToken(refreshToken);
        var userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        createAuthenticationObject(userDetails, request);
        createRefreshTokenCookie(response, refreshTokenService.generateRefreshTokenAndSave(userDetails.getUsername()).getToken());
        return buildAuthResponseDto(userDetails, jwtService.generateJwtToken(userDetails.getUsername()));
    }

    private void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/v1/auth/refresh-token");
        refreshTokenCookie.setMaxAge(604800);
        response.addCookie(refreshTokenCookie);
        log.info("[AuthenticationService:createRefreshTokenCookie] Response Cookie Refresh Token created: {}", refreshToken);
    }


    public String extractCookieRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            throw new AccessDeniedException("access denied");
        }
        log.info("[AuthenticationService:extractCookieRefreshToken] Cookie Refresh Token has ben extract: {}", refreshToken);
        return refreshToken;
    }

    private static Authentication createAuthenticationObject(UserDetails userDetails, HttpServletRequest request) {
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }


}
