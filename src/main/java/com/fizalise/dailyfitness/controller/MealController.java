package com.fizalise.dailyfitness.controller;

import com.fizalise.dailyfitness.dto.MealDto;
import com.fizalise.dailyfitness.mapper.MealMapper;
import com.fizalise.dailyfitness.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;
    private final MealMapper mealMapper;
    @Operation(summary = "Получить список всех приемов пищи")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MealDto> getAllMeals(@RequestParam(defaultValue = "0") @Min(0) Integer page) {
        return mealMapper.toDtos(mealService.findAllMeals(page));
    }
    @Operation(summary = "Получить прием пищи")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MealDto getMeal(@PathVariable Long id) {
        return mealMapper.toDto(mealService.findMeal(id));
    }
}
