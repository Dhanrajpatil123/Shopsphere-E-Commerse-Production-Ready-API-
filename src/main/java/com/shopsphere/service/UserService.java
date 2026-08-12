package com.shopsphere.service;

import com.shopsphere.dto.request.RegisterUserRequest;
import com.shopsphere.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {

    UserResponse registerUser(RegisterUserRequest request);

    UserResponse getUserById(Long userId);

    UserResponse updateUserById(Long userId, RegisterUserRequest request);

    Page<UserResponse> getAllUsers(int page, int size);
}
