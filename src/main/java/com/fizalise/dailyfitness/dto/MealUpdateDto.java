package com.fizalise.dailyfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record MealUpdateDto(@NotBlank
                            @Schema(description = "Название приема пищи", example = "Второй завтрак")
                            String name) {
}
