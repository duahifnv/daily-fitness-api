package com.fizalise.dailyfitness.mapper;

import com.fizalise.dailyfitness.dto.DishDto;
import com.fizalise.dailyfitness.entity.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DishMapper {
    @Mapping(target = "id", ignore = true)
    Dish toDish(DishDto dto);
    DishDto toDto(Dish dish);
    List<DishDto> toDtos(Page<Dish> dishes);
    @Mapping(target = "id", ignore = true)
    void updateDish(@MappingTarget Dish dish, DishDto dishDto);
}
