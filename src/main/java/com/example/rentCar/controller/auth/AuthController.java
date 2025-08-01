package com.example.rentCar.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rentCar.domain.req.UserLoginDTO;
import com.example.rentCar.domain.res.ResLogin;
import com.example.rentCar.utils.service.SecurityUtils;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final SecurityUtils securityUtils;

    public AuthController(AuthenticationManager authenticationManager, SecurityUtils securityUtils) {
        this.authenticationManager = authenticationManager;
        this.securityUtils = securityUtils;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLogin> postLogin(@RequestBody UserLoginDTO user) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(),
                user.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
        String abc = this.securityUtils.generateJwt();

        return ResponseEntity.ok().body(null);
    }
}
