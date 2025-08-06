package com.example.rentCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rentCar.domain.CarModel;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    boolean existsByName(String name);
}
