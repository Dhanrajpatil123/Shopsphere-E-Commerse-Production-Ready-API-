package com.shopsphere.serviceImplementation;

import com.shopsphere.dto.request.RegisterUserRequest;
import com.shopsphere.dto.response.UserResponse;
import com.shopsphere.exception.DuplicateResourceException;
import com.shopsphere.exception.ResourceNotFoundException;
import com.shopsphere.model.Role;
import com.shopsphere.model.User;
import com.shopsphere.repository.RoleRepository;
import com.shopsphere.repository.UserRepository;
import com.shopsphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Override
    public UserResponse registerUser(RegisterUserRequest request) {

        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already register");
        }

        Role customerRole = this.roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() ->  new ResourceNotFoundException("Customer Role Not Found"));


        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .active(true)
                .role(customerRole)
                .build();

        User savedUser = this.userRepository.save(user);

        return mapToResponse(savedUser);
    }


    @Override
    public UserResponse getUserById(Long userId) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return mapToResponse(user);
    }



    @Override
    public UserResponse updateUserById(Long userId, RegisterUserRequest request) {

        return null;
    }



    @Override
    public Page<UserResponse> getAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(1, 10);

        Page<User> users = this.userRepository.findAll(pageable);

        return users.map(this::mapToResponse);
    }


    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .active(user.isActive())
                .build();
    }

}
