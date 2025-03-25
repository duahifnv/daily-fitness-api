package com.fizalise.dailyfitness.dto.reports;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record DailyReport(@Schema(description = "Дата", example = "2025-03-23")
                          LocalDate date,
                          @Schema(description = "Всего калорий", example = "1800")
                          Integer totalCalories,
                          @Schema(description = "Список приемов пищи")
                          List<MealReport> mealReports) {
}
