package com.example.rentCar.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.rentCar.domain.res.ResultPaginationDTO;

import com.example.rentCar.domain.Car;

import com.example.rentCar.domain.CarModel;
import com.example.rentCar.domain.dto.CarCriteriaDTO;
import com.example.rentCar.repository.CarRepository;
import com.example.rentCar.utils.error.InvalidException;
import com.example.rentCar.utils.specification.CarSpecs;

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
        res.setAvailable(car.isAvailable());
        res.setCapacity(car.getCapacity());
        res.setCarModel(carModel);
        res.setDescription(car.getDescription());
        res.setPrice(car.getPrice());
        res.setFuelType(car.getFuelType());
        res.setYear(car.getYear());
        res.setMileage(car.getMileage());
        res.setLocation(car.getLocation());
        //
        // res.getCarImages()

        return this.carRepository.save(res);
    }

    public Car handleSaveCar(Car car) {
        return this.carRepository.save(car);
    }

    public Car handleUpdateCar(Car car) {

        Car existing = carRepository.findById(car.getId())
                .orElseThrow(() -> new InvalidException("Xe không tồn tại"));

        if (car.getCarModel() == null)
            throw new InvalidException("Mẫu xe không được để trống");
        CarModel carModel = this.carModelService.getCarModelById(car.getCarModel().getId());

        existing.setCarModel(carModel);
        existing.setCapacity(car.getCapacity());
        existing.setDescription(car.getDescription());
        existing.setAvailable(car.isAvailable());
        existing.setPrice(car.getPrice());
        existing.setFuelType(car.getFuelType());
        existing.setYear(car.getYear());
        existing.setMileage(car.getMileage());
        existing.setLocation(car.getLocation());

        return this.carRepository.save(existing);
    }

    public void deleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Xe không tồn tại"));

        this.carRepository.delete(car);
    }

    public Car getCarById(Long id) {
        return this.carRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Xe không tồn tại"));
    }

    public ResultPaginationDTO getCarWithPagination(Pageable pageable) {

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

    public ResultPaginationDTO GetCarWithSpecs(CarCriteriaDTO car, Pageable pageable) {

        Specification<Car> specs = Specification.where(CarSpecs.keywordSearch(car.getKeyword()))
                .or(CarSpecs.hasBrand(car.getBrand()))
                .or(CarSpecs.hasCarModel(car.getCarModel()));
        // .or(CarSpecs.priceGreaterThanOrEqual(car.getMinPrice()))
        // .or(CarSpecs.priceLessThanOrEqual(car.getMaxPrice()));
        if (car.getPrice() != null) {
            Specification<Car> priceSpecs = null;
            for (String price : car.getPrice()) {
                double min = 0;
                double max = 0;
                switch (price) {
                    case "0-45":
                        min = 0;
                        max = 45;
                        break;
                    case "46-65":
                        min = 46;
                        max = 65;
                        break;
                    case "66-110":
                        min = 66;
                        max = 110;
                        break;
                    case "111-999":
                        min = 111;
                        max = 999;
                        break;
                    default:
                        break;
                }
                Specification<Car> priceSpec = CarSpecs.priceBetween(min, max);
                priceSpecs = (priceSpecs == null) ? priceSpec : priceSpecs.or(priceSpec);

            }
            if (priceSpecs != null) {
                specs = specs.and(priceSpecs); // dùng and để filter chặt chẽ
            }

        }

        Page<Car> pages = this.carRepository.findAll(specs, pageable);
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