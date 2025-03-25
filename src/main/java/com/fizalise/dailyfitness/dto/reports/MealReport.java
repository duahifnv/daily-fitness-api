package com.fizalise.dailyfitness.dto.reports;

import com.fizalise.dailyfitness.dto.PortionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record MealReport(@Schema(description = "Название приема пищи", example = "Второй завтрак")
                         String name,
                         @Schema(description = "Список порций блюд")
                         @NotEmpty
                         List<@Valid PortionDto> portions,
                         @Schema(description = "Всего калорий", example = "1800")
                         Integer totalCalories) {
}
