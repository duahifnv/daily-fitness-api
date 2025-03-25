package com.fizalise.dailyfitness.dto.reports;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public final class DailyReport {
    @Schema(description = "Дата", example = "2025-03-23")
    private LocalDate date;
    @Schema(description = "Всего калорий", example = "1800")
    private Integer totalCalories;
    @Schema(description = "БЖУ")
    private NutritionDto totalNutrition;
    @Schema(description = "Разница с дневной нормой", example = "-250")
    private Integer differenceWithDailyNorm;
    @Schema(description = "Список приемов пищи")
    private List<MealReport> mealReports;
}
