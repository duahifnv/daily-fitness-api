package com.fizalise.dailyfitness.mapper;

import com.fizalise.dailyfitness.dto.DishDto;
import com.fizalise.dailyfitness.dto.MealDto;
import com.fizalise.dailyfitness.dto.MealUpdateDto;
import com.fizalise.dailyfitness.dto.PortionDto;
import com.fizalise.dailyfitness.dto.reports.DailyReport;
import com.fizalise.dailyfitness.dto.reports.MealReport;
import com.fizalise.dailyfitness.dto.reports.NutritionDto;
import com.fizalise.dailyfitness.dto.reports.PortionReport;
import com.fizalise.dailyfitness.entity.Dish;
import com.fizalise.dailyfitness.entity.Meal;
import com.fizalise.dailyfitness.entity.Portion;
import com.fizalise.dailyfitness.entity.User;
import com.fizalise.dailyfitness.service.DishService;
import com.fizalise.dailyfitness.service.UserService;
import org.mapstruct.*;
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
    @Autowired
    private DishMapper dishMapper;

    @Mapping(target = "userId", expression = "java(meal.getUser().getId())")
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
    @Mapping(source = "authentication", target = "user", qualifiedByName = "getUser")
    @Mapping(target = "portions", qualifiedByName = "toPortions")
    public abstract Meal toMeal(MealDto mealDto, Authentication authentication);

    public void updateMeal(Meal meal, MealUpdateDto updateDto) {
        meal.setName(updateDto.name());
    }

    @Named("getUser")
    public User getUser(Authentication authentication) {
        return userService.findByEmail(authentication.getName());
    }
    @Named("getDish")
    public Dish getDish(Long dishId) {
        return dishService.findDish(dishId);
    }
    @Named("toDishDto")
    public DishDto toDishDto(Dish dish) {
        return dishMapper.toDto(dish);
    }
    /*
    Маппинг отчетов
     */
    @Mapping(source = "meals", target = "mealReports", qualifiedByName = "getMealReports")
    @Mapping(source = "meals", target = "totalCalories", qualifiedByName = "sumMealsCalories")
    @Mapping(source = "meals", target = "totalNutrition", qualifiedByName = "sumMealsNutrition")
    public abstract DailyReport toReport(List<Meal> meals, LocalDate date, Authentication authentication);

    @Mapping(target = "portions", qualifiedByName = "toPortionReports")
    @Mapping(source = "meal.portions", target = "totalCalories", qualifiedByName = "sumPortionsCalories")
    @Mapping(source = "meal.portions", target = "nutrition", qualifiedByName = "sumPortionsNutrition")
    public abstract MealReport toMealReport(Meal meal);

    @Mapping(target = "dish", qualifiedByName = "toDishDto")
    public abstract PortionReport toPortionReport(Portion portion);

    @Named("toPortionReports")
    public abstract List<PortionReport> toPortionReports(List<Portion> portions);
    @Named("getMealReports")
    public abstract List<MealReport> toMealReports(List<Meal> meal);

    @Named("sumPortionsCalories")
    public int sumPortionsCalories(List<Portion> portions) {
        return portions.stream()
                .mapToInt(p -> p.getDish().getCals() * p.getGrams() / 100)
                .sum();
    }
    @Named("sumMealsCalories")
    public int sumMealsCalories(List<Meal> meals) {
        var mealReports = toMealReports(meals);
        return mealReports.stream()
                .mapToInt(MealReport::totalCalories)
                .sum();
    }
    @Named("sumPortionsNutrition")
    public NutritionDto sumPortionsNutrition(List<Portion> portions) {
        int proteinsSum = 0;
        int fatsSum = 0;
        int carbsSum = 0;
        for (var portion : portions) {
            proteinsSum += portion.getDish().getProteins() * portion.getGrams() / 100;
            fatsSum += portion.getDish().getFats() * portion.getGrams() / 100;
            carbsSum += portion.getDish().getCarbs() * portion.getGrams() / 100;
        }
        return NutritionDto.builder()
                .proteins(proteinsSum)
                .fats(fatsSum)
                .carbs(carbsSum).build();
    }
    @Named("sumMealsNutrition")
    public NutritionDto sumMealsNutrition(List<Meal> meals) {
        var mealReports = toMealReports(meals);
        int proteinsSum = 0;
        int fatsSum = 0;
        int carbsSum = 0;
        for (var mealReport : mealReports) {
            proteinsSum += mealReport.nutrition().proteins();
            fatsSum += mealReport.nutrition().fats();
            carbsSum += mealReport.nutrition().carbs();
        }
        return NutritionDto.builder()
                .proteins(proteinsSum)
                .fats(fatsSum)
                .carbs(carbsSum).build();
    }
    @AfterMapping
    public void differenceWithDailyNorm(@MappingTarget DailyReport dailyReport, Authentication authentication) {
        int dailyNorm = getUser(authentication).getDailyNorm();
        dailyReport.setDifferenceWithDailyNorm(dailyReport.getTotalCalories() - dailyNorm);
    }
}
