package com.example.rentCar.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private String capacity;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private boolean available;
    private double price;

    @OneToMany(mappedBy = "car")
    @JsonIgnore
    private List<CarImage> carImages;

    @ManyToOne
    @JoinColumn(name = "car_model_id")
    private CarModel carModel;
}
