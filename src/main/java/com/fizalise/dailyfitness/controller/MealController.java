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
import java.util.ArrayList;
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
    @GetMapping("/report")
    @ResponseStatus(HttpStatus.OK)
    public DailyReport getDailyReport(@RequestParam(required = false)
            @Schema(description = "Дата отчета (по умолчанию сегодняшний)") Optional<LocalDate> date,
                                          Authentication authentication) {
        List<Meal> meals = mealService.findAllMeals(date.orElse(LocalDate.now()), authentication);
        List<MealReport> mealReports = new ArrayList<>();
        for (Meal meal : meals) {
            var mealReport = MealReport.builder()
                    .name(meal.getName())
                    .portions(mealMapper.toPortionDtos(meal.getPortions()))
                    .totalCalories(sumPortionsCalories(meal.getPortions())) // todo: Неверный счет
                    .build();
            mealReports.add(mealReport);
        }
        return DailyReport.builder()
                .date(date.orElse(LocalDate.now()))
                .totalCalories(sumMealReportsCalories(mealReports))
                .mealReports(mealReports)
                .build();
    }
    private int sumPortionsCalories(List<Portion> portions) {
        return portions.stream()
                .mapToInt(p -> p.getDish().getCals() * p.getGrams() / 100)
                .sum();
    }
    private int sumMealReportsCalories(List<MealReport> mealReports) {
        return mealReports.stream()
                .mapToInt(MealReport::totalCalories)
                .sum();
    }
}
