package com.cts.Pharma.dto;

import lombok.Data;

@Data
public class ResetUsernameRequest
{
    private String token;
    private String newUsername;
}
