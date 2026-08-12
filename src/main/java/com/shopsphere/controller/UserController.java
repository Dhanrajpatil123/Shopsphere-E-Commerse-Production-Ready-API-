package com.shopsphere.controller;

import com.shopsphere.dto.request.RegisterUserRequest;
import com.shopsphere.dto.request.UpdateUserRequest;
import com.shopsphere.dto.response.UserResponse;
import com.shopsphere.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request){

        UserResponse response = this.userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){

        UserResponse response = this.userService.getUserById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size){

        Page<UserResponse> responses = this.userService.getAllUsers(page, size);

        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable Long userId, @Valid @RequestBody UpdateUserRequest request, Authentication authentication){

        String authenticatedEmail = authentication.getName();

        UserResponse response = this.userService.updateUserById(userId, request, authenticatedEmail);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId, Authentication authentication){

        String authenticationEmail = authentication.getName();

        this.userService.deactivateUser(userId, authenticationEmail);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId){
        this.userService.deleteUser(userId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }



    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication){

        String email = authentication.getName();

        UserResponse userResponse = this.userService.getCurrentUser(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponse);
    }


}
