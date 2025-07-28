package com.example.rentCar.utils.error;

public class InvalidException extends RuntimeException {
    public InvalidException(String message) {
        super(message);
    }
}
