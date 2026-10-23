package com.employee_management.ems.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="employee")
@NoArgsConstructor(access = AccessLevel.PUBLIC) // creates protected no-arg constructor
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(nullable = false)
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @Column(nullable= false)
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @Column(nullable = false, unique = true, length=50)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    @Column(nullable = false)
    @Size(message = "Password must be at least 6 characters")
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @Column(name = "department_id", nullable = false)
    private Integer departmentId;
    @ManyToOne
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;
    @Column(nullable = false)
    @NotNull(message = "Hire date cannot be blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    @Column(name="role_id",nullable = false)
    @NotNull(message = "Job title cannot be blank")
    private Integer roleId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", insertable=false, updatable=false)
    private Role role;
    @Column(nullable = false)
    @NotNull(message = "DOB cannot be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    @Column(nullable = false)
    @NotBlank(message = "Gender cannot be blank")
    private String gender;
    @Column(nullable = false,columnDefinition = "Text") // text field
    @NotBlank(message = "Address cannot be blank")
    private String address;
    @Column(nullable = false)
    private Integer managerId;
    @Transient
    private Integer primaryPhoneIndex; // Not stored in DB, only for form
    //@Transient
    //private Integer departmentId;
//    @Transient
//    private Integer roleId;

    @OneToMany(mappedBy="employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Phone> phones = new ArrayList<>();

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + Id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
