package com.example.healthcare_api.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeviceDTO {
    private Long id;
    @Size(min = 3,message = "Doctor's name must be at least 3 characters")
    private String name;
    private String description;
    private Double expense;
    private Long departmentId;

    private DepartmentDTO department;
}
