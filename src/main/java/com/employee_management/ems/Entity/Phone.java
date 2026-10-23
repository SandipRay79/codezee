package com.employee_management.ems.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="phone")
@Getter
@Setter
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(name="is_primary", nullable = true)
    private boolean isPrimary;

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = true, updatable = true)
    private Employee employee;
}
