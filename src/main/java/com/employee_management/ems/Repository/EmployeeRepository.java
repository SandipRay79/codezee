package com.employee_management.ems.Repository;

import com.employee_management.ems.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByEmail(String email);

    @Query("""
            SELECT e FROM Employee e
            WHERE (:name IS NULL
                   OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
                   OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:departmentId IS NULL OR e.departmentId = :departmentId)
            """)
    Page<Employee> search(@Param("name") String name,
                          @Param("departmentId") Integer departmentId,
                          Pageable pageable);
}
