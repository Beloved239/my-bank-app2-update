package com.Springboot.mybankapp2.repository;

import com.Springboot.mybankapp2.Entity.User;
import com.Springboot.mybankapp2.dto.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
}
