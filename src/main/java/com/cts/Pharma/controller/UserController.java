package com.cts.Pharma.controller;

import com.cts.Pharma.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> register(@RequestBody User user) {
        return new ResponseEntity<>(userService.register(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return new ResponseEntity<>(userService.login(request.getName(), request.getPassword()), HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return new ResponseEntity<>(userService.forgotPassword(request.getEmailId()), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return new ResponseEntity<>(userService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        ), HttpStatus.OK);
    }

    @PostMapping("/forgot-username")
    public ResponseEntity<String> forgotUsername(@RequestBody ForgotUsernameRequest request) {
        return new ResponseEntity<>(userService.forgotUsername(request.getEmailId()), HttpStatus.OK);
    }

    @PostMapping("/reset-username")
    public  ResponseEntity<String> resetUsername(@RequestBody ResetUsernameRequest request) {
        return new ResponseEntity<>(userService.resetUsername(
                request.getToken(),
                request.getNewUsername()
        ), HttpStatus.OK);
    }
}
