package com.fizalise.dailyfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PortionDto(@Schema(description = "ID порции", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
                         Long id,
                         @Schema(description = "ID блюда", example = "1")
                         @NotNull
                         Long dishId,
                         @Schema(description = "Количество грамм в порции", example = "120")
                         Integer grams,
                         @Schema(description = "ID приема пищи", example = "52", accessMode = Schema.AccessMode.READ_ONLY)
                         Long mealId) {
}