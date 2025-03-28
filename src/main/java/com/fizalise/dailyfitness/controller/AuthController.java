package com.fizalise.dailyfitness.controller;

import com.fizalise.dailyfitness.dto.authentication.AuthenticationRequest;
import com.fizalise.dailyfitness.dto.authentication.JwtResponse;
import com.fizalise.dailyfitness.dto.authentication.UserRequest;
import com.fizalise.dailyfitness.dto.authentication.UserResponse;
import com.fizalise.dailyfitness.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @Operation(summary = "Зарегистрировать нового пользователя",
            description = "Возвращает сгенерированный JWT-токен")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse registerNewUser(@Valid @RequestBody UserRequest userRequest) {
        return authService.registerNewUser(userRequest);
    }
    @Operation(summary = "Аутентифицировать существующего пользователя",
            description = "Возвращает сгенерированный JWT-токен")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return authService.authenticate(authenticationRequest);
    }
    @Operation(summary = "Получить информацию о себе")
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public UserResponse getUser(Authentication authentication) {
        return authService.getUser(authentication);
    }
    @Operation(summary = "Изменить информацию о себе")
    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public void updateUser(@Valid @RequestBody UserRequest userRequest,
                           Authentication authentication) {
        authService.updateUser(userRequest, authentication);
    }
}
