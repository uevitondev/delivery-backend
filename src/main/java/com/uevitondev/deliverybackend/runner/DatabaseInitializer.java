package com.uevitondev.deliverybackend.runner;


import com.uevitondev.deliverybackend.domain.role.Role;
import com.uevitondev.deliverybackend.domain.user.User;
import com.uevitondev.deliverybackend.domain.role.RoleRepository;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public DatabaseInitializer(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Role adminRole = new Role("ROLE_ADMIN");
        Role clientRole = new Role("ROLE_CLIENT");
        Role sellerRole = new Role("ROLE_SELLER");

        roleRepository.saveAll(List.of(adminRole, clientRole, sellerRole));

        User adminUser = new User();
        adminUser.setId(UUID.randomUUID());
        adminUser.setFirstName("Admin");
        adminUser.setLastName("UserAdmin");
        adminUser.setUsername("admin@gmail.com");
        adminUser.setPassword("$2a$10$Y7fk59/1Pg.ig0Goy0yTS.5RgKD18N5J3MYCo5bPYzVpslJqfr4uu");
        adminUser.getRoles().add(adminRole);
        userRepository.save(adminUser);

        User clientUser = new User();
        clientUser.setId(UUID.randomUUID());
        clientUser.setFirstName("Client");
        clientUser.setLastName("UserClient");
        clientUser.setUsername("client@gmail.com");
        clientUser.setPassword("$2a$10$Y7fk59/1Pg.ig0Goy0yTS.5RgKD18N5J3MYCo5bPYzVpslJqfr4uu");
        clientUser.getRoles().add(clientRole);
        userRepository.save(clientUser);

        User sellerUser = new User();
        sellerUser.setId(UUID.randomUUID());
        sellerUser.setFirstName("Seller");
        sellerUser.setLastName("UserSeller");
        sellerUser.setUsername("seller@gmail.com");
        sellerUser.setPassword("$2a$10$Y7fk59/1Pg.ig0Goy0yTS.5RgKD18N5J3MYCo5bPYzVpslJqfr4uu");
        sellerUser.getRoles().add(sellerRole);
        userRepository.save(sellerUser);

    }
}
