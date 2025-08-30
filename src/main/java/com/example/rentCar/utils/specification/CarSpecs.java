package com.example.rentCar.utils.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.rentCar.domain.Brand_;
import com.example.rentCar.domain.Car;
import com.example.rentCar.domain.CarModel_;
import com.example.rentCar.domain.Car_;

public class CarSpecs {
    public static Specification<Car> hasCarModel(List<String> carModel) {
        return (root, query, builder) -> {
            if (carModel == null || carModel.isEmpty())
                return null;
            return builder.in(root.get(Car_.CAR_MODEL).get(CarModel_.NAME)).value(carModel);
        };
    }

    public static Specification<Car> hasBrand(List<String> brands) {
        return (root, query, builder) -> {
            if (brands == null || brands.isEmpty())
                return null;
            return builder.in(root.get(Car_.CAR_MODEL).get(CarModel_.BRAND).get(Brand_.NAME)).value(brands);
        };
    }

    public static Specification<Car> priceGreaterThanOrEqual(Double minPrice) {
        return (root, query, builder) -> {
            if (minPrice == null)
                return null;
            return builder.greaterThanOrEqualTo(root.get(Car_.PRICE), minPrice);
        };
    }

    public static Specification<Car> priceLessThanOrEqual(Double maxPrice) {
        return (root, query, builder) -> {
            if (maxPrice == null)
                return null;
            return builder.lessThanOrEqualTo(root.get(Car_.PRICE), maxPrice);
        };
    }

    public static Specification<Car> keywordSearch(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isEmpty())
                return null;
            String pattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get(Car_.CAR_MODEL).get(CarModel_.NAME)), pattern),
                    builder.like(builder.lower(root.get(Car_.CAR_MODEL).get(CarModel_.BRAND).get(Brand_.NAME)),
                            pattern));
        };
    }
}
