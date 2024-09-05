package com.uevitondev.deliverybackend.domain.user;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
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
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        user.setPhoneNumber(dto.phoneNumber());
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        return new UserResponseDTO(userRepository.save(user));
    }


    @Transactional
    public UserResponseDTO updateUserById(UUID userId, UserRequestDTO dto) {
        var user = getUserById(userId);
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhoneNumber(dto.phoneNumber());
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
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

    public static User getUserAuthenticated() {
        var userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetailsImpl.getUser() != null) {
            return userDetailsImpl.getUser();
        }
        throw new AccessDeniedException("Access denied");
    }

}
