package com.fizalise.dailyfitness.service;

import com.fizalise.dailyfitness.entity.Dish;
import com.fizalise.dailyfitness.repository.DishRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishService {
    @Value("${pagination.page-size}")
    private int pageSize;
    private final DishRepository dishRepository;
    public Page<Dish> findAllDishes(@Min(0) Integer page) {
        return dishRepository.findAll(getPageRequest(page));
    }
    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "name"));
    }
}
