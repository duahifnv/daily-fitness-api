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
                example = "user@mail.org")
        String email,
        @Schema(description = "Пол", enumAsRef = true)
        Gender gender,
        @Schema(description = "Возраст", minimum = "18", maximum = "150",
                example = "25")
        Integer age,
        @Schema(description = "Вес", minimum = "10", maximum = "500",
                example = "70")
        BigDecimal weight,
        @Schema(description = "Рост", minimum = "50", maximum = "250",
                example = "180")
        BigDecimal growth,
        @Schema(description = "Рассчитанная дневная норма калорий", example = "1800")
        Integer dailyNorm,
        @Schema(description = "Цель", enumAsRef = true)
        Goal goal,
        @Schema(description = "Активность", enumAsRef = true)
        Activity activity) {}