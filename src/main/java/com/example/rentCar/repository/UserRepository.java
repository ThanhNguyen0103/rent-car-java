package com.example.rentCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rentCar.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
