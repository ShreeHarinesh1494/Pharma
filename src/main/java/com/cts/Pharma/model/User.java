package com.cts.Pharma.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class User
{
    @Id
    private String userId;
    private String name;
    private String role;
    private String emailId;
    private String password;
    private String phoneNumber;
    private String passwordResetToken;
    private LocalDateTime passwordTokenExpiry;
    private String usernameResetToken;
    private LocalDateTime usernameTokenExpiry;
}
