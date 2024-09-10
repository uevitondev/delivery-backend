package com.uevitondev.deliverybackend.config.security;

import com.uevitondev.deliverybackend.config.security.jwt.JwtService;
import com.uevitondev.deliverybackend.config.security.jwt.JwtTokenSecurityFilter;
import com.uevitondev.deliverybackend.domain.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final HandlerExceptionResolver resolver;

    private static final String[] ENDPOINTS_PUBLIC = {
            "/v1/h2-console/**",
            "/v1/doc/**",
            "/v1/test/public/**",
            "/v1/stores/**",
            "/v1/categories/**",
            "/v1/products/**",
            "/v1/auth/sign-in/**",
            "/v1/auth/reset-password/**",
            "/v1/auth/change-password/**",
            "/v1/auth/refresh-token/**",
            "/v1/auth/sign-up/**",
            "/v1/auth/verification/**"
    };

    private static final String[] ENDPOINTS_ADMIN = {"/v1/test/admin/**"};


    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService,
                             JwtService jwtService,
                             @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.resolver = resolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .cors(httpCors -> httpCors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(ENDPOINTS_PUBLIC).permitAll()
                        .requestMatchers(ENDPOINTS_ADMIN).hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtTokenSecurityFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> {
                    handler.authenticationEntryPoint(authenticationEntryPointResolver());
                    handler.accessDeniedHandler(accessDeniedHandlerResolver());
                })
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenSecurityFilter jwtTokenSecurityFilter() {
        return new JwtTokenSecurityFilter(jwtService, userDetailsService, resolver);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPointResolver authenticationEntryPointResolver() {
        return new AuthenticationEntryPointResolver(this.resolver);
    }

    @Bean
    public AccessDeniedHandlerResolver accessDeniedHandlerResolver() {
        return new AccessDeniedHandlerResolver(this.resolver);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200",
                "https://delivery-ui-bay.vercel.app"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}