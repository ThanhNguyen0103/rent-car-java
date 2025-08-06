package com.example.rentCar.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.rentCar.domain.Car;
import com.example.rentCar.domain.CarImage;
import com.example.rentCar.service.CarImageService;
import com.example.rentCar.service.CarService;
import com.example.rentCar.utils.annotation.ApiMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1")
public class CarController {
    private final CarService carService;
    private final CarImageService carImageService;

    public CarController(CarService carService, CarImageService carImageService) {
        this.carService = carService;
        this.carImageService = carImageService;
    }

    @PostMapping("/cars")
    @ApiMessage("Create cars success")
    public ResponseEntity<Car> createBrand(@RequestParam("car") String carJson,
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("folder") String folder) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Car car = mapper.readValue(carJson, Car.class);

        Car res = this.carService.handleCreateCar(car);
        List<CarImage> carImages = files.stream()
                .map(file -> {
                    try {
                        return this.carImageService.uploadCarImage(res.getId(), folder, file);
                    } catch (URISyntaxException | IOException e) {
                        throw new RuntimeException("Upload failed for file: " + file.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());
        res.setCarImages(carImages);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

}
