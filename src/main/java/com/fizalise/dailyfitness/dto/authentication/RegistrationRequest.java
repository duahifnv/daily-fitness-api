package com.fizalise.dailyfitness.dto.authentication;

import com.fizalise.dailyfitness.entity.Activity;
import com.fizalise.dailyfitness.entity.Gender;
import com.fizalise.dailyfitness.entity.Goal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record RegistrationRequest(
                                  @NotBlank
                                  @Schema(description = "Имя пользователя", example = "Юзер")
                                  String name,
                                  @NotBlank @Email
                                  @Schema(description = "Почта", format = "email@domen.xx",
                                          example = "user@mail.org")
                                  String email,
                                  @NotBlank @Length(min = 4, max = 30)
                                  @Schema(description = "Пароль от 4 до 30 символов", minLength = 4,
                                          maxLength = 30, example = "user")
                                  String password,
                                  @NotNull
                                  @Schema(description = "Пол", enumAsRef = true)
                                  Gender gender,
                                  @NotNull @Min(18) @Max(99)
                                  @Schema(description = "Возраст", minimum = "18", maximum = "99", example = "25")
                                  Integer age,
                                  @NotNull @Min(0)
                                  @Schema(description = "Вес", minimum = "0", example = "70")
                                  BigDecimal weight,
                                  @NotNull @Min(0)
                                  @Schema(description = "Рост", minimum = "0", example = "180")
                                  BigDecimal growth,
                                  @NotNull
                                  @Schema(description = "Цель", enumAsRef = true)
                                  Goal goal,
                                  @NotNull
                                  @Schema(description = "Активность", enumAsRef = true)
                                  Activity activity) {}