package com.employee_management.ems.Controller.user;

import com.employee_management.ems.Entity.Department;
import com.employee_management.ems.Entity.Employee;
import com.employee_management.ems.Entity.Phone;
import com.employee_management.ems.Entity.Role;
import com.employee_management.ems.Service.user.DepartmentService;
import com.employee_management.ems.Service.user.EmployeeService;
import com.employee_management.ems.Service.user.RoleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.Attribute;
import java.util.List;

@Controller
public class EmployeeController {

    EmployeeService empService;
    RoleService roleService;
    DepartmentService departmentService;
    Employee employee;
    public EmployeeController(EmployeeService empService, DepartmentService departmentService,RoleService roleService) {
        this.empService = empService;
        this.departmentService = departmentService;
        this.roleService = roleService;
    }
    @GetMapping("/register")
    public String showRegister(Model model) {
       Employee employee = new Employee();
       // Initialize phone list so thymeleaf doesn't break on phones[0], phones[1]
       employee.getPhones().add(new Phone());
       employee.getPhones().add(new Phone());
       model.addAttribute("employee", employee);
       model.addAttribute("departments", departmentService.getAllDepartments());
       model.addAttribute("roles", roleService.getAllRoles());
       model.addAttribute("pageTitle", "Registration");
       model.addAttribute("content", "register");
       employee.setPrimaryPhoneIndex(0);
       return "layout";
    }

    @PostMapping("/register")
    public String userRegistration(@ModelAttribute Employee employee) {

        // Set employee reference for each phone
        if (employee.getPhones() != null) {
            for (int i = 0; i < employee.getPhones().size(); i++) {
                Phone phone = employee.getPhones().get(i);
                phone.setEmployee(employee);

                // set primary phone based on index from form
                if (employee.getPrimaryPhoneIndex() != null && employee.getPrimaryPhoneIndex() == i) {
                    phone.setPrimary(true);
                } else {
                    phone.setPrimary(false);
                }
            }
        }

        empService.registerUser(employee);
        return "redirect:/employeelist";
    }

    @GetMapping("/employeelist")
    public String getEmployeeList(Model model) {
        model.addAttribute("employees", empService.getAllEmployees());
        model.addAttribute("pageTitle", "EmployeeList");
        model.addAttribute("content", "user/employeeList");
        return "layout";
    }

    

    @GetMapping("/employee/edit/{id}")
    public String getEmployee(@PathVariable Integer id,@ModelAttribute Employee employee, Model model) {
       model.addAttribute("employee",empService.fetchEmployeeById(id));
       model.addAttribute("departments", departmentService.getAllDepartments());
       model.addAttribute("roles", roleService.getAllRoles());
       model.addAttribute("pageTitle", "Edit");
       model.addAttribute("content", "user/employeeEdit");
        // Set employee reference for each phone
        if (employee.getPhones() != null) {
            for (int i = 0; i < employee.getPhones().size(); i++) {
                Phone phone = employee.getPhones().get(i);
                phone.setEmployee(employee);

                // set primary phone based on index from form
                if (employee.getPrimaryPhoneIndex() != null && employee.getPrimaryPhoneIndex() == i) {
                    phone.setPrimary(true);
                } else {
                    phone.setPrimary(false);
                }
            }
        }
        //return "/user/employeeEdit";
        return "layout";
    }

    @PostMapping("/employee/edit/{id}")
    public String updateEmployee(@Valid @ModelAttribute Employee employee, BindingResult bindingresult, @PathVariable("id") Integer Id, RedirectAttributes redirectAttributes, Model model) {
        if(bindingresult.hasErrors()) {
            model.addAttribute("content", "user/employeeEdit");

            return "layout";
        }

        boolean isUpdate =  empService.updateEmployee(employee,Id);
        if (isUpdate == true) {
            redirectAttributes.addFlashAttribute("msg", "Employee updated successfully");
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("pageTitle", "Edit");
            return "redirect:/employeelist";
        }
        else {
            redirectAttributes.addFlashAttribute("msg", "Error occured during employee updation");
            return "redirect:/employeelist";
        }
    }

    @GetMapping("/employee/delete/{id}")
    public String deleteRecord(@PathVariable("id") int Id, RedirectAttributes redirectAttributes)
    {
        boolean success = empService.deleteEmployeeById(Id);
        if (success) {
            redirectAttributes.addFlashAttribute("msg", "Employee deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("msg", "Error, Employee not deleted.");
        }
        return "redirect:/employeelist";
    }
}
