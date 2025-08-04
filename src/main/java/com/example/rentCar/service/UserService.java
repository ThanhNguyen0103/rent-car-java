package com.example.rentCar.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.rentCar.domain.Role;
import com.example.rentCar.domain.User;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.repository.UserRepository;
import com.example.rentCar.utils.error.InvalidException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository,
            RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public User handleCreateUser(User user) {
        User res = new User();
        boolean userExists = this.userRepository.existsByEmail(user.getEmail());
        if (userExists) {
            throw new InvalidException("Email đã tồn tại ");
        }
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setFullName(user.getFullName());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setAvatar(user.getAvatar());
        res.setPassword(user.getPassword());
        if (user.getRole() != null) {
            Role role = this.roleService.getRoleById(user.getRole().getId());
            res.setRole(role);
        }
        return res;
    }

    public User handleSaveUser(User user) {
        return this.userRepository.save(user);
    }

    public User handleUpdateUser(User user) {
        Optional<User> userOptional = this.userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            User res = userOptional.get();
            res.setAge(user.getAge());
            res.setFullName(user.getFullName());
            res.setGender(user.getGender());
            // res.setPassword(user.getPassword());
            if (user.getRole() != null) {
                Role role = this.roleService.getRoleById(user.getRole().getId());
                res.setRole(role);
            }
            return this.userRepository.save(res);
        } else {
            throw new InvalidException("User không tồn tại..");
        }

    }

    public void handleDeleteUser(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            User res = userOptional.get();
            this.userRepository.delete(res);
        } else {
            throw new InvalidException("User không tồn tại..");
        }
    }

    public User getUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            User res = userOptional.get();
            return res;
        } else {
            throw new InvalidException("User không tồn tại..");
        }
    }

    public ResultPaginationDTO getUserWithPagination(Pageable pageable) {

        Page<User> pages = this.userRepository.findAll(pageable);

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

    public User getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("");
        }
        return user;
    }

    public User handleSaveRefreshToken(String refreshToken, User user) {
        user.setRefreshToken(refreshToken);
        return this.userRepository.save(user);
    }

    public User getUserByEmailAndRefreshToken(String email, String token) {
        User user = this.userRepository.findByEmailAndRefreshToken(email, token);
        if (user == null) {
            throw new InvalidException("User không tồn tại");
        }
        return user;
    }

}
