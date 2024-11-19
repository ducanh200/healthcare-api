package com.example.healthcare_api.repositories;

import com.example.healthcare_api.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result,Long> {
    @Query("SELECT r FROM Result r JOIN r.booking b WHERE b.status = 4")
    List<Result> findResultsByBookingStatus();
    @Query("SELECT r FROM Result r WHERE r.booking.patient.id = :patientId AND r.booking.status = 4")
    List<Result> findByPatientId(@Param("patientId") Long patientId);
    List<Result> findByBookingId(Long bookingId);

}
