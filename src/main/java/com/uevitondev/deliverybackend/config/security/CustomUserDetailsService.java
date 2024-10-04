package com.uevitondev.deliverybackend.config.security;


import com.uevitondev.deliverybackend.domain.user.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .filter(user -> {
                    if (Boolean.FALSE.equals(user.isEnabled())) {
                        throw new DisabledException("user is disabled");
                    }
                    return user.isEnabled();
                })
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found for email"));
    }

}