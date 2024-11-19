package com.example.healthcare_api.repositories;

import com.example.healthcare_api.entities.Booking;
import com.example.healthcare_api.entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift,Long> {
    @Query("SELECT b FROM Booking b WHERE b.date = :date AND b.department.id = :departmentId")
    List<Booking> findByDateAndDepartment(@Param("date") Date date, @Param("departmentId") Long departmentId);

    @Query("SELECT d.maxBooking FROM Department d WHERE d.id = :departmentId")
    int findMaxBookingByDepartmentId(@Param("departmentId") Long departmentId);
}
