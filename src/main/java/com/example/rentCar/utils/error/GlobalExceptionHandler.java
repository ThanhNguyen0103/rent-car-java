package com.example.rentCar.utils.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.rentCar.domain.res.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidException(Exception ex) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setMessage(ex.getMessage());
        res.setError("Conflict ...");
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @ExceptionHandler(value = {
            BadCredentialsException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Đăng nhập không thành công");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(res.getStatusCode()).body(res);

    }
}
