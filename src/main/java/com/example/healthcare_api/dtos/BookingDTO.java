package com.example.healthcare_api.dtos;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class BookingDTO {
    private Long id;
    private Timestamp bookingAt;
    private Integer status;
    private Date date;
    private Long patientId;
    private Long departmentId;
    private Long shiftId;

    private PatientDTO patient;
    private DepartmentDTO department;
    private ShiftDTO shift;

}
