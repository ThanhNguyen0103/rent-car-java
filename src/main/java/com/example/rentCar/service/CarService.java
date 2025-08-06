package com.example.rentCar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rentCar.domain.Role;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.domain.Brand;
import com.example.rentCar.domain.Car;
import com.example.rentCar.domain.CarModel;
import com.example.rentCar.repository.CarRepository;
import com.example.rentCar.utils.error.InvalidException;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final CarModelService carModelService;

    public CarService(CarRepository carRepository, CarModelService carModelService) {
        this.carRepository = carRepository;
        this.carModelService = carModelService;
    }

    public Car handleCreateCar(Car car) {
        if (car.getCarModel() == null)
            throw new InvalidException("CarModel không được để trống");
        CarModel carModel = this.carModelService.getCarModelById(car.getCarModel().getId());
        Car res = new Car();
        res.setAvailable(true);
        res.setCapacity("16");
        res.setCarModel(carModel);
        res.setDescription(car.getDescription());
        res.setPrice(car.getPrice());
        //
        // res.getCarImages()

        return this.carRepository.save(res);
    }

    public Car handleSaveBrand(Car car) {
        return this.carRepository.save(car);
    }

    public Car handleUpdateCar(Car car) {
        Car existing = carRepository.findById(car.getId())
                .orElseThrow(() -> new InvalidException("Car không tồn tại"));

        if (car.getCarModel() == null)
            throw new InvalidException("CarModel không được để trống");
        CarModel carModel = this.carModelService.getCarModelById(car.getCarModel().getId());

        existing.setCarModel(carModel);
        existing.setCapacity(car.getCapacity());
        existing.setDescription(car.getDescription());
        existing.setAvailable(car.isAvailable());
        existing.setPrice(car.getPrice());

        return carRepository.save(existing);
    }

    public void deleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Car không tồn tại"));
        this.carRepository.delete(car);
    }

    public Car getCarById(Long id) {
        return this.carRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Car không tồn tại"));
    }

    public ResultPaginationDTO getBrandWithPagination(Pageable pageable) {

        Page<Car> pages = this.carRepository.findAll(pageable);

        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setCurrentPage(pages.getNumber() + 1);
        meta.setPageSize(pages.getSize());
        meta.setPages(pages.getTotalPages());
        meta.setTotal(pages.getTotalElements());

        result.setResult(pages.getContent());
        result.setMeta(meta);
        return result;
    }

}