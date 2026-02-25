package com.cts.Pharma.service;

import com.cts.Pharma.model.User;
import com.cts.Pharma.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // SIGN UP
    public String register(User user) {

        // encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return "User Registered Successfully";
    }

    // LOGIN
    public String login(String name, String password) {

        User user = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // compare raw password with hashed password
        if (passwordEncoder.matches(password, user.getPassword())) {
            return "Login Successful";
        } else {
            return "Invalid Credentials";
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
