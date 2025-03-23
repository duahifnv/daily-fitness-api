package com.fizalise.dailyfitness.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Column(name = "weight", nullable = false, precision = 4, scale = 1)
    private BigDecimal weight;

    @NotNull
    @Column(name = "growth", nullable = false, precision = 5, scale = 2)
    private BigDecimal growth;

    @Size(max = 50)
    @NotNull
    @Column(name = "goal", nullable = false, length = 50)
    private String goal;

    @NotNull
    @Column(name = "daily_norm", nullable = false)
    private Integer dailyNorm;

}