package com.example.rentCar.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.rentCar.domain.User;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.service.UserService;
import com.example.rentCar.utils.annotation.ApiMessage;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create User success")
    public ResponseEntity<User> postCreateUser(@RequestBody User user) {
        User res = this.userService.handleCreateUser(user);
        String pw = passwordEncoder.encode(res.getPassword());
        res.setPassword(pw);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.handleSaveUser(res));

    }

    @PutMapping("/users")
    @ApiMessage("Update user success")
    public ResponseEntity<User> putMethodName(@RequestBody User user) {
        User res = this.userService.handleUpdateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user success")
    public ResponseEntity<Void> deleteMethod(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get user success")
    public ResponseEntity<User> getMethodName(@PathVariable("id") long id) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getUserById(id));
    }

    @GetMapping("/users")
    @ApiMessage("Get user with pagination success")
    public ResponseEntity<ResultPaginationDTO> getAllUserMethod(Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getUserWithPagination(pageable));
    }

}
