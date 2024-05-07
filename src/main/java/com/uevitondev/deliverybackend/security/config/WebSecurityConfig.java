package com.uevitondev.deliverybackend.security.config;

import com.uevitondev.deliverybackend.security.jwt.JwtService;
import com.uevitondev.deliverybackend.security.jwt.JwtTokenSecurityFilter;
import com.uevitondev.deliverybackend.domain.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final HandlerExceptionResolver resolver;

    private static final String[] ENDPOINTS_PUBLIC = {"/delivery/v1/h2-console/**", "/delivery/v1/api/test/public/**", "/delivery/v1/api/auth/sign-in/**"};
    private static final String[] ENDPOINTS_ADMIN = {"/delivery/v1/api/admin/**"};


    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtService jwtService,
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
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtTokenSecurityFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> {
                    handler.authenticationEntryPoint(customBasicAuthenticationEntryPoint());
                    handler.accessDeniedHandler(customBearerTokenAccessDeniedHandler());
                })
                .build();
    }

    @Bean
    public JwtTokenSecurityFilter jwtTokenSecurityFilter() {
        List<String> publicEndpoints = Arrays.asList(ENDPOINTS_PUBLIC);
        return new JwtTokenSecurityFilter(publicEndpoints, jwtService, userDetailsService, resolver);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint() {
        return new CustomBasicAuthenticationEntryPoint(this.resolver);
    }

    @Bean
    public CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler() {
        return new CustomBearerTokenAccessDeniedHandler(this.resolver);
    }


}