package com.fizalise.dailyfitness.dto;

import com.fizalise.dailyfitness.entity.Activity;
import com.fizalise.dailyfitness.entity.Gender;
import com.fizalise.dailyfitness.entity.Goal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record RegistrationRequest(
                                  @NotBlank
                                  @Schema(description = "Имя пользователя", example = "Юзер")
                                  String name,
                                  @NotBlank @Email
                                  @Schema(description = "Почта", format = "email@domen.xx",
                                          example = "mail@mail.ru")
                                  String email,
                                  @NotBlank @Length(min = 4, max = 30)
                                  @Schema(description = "Пароль от 4 до 30 символов", minLength = 4,
                                          maxLength = 30, example = "3afl3ajf")
                                  String password,
                                  @NotBlank
                                  @Schema(description = "Пол", enumAsRef = true)
                                  Gender gender,
                                  @NotNull @Min(18) @Max(99)
                                  @Schema(description = "Возраст", minimum = "18", maximum = "99")
                                  Integer age,
                                  @NotNull @Min(0)
                                  @Schema(description = "Вес", minimum = "0")
                                  BigDecimal weight,
                                  @NotNull @Min(0)
                                  @Schema(description = "Рост", minimum = "0")
                                  BigDecimal growth,
                                  @NotBlank
                                  @Schema(description = "Цель", enumAsRef = true)
                                  Goal goal,
                                  @NotBlank
                                  @Schema(description = "Активность", enumAsRef = true)
                                  Activity activity) {}