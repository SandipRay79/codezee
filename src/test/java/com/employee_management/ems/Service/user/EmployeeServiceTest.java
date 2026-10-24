package com.employee_management.ems.Service.user;

import com.employee_management.ems.Entity.Employee;
import com.employee_management.ems.Repository.DepartmentRepository;
import com.employee_management.ems.Repository.EmployeeRepository;
import com.employee_management.ems.Repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private EmployeeService employeeService;

    @Test
    void registerUser_encodesPasswordBeforeSaving() {
        Employee employee = new Employee();
        employee.setPassword("secret");
        employee.setRoleId(1);
        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Employee saved = employeeService.registerUser(employee);

        assertEquals("hashed", saved.getPassword());
    }

    @Test
    void loadUserByUsername_unknownEmail_throwsUsernameNotFound() {
        when(employeeRepository.findByEmail("nobody@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> employeeService.loadUserByUsername("nobody@example.com"));
    }

    @Test
    void getAllEmployees_returnsRepositoryResults() {
        when(employeeRepository.findAll()).thenReturn(List.of(new Employee()));

        assertEquals(1, employeeService.getAllEmployees().size());
    }
}
