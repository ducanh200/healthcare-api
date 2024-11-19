package com.example.healthcare_api.repositories;

import com.example.healthcare_api.entities.Device;
import com.example.healthcare_api.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Long> {
    @Query("SELECT DISTINCT d FROM Device d JOIN FETCH d.department")
    List<Device> findAllWithDepartment();
}
