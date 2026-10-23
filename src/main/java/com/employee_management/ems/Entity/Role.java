package com.employee_management.ems.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="role")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // creates protected no-arg constructor
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String title;
    @Column(nullable = false)
    @NotBlank(message = "Salary min range cannot be blank")
    private Double salary_min_range;
    @Column(nullable = false)
    @NotBlank(message = "Salary max range cannot be blank")
    private Double salary_max_range;

}
