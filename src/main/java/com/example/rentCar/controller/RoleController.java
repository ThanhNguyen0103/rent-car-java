package com.example.rentCar.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.rentCar.domain.Role;
import com.example.rentCar.domain.User;
import com.example.rentCar.service.RoleService;
import com.example.rentCar.utils.annotation.ApiMessage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create role success")
    public ResponseEntity<Role> postMethodName(@RequestBody Role role) {
        Role res = this.roleService.handleCreateRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.handleSaveRole(res));
    }

    @PutMapping("/roles")
    @ApiMessage("Update role success")
    public ResponseEntity<Role> putMethodName(@RequestBody Role role) {
        return ResponseEntity.ok().body(this.roleService.handleUpdateRole(role));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete user success")
    public ResponseEntity<Void> deleteMethod(@PathVariable("id") long id) {
        this.roleService.handleDeleteRole(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Get role success")
    public ResponseEntity<Role> getMethodName(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.getRoleById(id));
    }

}
