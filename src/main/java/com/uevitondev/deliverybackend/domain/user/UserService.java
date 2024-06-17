package com.uevitondev.deliverybackend.domain.user;

import com.uevitondev.deliverybackend.domain.exception.DatabaseException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.role.Role;
import com.uevitondev.deliverybackend.domain.role.RoleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserAccountDTO getUserAccountData() {
        return new UserAccountDTO(getUserAuthenticated());
    }


    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDTO::new).toList();
    }

    public UserResponseDTO findUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found for userId: " + id));
        return new UserResponseDTO(user);
    }

    public UserResponseDTO insertNewUser(UserRequestDTO dto) {
        try {
            User user = new User();
            user.setUsername(dto.getEmail());
            user.setPassword(dto.getPassword());

            for (UUID roleId : dto.getRolesId()) {
                Role role = roleRepository.getReferenceById(roleId);
                user.getRoles().add(role);
            }
            user = userRepository.save(user);
            return new UserResponseDTO(user);
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.toString());
        }
    }

    public UserResponseDTO updateUserById(UUID id, UserRequestDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found for userId: " + id));
        user.setUsername(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.getRoles().clear();
        user.setUpdatedAt(LocalDateTime.now());

        for (UUID roleId : dto.getRolesId()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("role not found for userId: " + roleId));
            user.getRoles().add(role);
        }

        user = userRepository.save(user);
        return new UserResponseDTO(user);
    }

    public void deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("user not found for userId: " + id);
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }

    public static User getUserAuthenticated() {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetailsImpl.getUser() != null) {
            return userDetailsImpl.getUser();
        }
        throw new AccessDeniedException("Access denied");
    }

}
