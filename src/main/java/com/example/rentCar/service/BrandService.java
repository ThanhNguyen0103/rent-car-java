package com.example.rentCar.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rentCar.domain.Brand;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.repository.BrandRepository;
import com.example.rentCar.utils.error.InvalidException;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand handleCreateBrand(Brand brand) {
        Brand res = new Brand();
        boolean existsByName = this.brandRepository.existsByName(brand.getName());
        if (existsByName) {
            throw new InvalidException("Brand name đã tồn tại ");
        }
        res.setName(brand.getName());
        return res;
    }

    public Brand handleSaveBrand(Brand brand) {
        return this.brandRepository.save(brand);
    }

    public Brand getBrandById(long id) {
        Optional<Brand> rOptional = this.brandRepository.findById(id);
        if (rOptional.isPresent()) {
            Brand brand = rOptional.get();
            return brand;
        }
        return null;
    }

    public Brand handleUpdateBrand(Brand brand) {

        Optional<Brand> brandOptional = this.brandRepository.findById(brand.getId());
        if (brandOptional.isEmpty()) {
            throw new InvalidException("Brand name đã tồn tại");
        }
        Brand currentBrand = brandOptional.get();
        boolean existsName = this.brandRepository.existsByName(brand.getName());
        if (existsName && !currentBrand.getName().equals(brand.getName())) {
            throw new InvalidException("Brand name đã tồn tại");
        }

        currentBrand.setName(brand.getName());
        return this.brandRepository.save(currentBrand);
    }

    public void handleDeleteBrand(long id) {
        Optional<Brand> brandOptional = this.brandRepository.findById(id);
        if (brandOptional.isPresent()) {
            Brand res = brandOptional.get();
            this.brandRepository.delete(res);
        } else {
            throw new InvalidException("Brand không tồn tại..");
        }
    }

    public ResultPaginationDTO getBrandWithPagination(Pageable pageable) {

        Page<Brand> pages = this.brandRepository.findAll(pageable);

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
