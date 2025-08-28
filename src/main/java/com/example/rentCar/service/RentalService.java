package com.example.rentCar.service;

import java.time.temporal.ChronoUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rentCar.domain.Car;
import com.example.rentCar.domain.Rental;
import com.example.rentCar.domain.User;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.repository.RentalRepository;
import com.example.rentCar.utils.constant.RentalStatusEnum;
import com.example.rentCar.utils.error.InvalidException;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final UserService userService;
    private final CarService carService;

    public RentalService(RentalRepository rentalRepository, UserService userService,
            CarService carService) {
        this.rentalRepository = rentalRepository;
        this.userService = userService;
        this.carService = carService;
    }

    public Rental handleCreateRental(Rental rental) {

        User user = this.userService.getUserById(rental.getUser().getId());

        Car car = this.carService.getCarById(rental.getCar().getId());

        Rental res = new Rental();
        res.setCar(car);
        res.setUser(user);

        res.setDropoffLocation(rental.getDropoffLocation());
        res.setPickupLocation(rental.getPickupLocation());
        res.setEndDate(rental.getEndDate());
        res.setStartDate(rental.getStartDate());
        res.setStatus(rental.getStatus());

        if (car != null && rental.getStartDate() != null && rental.getEndDate() != null) {
            res.setTotalPrice(
                    (ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate()) + 1)
                            * car.getPrice());
        }
        return this.rentalRepository.save(res);
    }

    public Rental handleUpdateRental(Rental rental) {
        Rental res = this.rentalRepository.findById(rental.getId())
                .orElseThrow(() -> new InvalidException("Rental không tồn tại"));
        if (res.getStatus() == RentalStatusEnum.COMPLETED ||
                res.getStatus() == RentalStatusEnum.CANCELED) {
            throw new InvalidException("Không thể sửa đơn hàng đã hoàn thành hoặc bị huỷ");
        }
        if (rental.getStatus() != null) {
            res.setStatus(rental.getStatus());
        }
        if (rental.getDropoffLocation() != null) {
            res.setDropoffLocation(rental.getDropoffLocation());
        }
        if (rental.getPickupLocation() != null) {
            res.setPickupLocation(rental.getPickupLocation());
        }
        if (rental.getEndDate() != null) {
            res.setEndDate(rental.getEndDate());
        }
        if (rental.getStartDate() != null) {
            res.setStartDate(rental.getStartDate());
        }
        if (res.getCar() != null && res.getStartDate() != null && res.getEndDate() != null) {
            res.setTotalPrice(
                    (ChronoUnit.DAYS.between(res.getStartDate(), res.getEndDate()) + 1) * res.getCar().getPrice());
        }
        return this.rentalRepository.save(res);
    }

    public Rental getRentalById(long id) {
        return this.rentalRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Đơn hàng không tồn tại"));
    }

    public void deleteRental(long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new InvalidException("Đơn hàng không tồn tại"));

        if (rental.getStatus() == RentalStatusEnum.COMPLETED) {
            throw new InvalidException("Không thể xóa rental đã hoàn thành");
        }

        rentalRepository.delete(rental);
    }

    public ResultPaginationDTO getRentalWithPagination(Pageable pageable) {

        Page<Rental> pages = this.rentalRepository.findAll(pageable);

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

    public ResultPaginationDTO getRentalByUser(Pageable pageable, long id) {

        Page<Rental> pages = this.rentalRepository.findAllByUserId(id, pageable);

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
