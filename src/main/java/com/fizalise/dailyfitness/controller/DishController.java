package com.fizalise.dailyfitness.controller;

import com.fizalise.dailyfitness.dto.DishDto;
import com.fizalise.dailyfitness.entity.Dish;
import com.fizalise.dailyfitness.mapper.DishMapper;
import com.fizalise.dailyfitness.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;
    private final DishMapper dishMapper;
    @Operation(summary = "Получить список всех блюд")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DishDto> getAllDishes(@RequestParam(defaultValue = "0") @Min(0) Integer page) {
        return dishMapper.toDtos(dishService.findAllDishes(page));
    }
    @Operation(summary = "Получить блюдо")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DishDto getDish(@PathVariable Long id) {
        return dishMapper.toDto(dishService.findDish(id));
    }
    @Operation(summary = "Добавить блюдо")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DishDto addDish(@RequestBody DishDto dishDto) {
        Dish dish = dishService.saveDish(dishMapper.toDish(dishDto));
        return dishMapper.toDto(dish);
    }
    @Operation(summary = "Изменить блюдо")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateDish(@PathVariable Long id, @RequestBody DishDto dishDto) {
        Dish dish = dishService.findDish(id);
        dishMapper.updateDish(dish, dishDto);
        dishService.saveDish(dish);
    }
    @Operation(summary = "Удалить блюдо")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDish(@PathVariable Long id) {
        dishService.removeDish(id);
    }
}
