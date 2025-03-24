package com.fizalise.dailyfitness.mapper;

import com.fizalise.dailyfitness.dto.MealDto;
import com.fizalise.dailyfitness.dto.PortionDto;
import com.fizalise.dailyfitness.entity.Meal;
import com.fizalise.dailyfitness.entity.Portion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface MealMapper {
    @Mapping(target = "userId", expression = "java(meal.getUser().getId())")
    @Mapping(target = "date", expression = "java(LocalDate.now())")
    @Mapping(target = "portions", qualifiedByName = "toPortionDtos")
    MealDto toDto(Meal meal);
    List<MealDto> toDtos(Page<Meal> meals);
    @Mapping(target = "dishId", expression = "java(portion.getDish().getId())")
    @Mapping(target = "mealId", expression = "java(portion.getMeal().getId())")
    PortionDto toPortionDto(Portion portion);
    @Named("toPortionDtos")
    List<PortionDto> toPortionDtos(Set<Portion> portions);
}
