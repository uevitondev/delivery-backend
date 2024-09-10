package com.uevitondev.deliverybackend.domain.authentication;

import com.uevitondev.deliverybackend.config.security.jwt.JwtService;
import com.uevitondev.deliverybackend.config.security.jwt.TokenType;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.exception.InvalidTokenVerificationException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.exception.UserAlreadyExistsException;
import com.uevitondev.deliverybackend.domain.passwordresettoken.PasswordResetTokenDTO;
import com.uevitondev.deliverybackend.domain.passwordresettoken.PasswordResetTokenService;
import com.uevitondev.deliverybackend.domain.refreshtoken.RefreshTokenService;
import com.uevitondev.deliverybackend.domain.role.RoleRepository;
import com.uevitondev.deliverybackend.domain.tokenverification.TokenRequestDTO;
import com.uevitondev.deliverybackend.domain.tokenverification.TokenVerification;
import com.uevitondev.deliverybackend.domain.tokenverification.TokenVerificationService;
import com.uevitondev.deliverybackend.domain.user.User;
import com.uevitondev.deliverybackend.domain.user.UserDetailsImpl;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import com.uevitondev.deliverybackend.domain.utils.CookieService;
import com.uevitondev.deliverybackend.domain.utils.MailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final RefreshTokenService refreshTokenService;
    private final TokenVerificationService tokenVerificationService;
    private final CookieService cookieService;
    private final MailService mailService;


    public AuthenticationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService, PasswordResetTokenService passwordResetTokenService,
            RefreshTokenService refreshTokenService,
            TokenVerificationService tokenVerificationService,
            CookieService cookieService,
            MailService mailService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.refreshTokenService = refreshTokenService;
        this.tokenVerificationService = tokenVerificationService;
        this.cookieService = cookieService;
        this.mailService = mailService;
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

    @Transactional
    public void resetPassword(String email) {
        var user = userRepository.findByUsername(email).orElseThrow(
                () -> new ResourceNotFoundException("user not found")
        );
        var passwordResetToken = passwordResetTokenService.findPasswordResetTokenByUser(user.getId());
        passwordResetToken = passwordResetTokenService.updatePasswordResetToken(passwordResetToken);

        var emailDto = new MailService.EmailDTO(
                user.getUsername(),
                user.getFirstName(),
                "Password Reset Token",
                passwordResetToken.getToken(),
                "password-reset-token-email.html"
        );
        mailService.sendEmail(emailDto);
    }

    @Transactional
    public void changePassword(PasswordResetTokenDTO dto) {
        var passwordResetToken = passwordResetTokenService.findPasswordResetTokenByToken(dto.token());
        if (passwordResetToken.isExpired()) {
            throw new InvalidTokenVerificationException("password reset token is expired");
        }
        var user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
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
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Transactional
    public void signUp(SignUpRequestDTO dto) {
        var userExists = userRepository.findByUsername(dto.email());
        if (userExists.isPresent()) {
            LOGGER.error("exception while registering user: user already exists");
            throw new UserAlreadyExistsException("user already exists");
        }
        var newUser = registerNewUserFromSignUpRequestDto(dto);
        var token = tokenVerificationService.generateTokenVerificationByUser(newUser).getToken();
        var emailDto = new MailService.EmailDTO(
                newUser.getUsername(),
                newUser.getFirstName(),
                "Email de Verificação",
                token,
                "token-verification-email.html"
        );
        mailService.sendEmail(emailDto);

    }

    public User registerNewUserFromSignUpRequestDto(SignUpRequestDTO dto) {
        var role = roleRepository.findByName("ROLE_CUSTOMER").orElseThrow(
                () -> new ResourceNotFoundException("role not found")
        );
        var user = new Customer(
                null,
                dto.firstName(),
                dto.lastName(),
                dto.email(),
                passwordEncoder.encode(dto.password())
        );
        user.getRoles().add(role);
        user = userRepository.save(user);
        passwordResetTokenService.createNewPasswordResetTokenForUser(user);
        LOGGER.info("User Successfully SignUp");
        return user;
    }


    @Transactional
    public void signUpVerification(TokenRequestDTO dto) {
        var tokenVerification = tokenVerificationService.validateUserTokenVerificationByToken(dto.token());
        enableUserByTokenVerification(tokenVerification);
    }


    public void enableUserByTokenVerification(TokenVerification tokenVerification) {
        var user = tokenVerification.getUser();
        user.isEnabled(true);
        user = userRepository.save(user);
        LOGGER.error("user enabled by token verification");
        LOGGER.error("user is enabled: {}", user.isEnabled());

    }


}
