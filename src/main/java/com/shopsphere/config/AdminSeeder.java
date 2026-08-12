package com.shopsphere.config;

import com.shopsphere.model.Role;
import com.shopsphere.model.User;
import com.shopsphere.repository.RoleRepository;
import com.shopsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {

        String adminEmail = "admin@shopsphere.com";

        if (userRepository.findByEmail(adminEmail).isPresent()){
            return;
        }

        Role adminRole = this.roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN is not found"));


        User user = User.builder()
                .firstName("Shop")
                .lastName("Admin")
                .email(adminEmail)
                .password(this.passwordEncoder.encode("Admin@123"))
                .phone("999999999")
                .active(true)
                .role(adminRole)
                .build();

        this.userRepository.save(user);
    }
}
