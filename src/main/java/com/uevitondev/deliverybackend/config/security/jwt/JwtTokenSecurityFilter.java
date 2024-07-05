package com.uevitondev.deliverybackend.config.security.jwt;


import com.uevitondev.deliverybackend.domain.user.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtTokenSecurityFilter extends OncePerRequestFilter {
    Logger log = LoggerFactory.getLogger(JwtTokenSecurityFilter.class);
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final HandlerExceptionResolver resolver;

    public JwtTokenSecurityFilter(JwtService jwtService,
                                  UserDetailsServiceImpl userDetailsService,
                                  @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.resolver = resolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var jwtToken = getToken(request);
            if (jwtToken != null) {
                var username = jwtService.validateJwtToken(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.info("[JwtTokenSecurityFilter:doFilterInternal] handle exception in filter: {}", e.getMessage());
            resolver.resolveException(request, response, null, e);
        }
    }

    private String getToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
