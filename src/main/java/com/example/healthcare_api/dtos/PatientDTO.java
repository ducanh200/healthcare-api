package com.example.healthcare_api.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class PatientDTO {

    private Long id;
    @Size(min = 3,message = "Patient's name must be at least 3 characters")
    private String name;
    private String email;
    @Size(min = 6,message = "Password must be at least 6 characters")
    private String password;
    private String gender;
    private Date birthday;
    private String phonenumber;
    private String address;
    private String city;
    private String role;

}
