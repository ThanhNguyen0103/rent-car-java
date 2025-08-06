package com.example.rentCar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rentCar.domain.CarImage;

@Repository
public interface CarImageRepository extends JpaRepository<CarImage, Long> {

}
