package com.example.UserManagementSystem.controller;

import com.example.UserManagementSystem.dto.UserRequest;
import com.example.UserManagementSystem.dto.UserResponse;
import com.example.UserManagementSystem.resultGenericClass.GenericResponse;
import com.example.UserManagementSystem.service.userService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class userController2 {
    @Autowired
    private userService userService;

   /* @GetMapping("/admin")
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


        Page<UserResponse> userPage = userService.getAllUsers(page, size, order, uniqueId, userName, email, field, role);
        List<UserResponse> users = userPage.getContent();

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("sortField", field);
        model.addAttribute("sortDirection", order);

        return "admin";

    }*/
    @GetMapping
    public GenericResponse<List<UserResponse>> fetchAdmin(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestParam(defaultValue = "desc") String order,
                                                          @RequestParam(defaultValue = "uniqueId") String field,
                                                          @RequestParam(required = false) String uniqueId,
                                                          @RequestParam(required = false) String userName,
                                                          @RequestParam(required = false) String email,
                                                          @RequestParam(required = false) String role
    ){

        Page<UserResponse> userResponses= userService.getAllUsers(page,size,order,uniqueId,userName,email,field,role);
        return GenericResponse.success(userResponses.getContent(),"Data fetched Successfully");
    }


    //Create user with role
    @PostMapping("/api/users")
    public GenericResponse<UserResponse> createAdmin( @Valid @RequestBody UserRequest userRequest){
        System.out.println("Received UserRequest: " + userRequest);
        UserResponse userResponse =  userService.createAndUpdateUser(userRequest);
        if(userRequest.uniqueId()==null) {
                return GenericResponse.success(userResponse, userRequest.role().toUpperCase() + " created successfully");
        }
        else {
                return GenericResponse.success(userResponse, userRequest.role().toUpperCase() + " updated successfully");
            }
    }



    @DeleteMapping("/admin/{uniqueId}")  // Correct URL pattern
    public GenericResponse<String> deleteAdmin(@PathVariable Long uniqueId) {
        String msg = userService.deleteAdmin(uniqueId);
        return GenericResponse.success(msg, "Admin deleted successfully!");
    }

}
