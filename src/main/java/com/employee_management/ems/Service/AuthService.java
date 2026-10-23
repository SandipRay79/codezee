package com.employee_management.ems.Service;

import com.employee_management.ems.DTO.LoginDTO;
import com.employee_management.ems.Entity.Employee;
import com.employee_management.ems.Repository.EmployeeRepository;
import jakarta.validation.constraints.Email;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final EmployeeRepository employeeRepository;

    public AuthService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
//    public Employee authenticate(String email, String password) {
//        return employeeRepository.findByEmail(email);
//    }

//    public AuthService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
//        this.employeeRepository = employeeRepository;
//        this.passwordEncoder = passwordEncoder;
//    }


}
