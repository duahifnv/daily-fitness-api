package com.fizalise.dailyfitness.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "dishes")
@Data
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(name = "cals", nullable = false)
    private Integer cals;

    @NotNull
    @Column(name = "proteins", nullable = false)
    private Integer proteins;

    @NotNull
    @Column(name = "fats", nullable = false)
    private Integer fats;

    @NotNull
    @Column(name = "carbs", nullable = false)
    private Integer carbs;

}