package com.example.rentCar.controller.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.example.rentCar.utils.constant.TokenType;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Value("${thanh.jwt.refreshToken-validity-in-seconds}")
    private long expiresRefreshToken;
    private final AuthenticationManager authenticationManager;
    private final SecurityUtils securityUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, SecurityUtils securityUtils,
            UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.securityUtils = securityUtils;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    @ApiMessage("Login success")
    public ResponseEntity<ResLogin> postLogin(@RequestBody UserLoginDTO user) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                user.getUsername(),
                user.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

        User currentUser = this.userService.getUserByEmail(user.getUsername());

        ResLogin.ResUserLogin userLogin = new ResLogin.ResUserLogin(
                currentUser.getId(), currentUser.getEmail(),
                currentUser.getFullName());

        String accessToken = this.securityUtils.generateJwt(userLogin, TokenType.ACCESS);
        String refreshToken = this.securityUtils.generateJwt(userLogin, TokenType.REFRESH);

        this.userService.handleSaveRefreshToken(refreshToken, currentUser);
        ResLogin res = new ResLogin(accessToken, userLogin);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expiresRefreshToken)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(res);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("refresh success")
    public ResponseEntity<ResLogin> getRefresh(@CookieValue("refreshToken") String refreshToken) {

        String email = this.securityUtils.extractSubjectFromValidRefreshToken(refreshToken);
        User currentUser = this.userService.getUserByEmailAndRefreshToken(email, refreshToken);

        ResLogin.ResUserLogin userLogin = new ResLogin.ResUserLogin(
                currentUser.getId(), currentUser.getEmail(),
                currentUser.getFullName());

        String accessToken = this.securityUtils.generateJwt(userLogin, TokenType.ACCESS);
        String newRefreshToken = this.securityUtils.generateJwt(userLogin, TokenType.REFRESH);

        this.userService.handleSaveRefreshToken(newRefreshToken, currentUser);
        ResLogin res = new ResLogin(accessToken, userLogin);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expiresRefreshToken)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Get account success")
    public ResponseEntity<ResLogin.ResUserLogin> getAccount() {
        String email = SecurityUtils.getCurrentUserLogin() != null
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.getUserByEmail(email);
        ResLogin.ResUserLogin userLogin = new ResLogin.ResUserLogin(
                currentUser.getId(), currentUser.getEmail(),
                currentUser.getFullName());
        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/auth/logout")
    @ApiMessage("Logout success")
    public ResponseEntity<Void> postLogout() {
        String email = SecurityUtils.getCurrentUserLogin() != null
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.getUserByEmail(email);
        this.userService.handleSaveRefreshToken(null, currentUser);
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(null);
    }

    @PostMapping("/register")
    @ApiMessage("Register success")
    public ResponseEntity<ResLogin.ResUserLogin> postRegister(@RequestBody User user) {
        User currentUser = this.userService.handleCreateUser(user);
        String pw = passwordEncoder.encode(currentUser.getPassword());
        currentUser.setPassword(pw);
        this.userService.handleSaveUser(currentUser);
        ResLogin.ResUserLogin res = new ResLogin.ResUserLogin(
                currentUser.getId(), currentUser.getEmail(),
                currentUser.getFullName());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

}
