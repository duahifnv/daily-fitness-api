package com.fizalise.dailyfitness.dto.reports;

import com.fizalise.dailyfitness.dto.DishDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record PortionReport(@Schema(description = "Информация о блюде")
                            DishDto dish,
                            @Schema(description = "Количество грамм в порции", example = "120") @Min(0)
                            Integer grams) {
}
