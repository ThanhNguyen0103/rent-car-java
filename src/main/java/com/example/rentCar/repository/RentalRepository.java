package com.example.rentCar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rentCar.domain.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    Page<Rental> findAllByUserId(long userId, Pageable pageable);
}
