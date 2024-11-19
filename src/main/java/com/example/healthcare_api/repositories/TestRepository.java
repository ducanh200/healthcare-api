package com.example.healthcare_api.repositories;

import com.example.healthcare_api.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test,Long> {
    List<Test> findByResultId(Long resultId);
}
