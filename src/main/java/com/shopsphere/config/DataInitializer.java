package com.shopsphere.config;

import com.shopsphere.model.Role;
import com.shopsphere.repository.RoleRepository;
import com.shopsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {


    private final RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {

        if (this.roleRepository.findByName("ROLE_ADMIN").isEmpty()){

            this.roleRepository.save(
                    Role.builder()
                            .name("ROLE_ADMIN")
                            .build()
            );
        }


        if (this.roleRepository.findByName("ROLE_CUSTOMER").isEmpty()){

            this.roleRepository.save(
                    Role.builder()
                            .name("ROLE_CUSTOMER")
                            .build()
            );
        }
    }
}
