package com.example.rentCar.domain;

import java.time.Instant;

import com.example.rentCar.utils.SecurityUtils;
import com.example.rentCar.utils.constant.RentalStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rentals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private RentalStatusEnum status;
    private Instant startDate;
    private Instant endDate;
    private double totalPrice;

    private String pickupLocation;
    private String dropoffLocation;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtils.getCurrentUserLogin() != null
                ? SecurityUtils.getCurrentUserLogin().get()
                : null;
        this.status = this.status != null ? this.status : RentalStatusEnum.PENDING;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtils.getCurrentUserLogin() != null
                ? SecurityUtils.getCurrentUserLogin().get()
                : null;
    }
}
