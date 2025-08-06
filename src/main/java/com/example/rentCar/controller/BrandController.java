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

import com.example.rentCar.domain.Brand;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.service.BrandService;
import com.example.rentCar.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/brands")
    @ApiMessage("Create brand success")
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) {
        Brand res = this.brandService.handleCreateBrand(brand);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.brandService.handleSaveBrand(res));
    }

    @PutMapping("/brands")
    @ApiMessage("Update brand success")
    public ResponseEntity<Brand> updateBrand(@RequestBody Brand brand) {
        return ResponseEntity.ok().body(this.brandService.handleUpdateBrand(brand));
    }

    @DeleteMapping("/brands/{id}")
    @ApiMessage("Delete brand success")
    public ResponseEntity<Void> deleteBrand(@PathVariable("id") long id) {
        this.brandService.handleDeleteBrand(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/brands/{id}")
    @ApiMessage("Get brand success")
    public ResponseEntity<Brand> getBrandById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.brandService.getBrandById(id));
    }

    @GetMapping("/brands")
    @ApiMessage("Get brands with pagination success")
    public ResponseEntity<ResultPaginationDTO> getAllBrand(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.brandService.getBrandWithPagination(pageable));
    }

}
