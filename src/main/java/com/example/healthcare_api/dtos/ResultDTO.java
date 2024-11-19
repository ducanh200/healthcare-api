package com.example.healthcare_api.dtos;

import lombok.Data;

@Data
public class ResultDTO {
    private Long id;
    private String requestTest;
    private Double expense;
    private String diagnoseEnd;
    private Long bookingId;
    private Long doctorId;

    private BookingDTO booking;
    private DoctorDTO doctor;
}
