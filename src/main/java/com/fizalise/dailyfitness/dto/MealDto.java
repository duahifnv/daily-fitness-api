package com.fizalise.dailyfitness.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

public record MealDto(@Schema(description = "ID приема пищи", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
                      Long id,
                      @NotBlank
                      @Schema(description = "Название приема пищи", example = "Второй завтрак")
                      String name,
                      @Schema(description = "Дата приема пищи", example = "2025-03-23", accessMode = Schema.AccessMode.READ_ONLY)
                      LocalDate date,
                      @Schema(description = "ID пользователя", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
                      Long userId,
                      @Schema(description = "Список порций блюд")
                      @NotEmpty
                      List<PortionDto> portions
                      ) {
}
