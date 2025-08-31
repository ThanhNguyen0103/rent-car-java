package com.example.rentCar.domain.dto;

import java.util.List;

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
    private List<String> carModel;
    private List<String> brand;
    private List<String> price;

}
