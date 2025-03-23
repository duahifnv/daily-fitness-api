package com.fizalise.dailyfitness.repository;

import com.fizalise.dailyfitness.entity.Portion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortionRepository extends JpaRepository<Portion, Long> {
}
