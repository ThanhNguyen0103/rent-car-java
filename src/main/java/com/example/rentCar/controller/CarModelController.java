package com.example.rentCar.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rentCar.domain.CarModel;
import com.example.rentCar.domain.CarModel;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.service.CarModelService;
import com.example.rentCar.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class CarModelController {
    private final CarModelService carModelService;

    public CarModelController(CarModelService carModelService) {
        this.carModelService = carModelService;
    }

    @PostMapping("/car-models")
    @ApiMessage("Create carModel success")
    public ResponseEntity<CarModel> createCarModel(@RequestBody CarModel carModel) {
        CarModel res = this.carModelService.handleCreateCarModel(carModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.carModelService.handleSaveCarModel(res));
    }

    @PutMapping("/car-models")
    @ApiMessage("Update carModel success")
    public ResponseEntity<CarModel> updateCarModel(@RequestBody CarModel carModel) {
        return ResponseEntity.ok().body(this.carModelService.handleUpdateCarModel(carModel));
    }

    @DeleteMapping("/car-models/{id}")
    @ApiMessage("Delete carModel success")
    public ResponseEntity<Void> deleteCarModel(@PathVariable("id") long id) {
        this.carModelService.handleDeleteCarModel(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/car-models/{id}")
    @ApiMessage("Get carModel success")
    public ResponseEntity<CarModel> getCarModelById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.carModelService.getCarModelById(id));
    }

    @GetMapping("/car-models")
    @ApiMessage("Get car-models with pagination success")
    public ResponseEntity<ResultPaginationDTO> getAllCarModels(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.carModelService.getCarModelWithPagination(pageable));
    }
}
