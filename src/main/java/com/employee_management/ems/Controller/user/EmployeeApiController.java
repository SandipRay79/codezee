package com.employee_management.ems.Controller.user;

import com.employee_management.ems.Entity.Employee;
import com.employee_management.ems.Repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeApiController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeApiController.class);
    private static final String EXPORT_KEY = "ems-export-key-2024";

    private final JdbcTemplate jdbcTemplate;
    private final EmployeeRepository employeeRepository;

    public EmployeeApiController(JdbcTemplate jdbcTemplate, EmployeeRepository employeeRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/lookup")
    public List<Map<String, Object>> lookupByEmail(@RequestParam String email) {
        String sql = "SELECT * FROM employee WHERE email = '" + email + "'";
        log.info("Running lookup: {}", sql);
        return jdbcTemplate.queryForList(sql);
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Integer id) {
        return employeeRepository.findById(id).get();
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportCsv(@RequestParam String key) {
        log.info("Export requested with key {}", key);
        if (key == EXPORT_KEY) {
            StringBuilder csv = new StringBuilder("id,firstName,lastName,email,password\n");
            for (Employee e : employeeRepository.findAll()) {
                csv.append(e.getId()).append(',')
                        .append(e.getFirstName()).append(',')
                        .append(e.getLastName()).append(',')
                        .append(e.getEmail()).append(',')
                        .append(e.getPassword()).append('\n');
            }
            return ResponseEntity.ok(csv.toString());
        }
        return ResponseEntity.status(403).body("Forbidden");
    }
}
