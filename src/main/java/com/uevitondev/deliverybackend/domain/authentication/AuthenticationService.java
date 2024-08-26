package com.uevitondev.deliverybackend.domain.authentication;

import com.uevitondev.deliverybackend.config.security.jwt.JwtService;
import com.uevitondev.deliverybackend.config.security.jwt.TokenType;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.exception.UserAlreadyExistsException;
import com.uevitondev.deliverybackend.domain.refreshtoken.RefreshTokenService;
import com.uevitondev.deliverybackend.domain.role.RoleRepository;
import com.uevitondev.deliverybackend.domain.user.User;
import com.uevitondev.deliverybackend.domain.user.UserDetailsImpl;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import com.uevitondev.deliverybackend.domain.utils.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CookieService cookieService;


    public AuthenticationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            CookieService cookieService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.cookieService = cookieService;
    }

    public AuthResponseDTO signIn(SignInRequestDTO dto, HttpServletResponse response) {
        var usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var authentication = authenticationManager.authenticate(usernamePasswordAuthentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var refreshToken = refreshTokenService.generateRefreshTokenFromUserByUsername(userDetails.getUsername());
        var newRefreshTokenCookie = cookieService.createCookie(
                "refreshToken",
                refreshToken.getToken(),
                "/v1/auth/refresh-token",
                604800,
                true,
                true
        );
        response.addCookie(newRefreshTokenCookie);
        return buildAuthResponseDto(userDetails);
    }

    private AuthResponseDTO buildAuthResponseDto(UserDetailsImpl userDetails) {
        return new AuthResponseDTO(
                TokenType.Bearer.name(),
                jwtService.generateJwtToken(userDetails.getUsername()),
                jwtService.getExpirationJwtToken(),
                userDetails.getUser().getFirstName(),
                userDetails.getUsername(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }

    public AuthResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response) {
        var token = cookieService.extractCookieValueFromRequestByCookieName(request, "refreshToken");
        var refreshTokenEntity = refreshTokenService.validateTokenAndReturnRefreshToken(token);
        var userDetails = new UserDetailsImpl(refreshTokenEntity.getUser());
        setNewAuthenticationInContextFromUserDetails(userDetails);
        refreshTokenEntity = refreshTokenService.generateRefreshTokenFromUserByUsername(userDetails.getUsername());
        var newRefreshTokenCookie = cookieService.createCookie(
                "refreshToken",
                refreshTokenEntity.getToken(),
                "/v1/auth/refresh-token",
                604800,
                true,
                true
        );
        response.addCookie(newRefreshTokenCookie);
        return buildAuthResponseDto(userDetails);
    }

    private static void setNewAuthenticationInContextFromUserDetails(UserDetailsImpl userDetails) {
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    public void signUp(SignUpRequestDTO dto) {
        var user = userRepository.findByUsername(dto.email());
        if (user.isPresent()) {
            log.error("[AuthService:signUp]Exception while registering the user due to : user already exists");
            throw new UserAlreadyExistsException("user already exists");
        }
        var newUserCustomer = newUserCustomerFromSignUpRequestDto(dto);
        userRepository.save(newUserCustomer);
    }

    public User newUserCustomerFromSignUpRequestDto(SignUpRequestDTO dto) {
        var role = roleRepository.findByName("ROLE_CUSTOMER").orElseThrow(() -> new ResourceNotFoundException("role not found"));
        Customer user = new Customer();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setUsername(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.getRoles().add(role);
        return user;
    }


}
