package com.example.rentCar.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.rentCar.service.CarImageService;

@RestController
public class CarImageController {
    private final CarImageService carImageService;

    public CarImageController(CarImageService carImageService) {
        this.carImageService = carImageService;
    }

}
