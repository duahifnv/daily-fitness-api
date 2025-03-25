package com.fizalise.dailyfitness.service;

import com.fizalise.dailyfitness.dto.authentication.AuthenticationRequest;
import com.fizalise.dailyfitness.dto.authentication.JwtResponse;
import com.fizalise.dailyfitness.dto.authentication.UserRequest;
import com.fizalise.dailyfitness.entity.Role;
import com.fizalise.dailyfitness.entity.User;
import com.fizalise.dailyfitness.exception.CustomBadCredentialsException;
import com.fizalise.dailyfitness.exception.UserNotFoundException;
import com.fizalise.dailyfitness.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j(topic = "Сервис аутентификации")
public record AuthService(JwtService jwtService,
                          UserService userService,
                          UserMapper userMapper,
                          AuthenticationManager authenticationManager) {
    public JwtResponse registerNewUser(UserRequest userRequest) {
        User user = userService.createUser(
                userMapper.toUser(userRequest, Role.ROLE_USER)
        );
        log.info("Зарегистрирован новый пользователь: {}", user);
        return new JwtResponse(
                jwtService.generateToken(user)
        );
    }
    public JwtResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.email(),
                            authenticationRequest.password()
                    )
            );
            log.info("Пользователь с email: {} успешно аутентифицировал себя",
                    authenticationRequest.email());
            return new JwtResponse(
                    jwtService.generateToken(
                            userService().findByEmail(authenticationRequest.email())
                    )
            );
        } catch (BadCredentialsException e) {
            throw new CustomBadCredentialsException();
        } catch (InternalAuthenticationServiceException e) {
            throw new UserNotFoundException();
        }
    }
    public boolean hasAdminRole(Authentication authentication) {
        return getAuthorities(authentication).contains(Role.ROLE_ADMIN.name());
    }
    public List<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
}
