package com.cts.Pharma.service;

import com.cts.Pharma.exception.EmailNotFoundException;
import com.cts.Pharma.exception.InvalidCredentialsException;
import com.cts.Pharma.exception.InvalidTokenException;
import com.cts.Pharma.exception.NoUserFoundException;
import com.cts.Pharma.model.User;
import com.cts.Pharma.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

        Optional<User> user1 = userRepository.findByName(name);
        if(user1.isPresent())
        {
            User user = user1.get();
            // compare raw password with hashed password
            if (passwordEncoder.matches(password, user.getPassword())) {
                return "Login Successful";
            } else {
                throw new NoUserFoundException("Invalid Credentials");
            }
        }
        else
        {
            throw new NoUserFoundException("User Not Found");
        }



    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String forgotPassword(String emailId) {

        User user = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new EmailNotFoundException("Email Not Found"));

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
                .orElseThrow(() -> new InvalidTokenException("Invalid Token"));

        if (user.getPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token Expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordTokenExpiry(null);

        userRepository.save(user);

        return "Password Reset Successful";
    }

    public String forgotUsername(String emailId) {

        User user = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new EmailNotFoundException("Email Not Found"));

        String token = UUID.randomUUID().toString();

        user.setUsernameResetToken(token);
        user.setUsernameTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        String body = "Your username reset token is: " + token;

        emailService.sendEmail(emailId, "Username Reset Token", body);

        return "Username reset token sent to email";
    }

    public String resetUsername(String token, String newUsername) {

        User user = userRepository.findByUsernameResetToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid Token"));

        if (user.getUsernameTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token Expired");
        }

        if (userRepository.findByName(newUsername).isPresent()) {
            throw new InvalidCredentialsException("Username already taken");
        }

        user.setName(newUsername);
        user.setUsernameResetToken(null);
        user.setUsernameTokenExpiry(null);

        userRepository.save(user);

        return "Username Reset Successful";
    }
}
