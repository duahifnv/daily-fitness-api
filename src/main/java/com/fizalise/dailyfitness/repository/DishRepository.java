package com.fizalise.dailyfitness.repository;

import com.fizalise.dailyfitness.entity.Dish;
import com.fizalise.dailyfitness.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
}
