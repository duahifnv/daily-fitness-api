package com.fizalise.dailyfitness.mapper;

import com.fizalise.dailyfitness.dto.MealDto;
import com.fizalise.dailyfitness.dto.PortionDto;
import com.fizalise.dailyfitness.entity.Dish;
import com.fizalise.dailyfitness.entity.Meal;
import com.fizalise.dailyfitness.entity.Portion;
import com.fizalise.dailyfitness.entity.User;
import com.fizalise.dailyfitness.service.DishService;
import com.fizalise.dailyfitness.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", imports = LocalDate.class)
public abstract class MealMapper {
    @Autowired
    private UserService userService;
    @Autowired
    private DishService dishService;
    @Mapping(target = "userId", expression = "java(meal.getUser().getId())")
    @Mapping(target = "date", expression = "java(LocalDate.now())")
    @Mapping(target = "portions", qualifiedByName = "toPortionDtos")
    public abstract MealDto toDto(Meal meal);
    public abstract List<MealDto> toDtos(Page<Meal> meals);

    @Mapping(target = "dishId", expression = "java(portion.getDish().getId())")
    public abstract PortionDto toPortionDto(Portion portion);
    @Named("toPortionDtos")
    public abstract List<PortionDto> toPortionDtos(List<Portion> portions);

    @Mapping(source = "dishId", target = "dish", qualifiedByName = "getDish")
    public abstract Portion toPortion(PortionDto portionDto);
    @Named("toPortions")
    public abstract List<Portion> toPortions(List<PortionDto> portionDtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "mealDto.name", target = "name")
    @Mapping(target = "date", expression = "java(LocalDate.now())")
    @Mapping(source = "authentication", target = "user", qualifiedByName = "getUserId")
    @Mapping(target = "portions", qualifiedByName = "toPortions")
    public abstract Meal toMeal(MealDto mealDto, Authentication authentication);

    @Named("getUserId")
    public User getUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName());
    }
    @Named("getDish")
    public Dish getDish(Long dishId) {
        return dishService.findDish(dishId);
    }
}
