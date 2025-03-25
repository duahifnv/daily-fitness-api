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

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Сервис приема пищи")
public class MealService {
    @Value("${pagination.page-size}")
    private int pageSize;
    private final MealRepository mealRepository;
    private final UserService userService;
    public Page<Meal> findAllMeals(@Min(0) Integer page, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return mealRepository.findAllByUser(user, getPageRequest(page));
    }
    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id"));
    }
    public Meal findMeal(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }
    @Transactional
    public Meal saveMeal(Meal meal) {
        mealRepository.save(meal);
        log.info("Сохранен прием пищи: {}", meal);
        return meal;
    }
    @Transactional
    public void removeMeal(Long id) {
        Meal meal = findMeal(id);
        mealRepository.delete(meal);
        log.info("Удален прием пищи: {}", meal);
    }
}
