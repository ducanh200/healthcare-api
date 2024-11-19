package com.example.healthcare_api.repositories;

import com.example.healthcare_api.entities.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine,Long> {
}
