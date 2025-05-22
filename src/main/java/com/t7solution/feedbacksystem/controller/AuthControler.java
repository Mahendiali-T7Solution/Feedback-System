package com.t7solution.feedbacksystem.controller;

import com.t7solution.feedbacksystem.dto.RoleDto;
import com.t7solution.feedbacksystem.dto.UserDto;
import com.t7solution.feedbacksystem.model.User;
import com.t7solution.feedbacksystem.service.AuthService;
import com.t7solution.feedbacksystem.service.RoleService;
import com.t7solution.feedbacksystem.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControler {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/home")
    public String home() {
        return "home Page";
    }

    @PostMapping("/auth/signup")
    public String register(@RequestBody UserDto user) {
        System.out.println("user information: " + user);
        RoleDto roleDto = roleService.findRoleById(user.getRoleId());
        userService.saveUser(user, roleDto);
        return "user Registered sucessefully";
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody UserDto user , HttpServletResponse response) {
        return authService.authenticate(user);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Mahendiali";
    }
}
