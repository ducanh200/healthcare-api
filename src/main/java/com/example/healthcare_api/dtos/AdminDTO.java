package com.example.healthcare_api.dtos;

import lombok.Data;

@Data
public class AdminDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
}
