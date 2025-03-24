package com.fizalise.dailyfitness.service;

import com.fizalise.dailyfitness.entity.Meal;
import com.fizalise.dailyfitness.repository.MealRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealService {
    @Value("${pagination.page-size}")
    private int pageSize;
    private final MealRepository mealRepository;
    public Page<Meal> findAllMeals(@Min(0) Integer page) {
        return mealRepository.findAll(getPageRequest(page));
    }
    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id"));
    }
}
