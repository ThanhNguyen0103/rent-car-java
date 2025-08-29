package com.example.rentCar.domain.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarCriteriaDTO {
    private String keyword;
    private String capacity;
    private String carModel;
    private String brand;
    private Double minPrice;
    private Double maxPrice;

}
