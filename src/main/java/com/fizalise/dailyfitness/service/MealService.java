package com.fizalise.dailyfitness.service;

import com.fizalise.dailyfitness.entity.Meal;
import com.fizalise.dailyfitness.entity.User;
import com.fizalise.dailyfitness.exception.ResourceNotFoundException;
import com.fizalise.dailyfitness.repository.MealRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Сервис приема пищи")
public class MealService {
    public static final int pageSize = 5;
    private final MealRepository mealRepository;
    private final UserService userService;
    private final AuthService authService;
    public Page<Meal> findAllMeals(Integer page,
                                   Authentication authentication) {
        User user = findUser(authentication);
        return mealRepository.findAllByUser(user, getPageRequest(page));
    }
    public List<Meal> findAllMeals(LocalDate date, Authentication authentication) {
        User user = findUser(authentication);
        return mealRepository.findAllByUserAndDate(user, date);
    }
    public Page<Meal> findAllMeals(LocalDate date, Integer page,
                                   Authentication authentication) {
        User user = findUser(authentication);
        return mealRepository.findAllByUserAndDate(user, date, getPageRequest(page));
    }
    public Meal findMeal(Long id, Authentication authentication) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        if (!isLegalAccess(meal, authentication)) {
            log.info("Попытка доступа к чужой задаче от пользователя {}", authentication.getName());
            throw new ResourceNotFoundException();
        }
        return meal;
    }
    @Transactional
    public Meal saveMeal(Meal meal) {
        mealRepository.save(meal);
        log.info("Сохранен прием пищи: {}", meal);
        return meal;
    }
    @Transactional
    public void removeMeal(Long id, Authentication authentication) {
        Meal meal = findMeal(id, authentication);
        mealRepository.delete(meal);
        log.info("Удален прием пищи: {}", meal);
    }
    private boolean isLegalAccess(Meal meal, Authentication authentication) {
        User user = findUser(authentication);
        return authService.hasAdminRole(authentication) || meal.getUser().equals(user);
    }
    private User findUser(Authentication authentication) {
        return userService.findByUsername(authentication.getName());
    }
    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "date"));
    }
}
