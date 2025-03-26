package com.fizalise.dailyfitness.service;

import com.fizalise.dailyfitness.entity.Dish;
import com.fizalise.dailyfitness.exception.ResourceNotFoundException;
import com.fizalise.dailyfitness.repository.DishRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {
    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishService;

    private Dish createTestDish() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Test Dish");
        dish.setCals(500);
        dish.setProteins(30);
        dish.setFats(20);
        dish.setCarbs(50);
        return dish;
    }

    private Dish createTestDish2() {
        Dish dish = new Dish();
        dish.setId(2L);
        dish.setName("Another Dish");
        dish.setCals(350);
        dish.setProteins(25);
        dish.setFats(15);
        dish.setCarbs(40);
        return dish;
    }

    private List<Dish> createTestDishes() {
        return List.of(createTestDish(), createTestDish2());
    }

    @Test
    void findAllDishes_shouldReturnPageOfDishes() {
        // Arrange
        int page = 0;
        List<Dish> dishes = createTestDishes();
        PageRequest pageRequest = PageRequest.of(page, DishService.pageSize, Sort.by(Sort.Direction.ASC, "name"));
        Page<Dish> expectedPage = new PageImpl<>(dishes, pageRequest, dishes.size());

        when(dishRepository.findAll(pageRequest)).thenReturn(expectedPage);

        // Act
        Page<Dish> result = dishService.findAllDishes(page);

        // Assert
        assertNotNull(result);
        assertEquals(dishes.size(), result.getContent().size());
        assertEquals("Test Dish", result.getContent().get(0).getName());
        assertEquals(500, result.getContent().get(0).getCals());
        verify(dishRepository).findAll(pageRequest);
    }

    @Test
    void findDish_shouldReturnDishWhenExists() {
        // Arrange
        Dish expectedDish = createTestDish();
        when(dishRepository.findById(1L)).thenReturn(Optional.of(expectedDish));

        // Act
        Dish result = dishService.findDish(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Dish", result.getName());
        assertEquals(500, result.getCals());
        assertEquals(30, result.getProteins());
        assertEquals(20, result.getFats());
        assertEquals(50, result.getCarbs());
        verify(dishRepository).findById(1L);
    }

    @Test
    void findDish_shouldThrowResourceNotFoundExceptionWhenNotExists() {
        // Arrange
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> dishService.findDish(999L));
        verify(dishRepository).findById(999L);
    }

    @Test
    void saveDish_shouldSaveAndReturnDish() {
        // Arrange
        Dish newDish = new Dish();
        newDish.setName("New Dish");
        newDish.setCals(400);
        newDish.setProteins(20);
        newDish.setFats(15);
        newDish.setCarbs(45);

        Dish savedDish = new Dish();
        savedDish.setName("New Dish");
        savedDish.setCals(400);
        savedDish.setProteins(20);
        savedDish.setFats(15);
        savedDish.setCarbs(45);

        when(dishRepository.save(newDish)).thenReturn(savedDish);

        // Act
        Dish result = dishService.saveDish(newDish);

        // Assert
        assertNotNull(result);
        assertEquals("New Dish", result.getName());
        assertEquals(400, result.getCals());
        verify(dishRepository).save(newDish);
    }

    @Test
    void removeDish_shouldDeleteExistingDish() {
        // Arrange
        Dish dishToDelete = createTestDish();
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dishToDelete));
        doNothing().when(dishRepository).delete(dishToDelete);

        // Act
        dishService.removeDish(1L);

        // Assert
        verify(dishRepository).findById(1L);
        verify(dishRepository).delete(dishToDelete);
    }

    @Test
    void removeDish_shouldThrowResourceNotFoundExceptionWhenNotExists() {
        // Arrange
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> dishService.removeDish(999L));
        verify(dishRepository).findById(999L);
        verify(dishRepository, never()).delete(any());
    }
}