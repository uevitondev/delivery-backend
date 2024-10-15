package com.uevitondev.deliverybackend.domain.user;

import com.uevitondev.deliverybackend.config.security.CustomUserDetails;
import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.exception.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void existsUserByEmail(String email) {
        var optionalUserExists =  userRepository.findByEmail(email);
        if (optionalUserExists.isPresent()) {
            LOGGER.error("user already exists");
            throw new UserAlreadyExistsException("user already exists");
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("user not found")
        );
    }



    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user not found")
        );
    }

    public UserProfileDTO getUserProfile() {
        return new UserProfileDTO(getUserAuthenticated());
    }


    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDTO::new).toList();
    }

    public UserResponseDTO findUserById(UUID userId) {
        return new UserResponseDTO(getUserById(userId));
    }

    @Transactional
    public UserResponseDTO insertNewUser(UserRequestDTO dto) {
        var user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setPhoneNumber(dto.phoneNumber());
        return new UserResponseDTO(userRepository.save(user));
    }


    @Transactional
    public UserResponseDTO updateUserById(UUID userId, UserRequestDTO dto) {
        var user = getUserById(userId);
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setPhoneNumber(dto.phoneNumber());
        user.setUpdatedAt(LocalDateTime.now());
        return new UserResponseDTO(userRepository.save(user));
    }

    @Transactional
    public void deleteUserById(UUID userId) {
        try {
            userRepository.deleteById(getUserById(userId).getId());
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("integrity constraint violation");
        }
    }

    public User getUserAuthenticated() {
        var userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails != null) {
            return findUserByEmail(userDetails.getUsername());
        }
        throw new AccessDeniedException("Access denied");
    }

}
