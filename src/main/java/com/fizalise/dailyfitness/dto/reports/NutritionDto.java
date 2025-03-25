package com.fizalise.dailyfitness.dto.reports;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record NutritionDto(@Schema(description = "Белков", example = "130")
                           Integer proteins,
                           @Schema(description = "Жиров", example = "60")
                           Integer fats,
                           @Schema(description = "Углеводов", example = "30")
                           Integer carbs) {
}
