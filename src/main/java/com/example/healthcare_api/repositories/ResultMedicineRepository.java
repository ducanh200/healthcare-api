package com.example.healthcare_api.repositories;

import com.example.healthcare_api.entities.ResultMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultMedicineRepository extends JpaRepository<ResultMedicine,Long> {
    List<ResultMedicine> findByResultId(Long resultId);
}