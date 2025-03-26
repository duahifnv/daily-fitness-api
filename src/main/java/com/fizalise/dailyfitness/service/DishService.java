package com.fizalise.dailyfitness.service;

import com.fizalise.dailyfitness.entity.Dish;
import com.fizalise.dailyfitness.exception.ResourceNotFoundException;
import com.fizalise.dailyfitness.repository.DishRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Сервис блюд")
public class DishService {
    public final static int pageSize = 5;
    private final DishRepository dishRepository;
    public Page<Dish> findAllDishes(Integer page) {
        return dishRepository.findAll(getPageRequest(page));
    }
    public Dish findDish(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }
    @Transactional
    public Dish saveDish(Dish dish) {
        dishRepository.save(dish);
        log.info("Сохранено блюдо: {}", dish);
        return dish;
    }
    @Transactional
    public void removeDish(Long id) {
        Dish dish = findDish(id);
        dishRepository.delete(dish);
        log.info("Удалено блюдо: {}", dish);
    }
    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "name"));
    }
}
