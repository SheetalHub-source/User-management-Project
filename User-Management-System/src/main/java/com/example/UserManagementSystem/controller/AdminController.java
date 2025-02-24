package com.example.UserManagementSystem.controller;

import com.example.UserManagementSystem.dto.UserRequest;
import com.example.UserManagementSystem.dto.UserResponse;
import com.example.UserManagementSystem.service.CategoryService;
import com.example.UserManagementSystem.service.ProductService;
import com.example.UserManagementSystem.service.userService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private userService usersService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "RegistrationPage";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/home")
    public String homePage(){
        return "home";
    }

    @GetMapping("/admin")
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "uniqueId") String field,
            @RequestParam(required = false) String uniqueId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "ADMIN") String role,  // Default role to ADMIN
            Model model) {


        Page<UserResponse> userPage = usersService.getAllUsers(page, size, order, uniqueId, userName, email, field, role);
        List<UserResponse> users = userPage.getContent();

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("sortField", field);
        model.addAttribute("sortDirection", order);

        return "admin";

    }







}
