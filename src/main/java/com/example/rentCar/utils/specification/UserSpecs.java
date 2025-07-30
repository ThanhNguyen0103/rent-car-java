package com.example.rentCar.utils.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.example.rentCar.domain.User;
import com.example.rentCar.domain.User_;

public class UserSpecs {
    public static Specification<User> isLongTermCustomer(String fullName) {
        return (root, query, builder) -> {
            return builder.like(root.get(User_.FULL_NAME), "%" + fullName + "%");
        };
    }
}
