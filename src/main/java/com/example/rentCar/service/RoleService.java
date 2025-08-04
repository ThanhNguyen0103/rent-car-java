package com.example.rentCar.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.rentCar.domain.Role;
import com.example.rentCar.domain.User;
import com.example.rentCar.repository.RoleRepository;
import com.example.rentCar.utils.constant.RoleEnum;
import com.example.rentCar.utils.error.InvalidException;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role handleCreateRole(Role role) {
        Role res = new Role();
        boolean existsByName = this.roleRepository.existsByName(role.getName());
        if (existsByName) {
            throw new InvalidException("Role đã tồn tại ");
        }
        res.setDescription(role.getDescription());
        res.setName(role.getName());
        res.setActive(role.isActive());
        return res;
    }

    public Role handleSaveRole(Role role) {
        return this.roleRepository.save(role);
    }

    public Role getRoleById(long id) {
        Optional<Role> rOptional = this.roleRepository.findById(id);
        if (rOptional.isPresent()) {
            Role role = rOptional.get();
            return role;
        }
        return null;
    }

    public Role handleUpdateRole(Role role) {
        boolean existsName = this.roleRepository.existsByName(role.getName());
        if (existsName) {
            throw new InvalidException("Role name đã tồn tại");
        }

        Optional<Role> rOptional = this.roleRepository.findById(role.getId());

        if (rOptional.isPresent()) {
            Role res = rOptional.get();
            res.setActive(role.isActive());
            res.setDescription(role.getDescription());
            res.setName(role.getName());
            return this.handleSaveRole(res);
        }
        return null;
    }

    public void handleDeleteRole(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role res = roleOptional.get();
            this.roleRepository.delete(res);
        } else {
            throw new InvalidException("Role không tồn tại..");
        }
    }

    public Role getRoleByName(RoleEnum name) {
        return this.roleRepository.findByName(name);
    }
}
