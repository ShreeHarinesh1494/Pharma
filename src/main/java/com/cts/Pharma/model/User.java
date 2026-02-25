package com.cts.Pharma.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

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
}
