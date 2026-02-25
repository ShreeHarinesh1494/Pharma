package com.cts.Pharma.controller;

import com.cts.Pharma.dto.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cts.Pharma.model.User;
import com.cts.Pharma.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return userService.login(request.getName(), request.getPassword());
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return userService.forgotPassword(request.getEmailId());
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        return userService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );
    }

    @PostMapping("/forgot-username")
    public String forgotUsername(@RequestBody ForgotUsernameRequest request) {
        return userService.forgotUsername(request.getEmailId());
    }

    @PostMapping("/reset-username")
    public String resetUsername(@RequestBody ResetUsernameRequest request) {
        return userService.resetUsername(
                request.getToken(),
                request.getNewUsername()
        );
    }
}
