package com.shopsphere.service;

import com.shopsphere.dto.request.RegisterUserRequest;
import com.shopsphere.dto.request.UpdateUserRequest;
import com.shopsphere.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {

    UserResponse registerUser(RegisterUserRequest request);

    UserResponse getUserById(Long userId);

    Page<UserResponse> getAllUsers(int page, int size);

    UserResponse updateUserById(Long userId, UpdateUserRequest updateUserRequest, String authenticatedEmail);

    void deactivateUser(Long userId, String authenticatedEmail);

    void deleteUser(Long UserId);

    UserResponse getCurrentUser(String email);
}
