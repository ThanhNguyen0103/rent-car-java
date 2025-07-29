package com.example.rentCar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rentCar.domain.User;
import com.example.rentCar.domain.res.ResultPaginationDTO;
import com.example.rentCar.repository.UserRepository;
import com.example.rentCar.utils.error.InvalidException;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        res.setAvatar(user.getAvatar());
        res.setPassword(user.getPassword());
        return this.userRepository.save(res);

    }

    public User handleUpdateUser(User user) {
        Optional<User> userOptional = this.userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            User res = userOptional.get();
            res.setAge(user.getAge());
            res.setFullName(user.getFullName());
            res.setGender(user.getGender());
            // res.setPassword(user.getPassword());

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

}
