package com.example.rentCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rentCar.domain.Role;
import com.example.rentCar.utils.constant.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(RoleEnum name);

    Role findByName(RoleEnum name);
}
