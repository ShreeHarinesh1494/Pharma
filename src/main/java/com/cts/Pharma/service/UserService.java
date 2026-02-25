package com.cts.Pharma.service;

import com.cts.Pharma.model.User;
import com.cts.Pharma.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

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

    public String forgotPassword(String emailId) {

        System.out.println("Incoming Email: " + emailId);
        User user = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String token = UUID.randomUUID().toString();

        user.setPasswordResetToken(token);
        user.setPasswordTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        String body = "Your password reset token is: " + token;

        emailService.sendEmail(emailId, "Password Reset Token", body);

        return "Reset token sent to email";
    }

    public String resetPassword(String token, String newPassword) {

        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            return "Token expired";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordTokenExpiry(null);

        userRepository.save(user);

        return "Password reset successful";
    }

    public String forgotUsername(String emailId) {

        User user = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String token = UUID.randomUUID().toString();

        user.setUsernameResetToken(token);
        user.setUsernameTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        String subject = "Username Reset Token";

        String body = "Your username reset token is: " + token
                + "\n\nThis token will expire in 15 minutes.";

        emailService.sendEmail(emailId, subject, body);

        return "Username reset token sent to email";
    }

    public String resetUsername(String token, String newUsername) {

        User user = userRepository.findByUsernameResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getUsernameTokenExpiry().isBefore(LocalDateTime.now())) {
            return "Token expired";
        }

        // check if username already exists
        if (userRepository.findByName(newUsername).isPresent()) {
            return "Username already taken";
        }

        user.setName(newUsername);

        // clear token after success
        user.setUsernameResetToken(null);
        user.setUsernameTokenExpiry(null);

        userRepository.save(user);

        return "Username reset successful";
    }
}
