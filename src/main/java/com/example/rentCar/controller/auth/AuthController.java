package com.example.rentCar.controller.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rentCar.domain.User;
import com.example.rentCar.domain.req.UserLoginDTO;
import com.example.rentCar.domain.res.ResLogin;
import com.example.rentCar.service.UserService;
import com.example.rentCar.utils.SecurityUtils;
import com.example.rentCar.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final SecurityUtils securityUtils;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, SecurityUtils securityUtils,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.securityUtils = securityUtils;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    @ApiMessage("Login success")
    public ResponseEntity<ResLogin> postLogin(@RequestBody UserLoginDTO user) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(),
                user.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
        User currentUser = this.userService.getUserByEmail(user.getUsername());

        ResLogin.ResUserLogin userLogin = new ResLogin.ResUserLogin(
                currentUser.getId(), currentUser.getEmail(),
                currentUser.getFullName());

        String accessToken = this.securityUtils.generateJwt(userLogin);
        ResLogin res = new ResLogin(accessToken, userLogin);

        ResponseCookie resCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(100000)
                .build();
        // .status(HttpStatus.OK)
        // .header(HttpHeaders.SET_COOKIE, cookie.toString())
        // .body(res);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(res);
    }
}
