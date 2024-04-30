package com.uevitondev.deliverybackend.security.jwt;


import com.uevitondev.deliverybackend.security.userimpl.UserDetailsServiceImpl;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenSecurityFilter extends OncePerRequestFilter {
    Logger log = LoggerFactory.getLogger(JwtTokenSecurityFilter.class);
    private final List<String> publicEndpoints;
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final HandlerExceptionResolver resolver;


    public JwtTokenSecurityFilter(List<String> publicEndpoints, JwtService jwtService, UserDetailsServiceImpl userDetailsService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.publicEndpoints = publicEndpoints;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.resolver = resolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();
            if (isPublicEndpoint(requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }

            var token = this.getToken(request);
            if (token != null) {
                var username = jwtService.validateAccessToken(token);
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

    private boolean isPublicEndpoint(String requestURI) {
        return publicEndpoints.stream().anyMatch(endpoint -> pathMatcher.match(endpoint, requestURI));
    }

    private String getToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
