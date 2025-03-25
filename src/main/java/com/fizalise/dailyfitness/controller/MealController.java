package com.fizalise.dailyfitness.controller;

import com.fizalise.dailyfitness.dto.MealDto;
import com.fizalise.dailyfitness.dto.MealUpdateDto;
import com.fizalise.dailyfitness.dto.reports.DailyReport;
import com.fizalise.dailyfitness.dto.reports.MealReport;
import com.fizalise.dailyfitness.entity.Meal;
import com.fizalise.dailyfitness.entity.Portion;
import com.fizalise.dailyfitness.mapper.MealMapper;
import com.fizalise.dailyfitness.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;
    private final MealMapper mealMapper;
    @Operation(summary = "Получить список всех приемов пищи пользователя")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MealDto> getAllMeals(@RequestParam(defaultValue = "0") @Min(0)
                                         Integer page,
                                     @RequestParam(required = false) @Schema(description = "Дата приема пищи")
                                     LocalDate date,
                                     Authentication authentication) {
        if (date != null) {
            return mealMapper.toDtos(mealService.findAllMeals(date, page, authentication));
        }
        return mealMapper.toDtos(mealService.findAllMeals(page, authentication));
    }
    @Operation(summary = "Получить прием пищи")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MealDto getMeal(@PathVariable Long id, Authentication authentication) {
        return mealMapper.toDto(mealService.findMeal(id, authentication));
    }
    @Operation(summary = "Добавить прием пищи")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MealDto addMeal(@Valid @RequestBody MealDto mealDto, Authentication authentication) {
        Meal meal = mealService.saveMeal(
                mealMapper.toMeal(mealDto, authentication)
        );
        return mealMapper.toDto(meal);
    }
    @Operation(summary = "Изменить прием пищи")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMeal(@PathVariable Long id, @Valid @RequestBody MealUpdateDto updateDto,
                           Authentication authentication) {
        Meal meal = mealService.findMeal(id, authentication);
        mealMapper.updateMeal(meal, updateDto);
        mealService.saveMeal(meal);
    }
    @Operation(summary = "Удалить прием пищи")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMeal(@PathVariable Long id, Authentication authentication) {
        mealService.removeMeal(id, authentication);
    }
    @Operation(summary = "Получить отчет за день")
    @GetMapping("/daily/report")
    @ResponseStatus(HttpStatus.OK)
    public DailyReport getDailyReport(@RequestParam(required = false)
            @Schema(description = "Дата отчета (по умолчанию сегодняшний)") Optional<LocalDate> date,
                                          Authentication authentication) {
        LocalDate localDate = date.orElse(LocalDate.now());
        List<Meal> meals = mealService.findAllMeals(localDate, authentication);
        return mealMapper.toReport(meals, localDate, authentication);
    }
    /**
     * Проверка, уложился ли пользователь в дневную норму
     * @return true - значение дневной нормы больше чем съеденные калории за день,
     *         false - в ином случае
     */
    @Operation(summary = "Проверить дневную норму",
            description = "Проверка, уложился ли пользователь в дневную норму")
    @GetMapping("/daily/is-norm")
    @ResponseStatus(HttpStatus.OK)
    public boolean isDailyNorm(@Schema(description = "Дата (по умолчанию сегодняшняя)") Optional<LocalDate> date,
                               Authentication authentication) {
        int summedMealsCalories = mealMapper.sumMealsCalories(
                mealService.findAllMeals(date.orElse(LocalDate.now()), authentication)
        );
        int dailyNorm = mealMapper.getUser(authentication).getDailyNorm();
        return summedMealsCalories <= dailyNorm;
    }
}
