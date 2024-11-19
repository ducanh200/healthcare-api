package com.example.healthcare_api.dtos;

import lombok.Data;

import java.sql.Time;

@Data
public class ShiftDTO {
    private Long id;
    private Time time;
    private String session;
    private int status;
}
