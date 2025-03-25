package com.fizalise.dailyfitness.repository;

import com.fizalise.dailyfitness.entity.Meal;
import com.fizalise.dailyfitness.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findAllByUserAndDate(User user, LocalDate date);
    Page<Meal> findAllByUser(User user, Pageable pageable);
    Page<Meal> findAllByUserAndDate(User user, LocalDate date, Pageable pageable);
}
