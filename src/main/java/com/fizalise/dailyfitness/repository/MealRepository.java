package com.fizalise.dailyfitness.repository;

import com.fizalise.dailyfitness.entity.Meal;
import com.fizalise.dailyfitness.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    @Query("from Meal m where m.user = ?1")
    Page<Meal> findAllByUser(User user, Pageable pageable);
}
