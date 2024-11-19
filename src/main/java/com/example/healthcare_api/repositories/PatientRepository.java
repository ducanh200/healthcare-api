package com.example.healthcare_api.repositories;

import com.example.healthcare_api.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    List<Patient> findAllByNameContaining(String name);

    Patient findByEmail(String email);
}
