package com.example.healthcare_api.repositories;

import com.example.healthcare_api.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByPatientId(Long id);

    List<Booking> findByDepartmentId(Long id);

    List<Booking> findByDate(Date date);
    List<Booking> findByDateBetween(Date startDate, Date endDate);


    @Query("SELECT b FROM Booking b WHERE MONTH(b.date) = :month AND YEAR(b.date) = :year")
    List<Booking> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    List<Booking> findByStatus(int status);

}
