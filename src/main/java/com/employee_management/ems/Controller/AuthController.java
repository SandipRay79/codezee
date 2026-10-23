package com.employee_management.ems.Controller;

import com.employee_management.ems.DTO.LoginDTO;
import com.employee_management.ems.Entity.Employee;
import com.employee_management.ems.Service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String showWelcomePage(Model model) {
       model.addAttribute("pageTitle","EMS");
       model.addAttribute("content", "welcome");
       return "layout";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        //return "login";
        model.addAttribute("pageTitle","Login");
        model.addAttribute("content", "login");
        return "layout";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

//    @PostMapping("/authenticateTheUser")
//    public String authenticate(@ModelAttribute LoginDTO loginDTO,
//                               Model model) {
//        System.out.println("Inside authenticateTheUser method");
//        Employee emp = authService.authenticate(loginDTO);
//        logger.info("Employee returned from service: {}", emp);
//
//        if (emp == null) {
//            model.addAttribute("error", "Invalid email or password");
//            return "login";
//        }
//
//        model.addAttribute("name", emp.getFirstName());
//        return "success";
//    }

//    @GetMapping("/success")
//    public String success() {
//        return "success";
//    }
//    @GetMapping("/error")
//    public String error() {
//        return "login";
//    }
//    @GetMapping("/register")
//    public String register() {
//        return "register";
//    }
}
