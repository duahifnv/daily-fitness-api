package com.fizalise.dailyfitness.dto.authentication;

import com.fizalise.dailyfitness.entity.Activity;
import com.fizalise.dailyfitness.entity.Gender;
import com.fizalise.dailyfitness.entity.Goal;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record UserResponse(
        @Schema(description = "Имя пользователя", example = "Юзер")
        String name,
        @Schema(description = "Почта", format = "email@domen.xx",
                example = "mail@mail.org")
        String email,
        @Schema(description = "Пол", enumAsRef = true)
        Gender gender,
        @Schema(description = "Возраст", minimum = "18", maximum = "99")
        Integer age,
        @Schema(description = "Вес", minimum = "0")
        BigDecimal weight,
        @Schema(description = "Рост", minimum = "0")
        BigDecimal growth,
        @Schema(description = "Рассчитанная дневная норма калорий")
        Integer dailyNorm,
        @Schema(description = "Цель", enumAsRef = true)
        Goal goal,
        @Schema(description = "Активность", enumAsRef = true)
        Activity activity) {}