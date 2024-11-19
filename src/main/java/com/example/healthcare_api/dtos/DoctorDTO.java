package com.example.healthcare_api.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    @Size(min = 3,message = "Doctor's name must be at least 3 characters")
    private String name;
    private String email;
    @Size(min = 6,message = "Password must be at least 6 characters")
    private String password;
    private String thumbnail;
    private String phonenumber;
    private String role;
    private Long departmentId;

    private DepartmentDTO department;

}
