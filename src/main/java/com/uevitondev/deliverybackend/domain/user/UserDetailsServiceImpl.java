package com.uevitondev.deliverybackend.domain.user;


import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .filter(user -> {
                    if (Boolean.FALSE.equals(user.isEnabled())) {
                        throw new DisabledException("user is disabled");
                    }
                    return user.isEnabled();
                })
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found for username: " + username));
    }

}