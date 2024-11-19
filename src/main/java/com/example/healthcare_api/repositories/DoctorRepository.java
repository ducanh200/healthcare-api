package com.example.healthcare_api.repositories;

import com.example.healthcare_api.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {

    List<Doctor> findByDepartmentId(@Param("departmentId") Long departmentId);
    Doctor findByEmail(String email);

}
