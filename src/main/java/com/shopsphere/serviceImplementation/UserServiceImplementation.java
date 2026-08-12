package com.shopsphere.serviceImplementation;

import com.shopsphere.dto.request.RegisterUserRequest;
import com.shopsphere.dto.request.UpdateUserRequest;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
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



    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Page<UserResponse> getAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<User> users = this.userRepository.findAll(pageable);

        return users.map(this::mapToResponse);
    }


    @Override
    public UserResponse updateUserById(Long userId, UpdateUserRequest updateUserRequest, String authenticatedEmail) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + userId));


        // ------------- user update its own data -------------


        // check admin or not
        boolean isAdmin = this.userRepository.findByEmail(authenticatedEmail)
                .map(userFromDataBase -> userFromDataBase.getRole().getName().equals("ROLE_ADMIN"))
                .orElse(false);


        if (!isAdmin && !user.getEmail().equalsIgnoreCase(authenticatedEmail)) {
            throw new AccessDeniedException("You are not allowed to update this user - Access Denied");
        }

        if (!user.getEmail().equalsIgnoreCase(updateUserRequest.getEmail())
                    && this.userRepository.existsByEmail(updateUserRequest.getEmail())){

            throw new DuplicateResourceException("Email is already register");
        }

        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());
        user.setEmail(updateUserRequest.getEmail());
        user.setPhone(updateUserRequest.getPhone());

        User updateUser = this.userRepository.save(user);

        return mapToResponse(updateUser);
    }

    @Override
    public void deactivateUser(Long userId, String authenticatedEmail) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with : " + userId));


        User authenticateUser = this.userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new ResourceNotFoundException( "Authenticated user not found"));

        boolean isAdmin = authenticateUser.getRole().getName().equals("ROLE_ADMIN");


        if(!isAdmin && !user.getEmail().equalsIgnoreCase(authenticatedEmail)){

            throw new AccessDeniedException("You are not allowed to deactivate this user");
        }


        if (!user.isActive()){
            throw new IllegalStateException("User is already deactivated");
        }

        user.setActive(false);
        this.userRepository.save(user);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUser(Long userId) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Id : " + userId));

        this.userRepository.delete(user);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserResponse getCurrentUser(String email) {

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return mapToResponse(user);
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
