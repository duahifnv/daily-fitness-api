package com.fizalise.dailyfitness.repository;

import com.fizalise.dailyfitness.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    int PAGE_SIZE = 5;
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
