package com.shopsphere.repository;

import com.shopsphere.dto.response.UserResponse;
import com.shopsphere.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<UserResponse> getAllUsers(int page, int size);

}
