package com.fizalise.dailyfitness.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(@NotBlank @Email
                                    @Schema(description = "Почта", format = "email@domen.xx",
                                            example = "stacy@mail.org") String email,
                                    @NotBlank
                                    @Schema(description = "Пароль", minLength = 4,
                                            maxLength = 30, example = "stacy") String password) {}
