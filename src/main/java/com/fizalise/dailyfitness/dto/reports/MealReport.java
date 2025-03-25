package com.fizalise.dailyfitness.dto.reports;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record MealReport(@Schema(description = "Название приема пищи", example = "Второй завтрак")
                         String name,
                         @Schema(description = "Список порций блюд")
                         List<PortionReport> portions,
                         @Schema(description = "Всего калорий", example = "1800")
                         Integer totalCalories,
                         @Schema(description = "БЖУ")
                         NutritionDto nutrition) {
}
