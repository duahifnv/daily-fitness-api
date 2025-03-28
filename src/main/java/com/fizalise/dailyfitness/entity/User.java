package com.fizalise.dailyfitness.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
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
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(max = 500)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Column(name = "weight", nullable = false, precision = 4, scale = 1)
    private BigDecimal weight;

    @NotNull
    @Column(name = "growth", nullable = false, precision = 5, scale = 2)
    private BigDecimal growth;

    @NotNull
    @Column(name = "daily_norm", nullable = false)
    private Integer dailyNorm;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "goal", nullable = false)
    private Goal goal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activity", nullable = false)
    private Activity activity;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}