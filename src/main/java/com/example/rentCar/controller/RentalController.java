package com.example.rentCar.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rentCar.domain.Rental;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.service.RentalService;
import com.example.rentCar.utils.annotation.ApiMessage;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class RentalController {
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/rentals")
    @ApiMessage("Create rental success")
    public ResponseEntity<Rental> createRental(@RequestBody Rental rental) {
        Rental res = this.rentalService.handleCreateRental(rental);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/rentals")
    @ApiMessage("Update rental success")
    public ResponseEntity<Rental> updateRental(@RequestBody Rental rental) {
        Rental res = this.rentalService.handleUpdateRental(rental);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);

    }

    @DeleteMapping("/rentals/{id}")
    @ApiMessage("Delete rentals success")
    public ResponseEntity<Void> deleteRental(@PathVariable("id") long id) {
        this.rentalService.deleteRental(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/rentals/{id}")
    @ApiMessage("Get rentals success")
    public ResponseEntity<Rental> getRentalById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.rentalService.getRentalById(id));
    }

    @GetMapping("/rentals")
    @ApiMessage("Get rentals with pagination success")
    public ResponseEntity<ResultPaginationDTO> getAllRental(Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.rentalService.getRentalWithPagination(pageable));
    }

    @GetMapping("/user/{id}/rentals")
    @ApiMessage("Get rentals by user with pagination success")
    public ResponseEntity<ResultPaginationDTO> getRentalByUser(@PathVariable("id") long id,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.rentalService.getRentalByUser(pageable,
                id));
    }

}
