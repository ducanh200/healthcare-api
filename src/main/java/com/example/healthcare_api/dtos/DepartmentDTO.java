package com.example.healthcare_api.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentDTO {
    private Long id;
    @Size(min = 3,message = "Department's name must be at least 3 characters")
    private String name;
    private Integer maxBooking;
    private String description;
    private String thumbnail;

    public DepartmentDTO() {

    }
}
