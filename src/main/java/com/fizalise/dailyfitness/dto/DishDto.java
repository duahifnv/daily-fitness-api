package com.fizalise.dailyfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DishDto(@Schema(description = "ID блюда", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
                      Long id,
                      @NotBlank
                      @Schema(description = "Название блюда", example = "Сникерс")
                      String name,
                      @NotNull @Min(0)
                      @Schema(description = "Калорий на 100г", example = "300")
                      Integer cals,
                      @NotNull @Min(0)
                      @Schema(description = "Белков на 100г", example = "10")
                      Integer proteins,
                      @NotNull @Min(0)
                      @Schema(description = "Жиров на 100г", example = "30")
                      Integer fats,
                      @NotNull @Min(0)
                      @Schema(description = "Углеводов на 100г", example = "60")
                      Integer carbs) {
}
