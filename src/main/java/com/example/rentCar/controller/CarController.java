package com.example.rentCar.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.rentCar.domain.Car;
import com.example.rentCar.domain.CarImage;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.service.CarImageService;
import com.example.rentCar.service.CarService;
import com.example.rentCar.utils.annotation.ApiMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public ResponseEntity<Car> createCar(@RequestParam("car") String carJson,
            @RequestParam(required = false, name = "files") List<MultipartFile> files,
            @RequestParam("folder") String folder) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Car car = mapper.readValue(carJson, Car.class);

        Car res = this.carService.handleCreateCar(car);
        List<CarImage> carImages = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            carImages = files.stream()
                    .map(file -> {
                        try {
                            return this.carImageService.uploadCarImage(res.getId(), folder, file);
                        } catch (URISyntaxException | IOException e) {
                            throw new RuntimeException("Upload failed for file: " + file.getOriginalFilename(), e);
                        }
                    })
                    .collect(Collectors.toList());
        }

        res.setCarImages(carImages);
        this.carService.handleSaveCar(res);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/cars")
    @ApiMessage("Update cars success")
    public ResponseEntity<Car> updateCar(@RequestParam("car") String carJson,
            @RequestParam(required = false, name = "files") List<MultipartFile> files,
            @RequestParam("folder") String folder) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Car car = mapper.readValue(carJson, Car.class);

        Car res = this.carService.handleUpdateCar(car);
        List<CarImage> carImages = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            this.carImageService.deleteAllCarImg(res.getId());
            carImages = files.stream()
                    .map(file -> {
                        try {
                            return this.carImageService.uploadCarImage(res.getId(), folder, file);
                        } catch (URISyntaxException | IOException e) {
                            throw new RuntimeException("Upload failed for file: " + file.getOriginalFilename(), e);
                        }
                    })
                    .collect(Collectors.toList());
        }

        res.setCarImages(carImages);
        this.carService.handleSaveCar(res);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/cars/{id}")
    @ApiMessage("Delete car success")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") long id) {
        this.carService.deleteCar(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/cars/{id}")
    @ApiMessage("Get car success")
    public ResponseEntity<Car> getCarById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.carService.getCarById(id));
    }

    @GetMapping("/cars")
    @ApiMessage("Get cars with pagination success")
    public ResponseEntity<ResultPaginationDTO> getAllCar(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.carService.getCarWithPagination(pageable));
    }

}
