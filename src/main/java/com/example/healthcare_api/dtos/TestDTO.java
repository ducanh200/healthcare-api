package com.example.healthcare_api.dtos;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TestDTO {
    private Long id;
    private String diagnose;
    private String thumbnail;
    private Timestamp testAt;
    private Double expense;
    private Long deviceId;
    private Long doctorId;
    private Long resultId;

    private DeviceDTO device;
    private DoctorDTO doctor;
    private ResultDTO result;
}
