package com.example.rentCar.domain;

import java.time.Instant;
import java.util.List;

import com.example.rentCar.utils.SecurityUtils;
import com.example.rentCar.utils.constant.FuelTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "Số chỗ  không được để trống")
    @NotBlank(message = "Số chỗ không được để trống")
    private String capacity;
    @Column(columnDefinition = "MEDIUMTEXT")
    @NotNull(message = "Description không được để trống")
    @NotBlank(message = "Description không được để trống")
    private String description;

    private boolean available;
    @Min(value = 1, message = "Giá phải ít nhất 1đ")
    private double price;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<CarImage> carImages;

    @ManyToOne
    @JoinColumn(name = "car_model_id")
    private CarModel carModel;

    private String location;

    @NotNull
    @Min(1970)
    private int year;

    @NotNull
    @Min(1)
    private int mileage;
    @NotNull
    private FuelTypeEnum fuelType;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtils.getCurrentUserLogin() != null
                ? SecurityUtils.getCurrentUserLogin().get()
                : null;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtils.getCurrentUserLogin() != null
                ? SecurityUtils.getCurrentUserLogin().get()
                : null;
    }
}
