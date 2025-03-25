package com.fizalise.dailyfitness.controller;

import com.fizalise.dailyfitness.dto.MealDto;
import com.fizalise.dailyfitness.dto.MealUpdateDto;
import com.fizalise.dailyfitness.entity.Meal;
import com.fizalise.dailyfitness.mapper.MealMapper;
import com.fizalise.dailyfitness.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MealController {
    private final MealService mealService;
    private final MealMapper mealMapper;
    @Operation(summary = "Получить список всех приемов пищи пользователя")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MealDto> getAllMeals(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                     Authentication authentication) {
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
}
