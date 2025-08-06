package com.example.rentCar.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rentCar.domain.Brand;
import com.example.rentCar.domain.CarModel;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.repository.CarModelRepository;
import com.example.rentCar.utils.error.InvalidException;

@Service
public class CarModelService {
    private final CarModelRepository carModelRepository;
    private final BrandService brandService;

    public CarModelService(CarModelRepository carModelRepository, BrandService brandService) {
        this.carModelRepository = carModelRepository;
        this.brandService = brandService;
    }

    public CarModel handleCreateCarModel(CarModel carModel) {
        CarModel res = new CarModel();
        boolean existsByName = this.carModelRepository.existsByName(carModel.getName());
        if (existsByName) {
            throw new InvalidException("CarModel name đã tồn tại ");
        }
        Brand brand = this.brandService.getBrandById(carModel.getBrand().getId());
        if (brand != null) {
            res.setBrand(brand);
        }
        res.setName(carModel.getName());
        return res;
    }

    public CarModel handleSaveCarModel(CarModel carModel) {
        return this.carModelRepository.save(carModel);
    }

    public CarModel getCarModelById(long id) {
        Optional<CarModel> rOptional = this.carModelRepository.findById(id);
        if (rOptional.isPresent()) {
            CarModel carModel = rOptional.get();
            return carModel;
        } else
            throw new InvalidException("CarModel không tồn tại");

    }

    public CarModel handleUpdateCarModel(CarModel carModel) {

        Optional<CarModel> cOptional = this.carModelRepository.findById(carModel.getId());
        if (cOptional.isEmpty()) {
            throw new InvalidException("CarModel không tồn tại");
        }
        CarModel currentCarModel = cOptional.get();
        boolean existsName = this.carModelRepository.existsByName(carModel.getName());
        if (existsName && !currentCarModel.getName().equals(carModel.getName())) {
            throw new InvalidException("CarModel name đã tồn tại");
        }
        if (carModel.getBrand() == null) {
            throw new InvalidException("Brand không được để trống");
        }
        Brand brand = this.brandService.getBrandById(carModel.getBrand().getId());
        if (brand != null) {
            currentCarModel.setBrand(brand);
        }

        currentCarModel.setName(carModel.getName());
        return this.carModelRepository.save(currentCarModel);
    }

    public void handleDeleteCarModel(long id) {
        Optional<CarModel> cOptional = this.carModelRepository.findById(id);
        if (cOptional.isPresent()) {
            CarModel res = cOptional.get();
            this.carModelRepository.delete(res);
        } else {
            throw new InvalidException("CarModel không tồn tại..");
        }
    }

    public ResultPaginationDTO getCarModelWithPagination(Pageable pageable) {

        Page<CarModel> pages = this.carModelRepository.findAll(pageable);

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
