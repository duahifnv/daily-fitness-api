package com.fizalise.dailyfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PortionDto(@Schema(description = "ID блюда", example = "1")
                         @NotNull @Min(1)
                         Long dishId,
                         @Schema(description = "Количество грамм в порции", example = "120") @Min(0)
                         Integer grams) {
}