package com.example.healthcare_api.dtos;

import lombok.Data;

@Data
public class EmailDTO {
    private String to;
    private String subject;
    private String message;
}
