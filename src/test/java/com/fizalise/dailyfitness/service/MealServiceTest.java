package com.fizalise.dailyfitness.service;

import com.fizalise.dailyfitness.entity.*;
import com.fizalise.dailyfitness.exception.ResourceNotFoundException;
import com.fizalise.dailyfitness.repository.MealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MealService mealService;

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setGender(Gender.MALE);
        user.setAge(30);
        user.setWeight(new BigDecimal("75.5"));
        user.setGrowth(new BigDecimal("180.0"));
        user.setDailyNorm(2500);
        user.setGoal(Goal.WEIGHT_GAIN);
        user.setActivity(Activity.HIGH);
        user.setRole(Role.ROLE_USER);
        return user;
    }

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

    private Portion createTestPortion() {
        Portion portion = new Portion();
        portion.setId(1L);
        portion.setDish(createTestDish());
        portion.setGrams(200);
        return portion;
    }

    private Meal createTestMeal() {
        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Test Meal");
        meal.setDate(LocalDate.now());
        meal.setUser(createTestUser());
        meal.setPortions(List.of(createTestPortion()));
        return meal;
    }

    @Test
    void findAllMeals_withPage_shouldReturnPageOfMeals() {
        // Arrange
        int page = 0;
        User user = createTestUser();
        Meal meal = createTestMeal();
        PageRequest pageRequest = PageRequest.of(page, MealService.pageSize, Sort.by(Sort.Direction.DESC, "date"));
        Page<Meal> expectedPage = new PageImpl<>(List.of(meal), pageRequest, 1);

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userService.findByUsername(user.getEmail())).thenReturn(user);
        when(mealRepository.findAllByUser(user, pageRequest)).thenReturn(expectedPage);

        // Act
        Page<Meal> result = mealService.findAllMeals(page, authentication);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(meal.getId(), result.getContent().get(0).getId());
        verify(mealRepository).findAllByUser(user, pageRequest);
    }

    @Test
    void findMeal_shouldReturnMealWhenAdminAccess() {
        // Arrange
        User adminUser = createTestUser();
        adminUser.setRole(Role.ROLE_ADMIN);
        User mealOwner = createTestUser();
        mealOwner.setId(2L);
        Meal meal = createTestMeal();
        meal.setUser(mealOwner);

        when(mealRepository.findById(meal.getId())).thenReturn(Optional.of(meal));
        when(authentication.getName()).thenReturn(adminUser.getEmail());
        when(authService.hasAdminRole(authentication)).thenReturn(true);
        when(userService.findByUsername(adminUser.getEmail())).thenReturn(adminUser);

        // Act
        Meal result = mealService.findMeal(meal.getId(), authentication);

        // Assert
        assertNotNull(result);
        assertEquals(meal.getId(), result.getId());
        verify(mealRepository).findById(meal.getId());
    }

    @Test
    void saveMeal_shouldSaveAndReturnMeal() {
        // Arrange
        Meal mealToSave = createTestMeal();
        mealToSave.setId(null);

        when(mealRepository.save(mealToSave)).thenAnswer(invocation -> {
            Meal saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        // Act
        Meal result = mealService.saveMeal(mealToSave);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Test Meal", result.getName());
        verify(mealRepository).save(mealToSave);
    }

    @Test
    void removeMeal_shouldDeleteWhenAdminAccess() {
        // Arrange
        User adminUser = createTestUser();
        adminUser.setRole(Role.ROLE_ADMIN);
        User mealOwner = createTestUser();
        mealOwner.setId(2L);
        Meal meal = createTestMeal();
        meal.setUser(mealOwner);

        when(mealRepository.findById(meal.getId())).thenReturn(Optional.of(meal));
        when(authentication.getName()).thenReturn(adminUser.getEmail());
        when(authService.hasAdminRole(authentication)).thenReturn(true);
        when(userService.findByUsername(adminUser.getEmail())).thenReturn(adminUser);
        doNothing().when(mealRepository).delete(meal);

        // Act
        mealService.removeMeal(meal.getId(), authentication);

        // Assert
        verify(mealRepository).findById(meal.getId());
        verify(mealRepository).delete(meal);
    }

    // Дополнительные тесты для проверки других сценариев
    @Test
    void findMeal_shouldThrowWhenUserNotOwnerAndNotAdmin() {
        // Arrange
        User user1 = createTestUser();
        user1.setId(1L);
        User user2 = createTestUser();
        user2.setId(2L);
        Meal meal = createTestMeal();
        meal.setUser(user1);

        when(mealRepository.findById(meal.getId())).thenReturn(Optional.of(meal));
        when(authentication.getName()).thenReturn(user2.getEmail());
        when(authService.hasAdminRole(authentication)).thenReturn(false);
        when(userService.findByUsername(user2.getEmail())).thenReturn(user2);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                mealService.findMeal(meal.getId(), authentication));
    }

    @Test
    void findAllMeals_withDate_shouldReturnFilteredMeals() {
        // Arrange
        LocalDate date = LocalDate.now();
        User user = createTestUser();
        Meal meal = createTestMeal();

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userService.findByUsername(user.getEmail())).thenReturn(user);
        when(mealRepository.findAllByUserAndDate(user, date)).thenReturn(List.of(meal));

        // Act
        List<Meal> result = mealService.findAllMeals(date, authentication);

        // Assert
        assertEquals(1, result.size());
        assertEquals(meal.getId(), result.get(0).getId());
    }
}