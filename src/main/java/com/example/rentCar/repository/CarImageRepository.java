package com.example.rentCar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rentCar.domain.CarImage;

@Repository
public interface CarImageRepository extends JpaRepository<CarImage, Long> {
    void deleteAllByCarId(long id);

    List<CarImage> findAllByCarId(long id);
}
