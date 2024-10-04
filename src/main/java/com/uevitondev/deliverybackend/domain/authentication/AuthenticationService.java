package com.uevitondev.deliverybackend.domain.authentication;

import com.uevitondev.deliverybackend.config.security.CustomUserDetails;
import com.uevitondev.deliverybackend.config.security.jwt.JwtService;
import com.uevitondev.deliverybackend.config.security.jwt.TokenType;
import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.exception.UserAlreadyExistsException;
import com.uevitondev.deliverybackend.domain.passwordresettoken.PasswordResetToken;
import com.uevitondev.deliverybackend.domain.passwordresettoken.PasswordResetTokenDTO;
import com.uevitondev.deliverybackend.domain.passwordresettoken.PasswordResetTokenService;
import com.uevitondev.deliverybackend.domain.refreshtoken.RefreshTokenService;
import com.uevitondev.deliverybackend.domain.role.RoleRepository;
import com.uevitondev.deliverybackend.domain.tokenverification.TokenRequestDTO;
import com.uevitondev.deliverybackend.domain.tokenverification.TokenVerification;
import com.uevitondev.deliverybackend.domain.tokenverification.TokenVerificationService;
import com.uevitondev.deliverybackend.domain.user.User;
import com.uevitondev.deliverybackend.domain.user.UserService;
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

    private final UserService userService;
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
            UserService userService,
            RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService, PasswordResetTokenService passwordResetTokenService,
            RefreshTokenService refreshTokenService,
            TokenVerificationService tokenVerificationService,
            CookieService cookieService,
            MailService mailService
    ) {
        this.userService = userService;
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

    @Transactional
    public AuthResponseDTO signIn(SignInRequestDTO dto, HttpServletResponse response) {
        var usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var authentication = authenticationManager.authenticate(usernamePasswordAuthentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var userDetails = (CustomUserDetails) authentication.getPrincipal();
        var user = userService.findUserByEmail(userDetails.getUsername());
        var refreshToken = refreshTokenService.updateRefreshTokenByUser(user);
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

    private AuthResponseDTO buildAuthResponseDto(CustomUserDetails userDetails) {
        return new AuthResponseDTO(
                userDetails.getName(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                TokenType.Bearer.name(),
                jwtService.generateJwtToken(userDetails.getUsername()),
                jwtService.getExpirationJwtToken()
        );
    }

    @Transactional
    public void resetPassword(String email) {
        var user = userService.findUserByEmail(email);
        var passwordResetToken = passwordResetTokenService.findPasswordResetTokenByUser(user.getId());
        passwordResetToken = passwordResetTokenService.updatePasswordResetToken(passwordResetToken);
        sendEmailForPassswordResetTokenUser(user, passwordResetToken);
    }

    @Transactional
    public void changePassword(PasswordResetTokenDTO dto) {
        var passwordResetToken = passwordResetTokenService.validateAndConfirmPasswordResetTokenByToken(dto.token());
        changeUserPasswordByVerification(passwordResetToken, dto);
    }

    public void changeUserPasswordByVerification(PasswordResetToken passwordResetToken, PasswordResetTokenDTO dto) {
        var user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userService.saveUser(user);
        LOGGER.error("user password changed success by password reset token");
    }


    public AuthResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response) {
        var token = cookieService.extractCookieValueFromRequestByCookieName(request, "refreshToken");
        var validRefreshToken = refreshTokenService.validateAndConfirmRefreshTokenByToken(token);
        var user = validRefreshToken.getUser();
        setNewAuthenticationInContextFromUserDetails(new CustomUserDetails(user));
        var refreshTokenUpdated = refreshTokenService.updateRefreshTokenByUser(user);
        var newRefreshTokenCookie = cookieService.createCookie(
                "refreshToken",
                refreshTokenUpdated.getToken(),
                "/v1/auth/refresh-token",
                604800,
                true,
                true
        );
        response.addCookie(newRefreshTokenCookie);
        return buildAuthResponseDto(new CustomUserDetails(user));
    }

    private static void setNewAuthenticationInContextFromUserDetails(CustomUserDetails userDetails) {
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Transactional
    public void signUp(SignUpRequestDTO dto) {
        userService.existsUserByEmail(dto.email());
        var newUser = registerNewUserFromSignUpRequestDto(dto);
        refreshTokenService.generateRefreshTokenFromUser(newUser);
        var tokenVerification = tokenVerificationService.generateTokenVerificationByUser(newUser);
        sendEmailForTokenVerificationUser(newUser, tokenVerification);

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
        user = (Customer) userService.saveUser(user);
        passwordResetTokenService.createNewPasswordResetTokenForUser(user);
        LOGGER.info("User Successfully SignUp");
        return user;
    }

    @Transactional
    public void verification(TokenRequestDTO dto) {
        var tokenVerification = tokenVerificationService.validateAndConfirmTokenVerificationByToken(dto.token());
        enableUserByTokenVerification(tokenVerification);
    }

    @Transactional
    public void verificationNewToken(String email) {
        var user = userService.findUserByEmail(email);
        var tokenVerification = tokenVerificationService.updateTokenVerificationByUser(user);
        sendEmailForTokenVerificationUser(user, tokenVerification);
    }

    public void enableUserByTokenVerification(TokenVerification tokenVerification) {
        var user = tokenVerification.getUser();
        user.isEnabled(true);
        userService.saveUser(user);
        LOGGER.error("user enabled by token verification");
    }

    public void sendEmailForPassswordResetTokenUser(User user, PasswordResetToken passwordResetToken) {
        var emailDto = new MailService.EmailDTO(
                user.getEmail(),
                user.getFirstName(),
                "Redefinição De Senha",
                passwordResetToken.getToken(),
                "password-reset-token-email.html"
        );
        mailService.sendEmail(emailDto);
    }

    public void sendEmailForTokenVerificationUser(User user, TokenVerification tokenVerification) {
        var emailDto = new MailService.EmailDTO(
                user.getEmail(),
                user.getFirstName(),
                "Verificação De Conta",
                tokenVerification.getToken(),
                "token-verification-email.html"
        );
        mailService.sendEmail(emailDto);
    }


}
