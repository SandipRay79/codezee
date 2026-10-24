package com.employee_management.ems.Service.user;

import com.employee_management.ems.Entity.Department;
import com.employee_management.ems.Entity.Employee;
import com.employee_management.ems.Repository.DepartmentRepository;
import com.employee_management.ems.Repository.EmployeeRepository;
import com.employee_management.ems.Repository.RoleRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder,DepartmentRepository departmentRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepository=departmentRepository;
        this.roleRepository=roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee emp = employeeRepository.findByEmail(email);

        if (emp == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.withUsername(emp.getEmail())
                .password(emp.getPassword())
                .roles(emp.getRole().getTitle().toUpperCase())  // example: "ADMIN"
                .build();
    }

    public Employee registerUser(Employee employee) {
      String encodedPassword = passwordEncoder.encode(employee.getPassword());
      employee.setPassword(encodedPassword);
        //employee.setDepartment(departmentRepository.findById(employee.getDepartmentId()).orElse(null));
        employee.setRole(roleRepository.findById(employee.getRoleId()).orElse(null));
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    public Employee fetchEmployeeById(Integer id) {
       Employee employee = employeeRepository.findById(id).orElse(null);
        // determine primary phone position (optional if stored already)
        for (int i = 0; i < employee.getPhones().size(); i++) {
            if (Boolean.TRUE.equals(employee.getPhones().get(i).isPrimary())) {
                employee.setPrimaryPhoneIndex(i);
                break;
            }
        }
        return employee;
    }

    public boolean updateEmployee(Employee employee, Integer Id) {
       Employee existingEmployee = employeeRepository.findById(Id).orElseThrow(() -> new RuntimeException("Employee not found"));
       existingEmployee.setFirstName(employee.getFirstName());
       existingEmployee.setLastName(employee.getLastName());
       existingEmployee.setEmail(employee.getEmail());
       existingEmployee.setPhones(employee.getPhones());
       existingEmployee.setAddress(employee.getAddress());
       existingEmployee.setDob(employee.getDob());
       existingEmployee.setGender(employee.getGender());
       existingEmployee.setHireDate(employee.getHireDate());
       existingEmployee.setDepartmentId(employee.getDepartmentId());
       existingEmployee.setManagerId(employee.getManagerId());
       existingEmployee.setRoleId(employee.getRoleId());
       employeeRepository.save(existingEmployee);
       return true;
    }

    public boolean deleteEmployeeById(int Id) {
        employeeRepository.deleteById(Id);
        return true;
    }

    public Page<Employee> searchEmployees(String name, Integer departmentId, int page, int size) {
        String keyword = (name == null || name.isBlank()) ? null : name.trim().toLowerCase();
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50); // cap page size
        Pageable pageable = PageRequest.of(safePage, safeSize,
                Sort.by("lastName").ascending().and(Sort.by("firstName").ascending()));

        Specification<Employee> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null) {
                String pattern = "%" + keyword + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern),
                        cb.like(cb.lower(root.get("lastName")), pattern)));
            }
            if (departmentId != null) {
                predicates.add(cb.equal(root.get("departmentId"), departmentId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return employeeRepository.findAll(spec, pageable);
    }
}
