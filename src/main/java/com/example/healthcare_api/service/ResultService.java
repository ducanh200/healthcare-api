package com.example.healthcare_api.service;

import com.example.healthcare_api.dtos.BookingDTO;
import com.example.healthcare_api.dtos.DoctorDTO;
import com.example.healthcare_api.dtos.ResultDTO;
import com.example.healthcare_api.entities.*;
import com.example.healthcare_api.repositories.BookingRepository;
import com.example.healthcare_api.repositories.DoctorRepository;
import com.example.healthcare_api.repositories.MedicineRepository;
import com.example.healthcare_api.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private MedicineRepository medicineRepository;


    public List<ResultDTO> getAllResults() {
        List<Result> results = resultRepository.findAll();
        List<ResultDTO> resultDTOs = new ArrayList<>(results.size());

        results.forEach(result -> {
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setId(result.getId());
            resultDTO.setRequestTest(result.getRequestTest());
            resultDTO.setExpense(result.getExpense());
            resultDTO.setDiagnoseEnd(result.getDiagnoseEnd());
            resultDTO.setBookingId(result.getBooking().getId());
            resultDTO.setDoctorId(result.getDoctor().getId());

            BookingDTO bookingDTO = new BookingDTO();
            Booking booking = result.getBooking();
            bookingDTO.setId(booking.getId());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setShiftId(booking.getShift().getId());
            resultDTO.setBooking(bookingDTO);

            DoctorDTO doctorDTO = new DoctorDTO();
            Doctor doctor = result.getDoctor();
            doctorDTO.setId(doctor.getId());
            doctorDTO.setName(doctor.getName());
            doctorDTO.setPhonenumber(doctor.getPhonenumber());
            doctorDTO.setDepartmentId(doctor.getDepartment().getId());
            resultDTO.setDoctor(doctorDTO);

            resultDTOs.add(resultDTO);
        });

        return resultDTOs;
    }

    public ResultDTO createResult(@RequestBody ResultDTO request) {
        // Tìm phòng ban tương ứng
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + request.getBookingId()));

        // Tìm đơn thuốc tương ứng
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found with id: " + request.getDoctorId()));

        // Tạo một đối tượng Result mới
        Result result = new Result();
        result.setRequestTest(request.getRequestTest());
        result.setExpense(request.getExpense());
        result.setDiagnoseEnd(request.getDiagnoseEnd());
        result.setBooking(booking);
        result.setDoctor(doctor);

        // Lưu đối tượng Result mới vào cơ sở dữ liệu
        Result savedResult = resultRepository.save(result);

        // Tạo DTO từ đối tượng Result đã lưu
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setId(savedResult.getId());
        resultDTO.setExpense(savedResult.getExpense());
        resultDTO.setBookingId(savedResult.getBooking().getId());
        resultDTO.setDoctorId(savedResult.getDoctor().getId());

        return resultDTO;
    }
    public ResultDTO getById(Long id) {
        // Tìm kết quả theo id
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Result not found with id: " + id));

        // Tạo DTO từ kết quả đã tìm thấy
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setId(result.getId());
        resultDTO.setRequestTest(result.getRequestTest());
        resultDTO.setExpense(result.getExpense());
        resultDTO.setDiagnoseEnd(result.getDiagnoseEnd());
        resultDTO.setBookingId(result.getBooking().getId());
        resultDTO.setDoctorId(result.getDoctor().getId());

        BookingDTO bookingDTO = new BookingDTO();
        Booking booking = result.getBooking();
        bookingDTO.setId(booking.getId());
        bookingDTO.setDate(booking.getDate());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setBookingAt(booking.getBookingAt());
        bookingDTO.setDepartmentId(booking.getDepartment().getId());
        bookingDTO.setPatientId(booking.getPatient().getId());
        bookingDTO.setShiftId(booking.getShift().getId());
        resultDTO.setBooking(bookingDTO);

        DoctorDTO doctorDTO = new DoctorDTO();
        Doctor doctor = result.getDoctor();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setPhonenumber(doctor.getPhonenumber());
        doctorDTO.setDepartmentId(doctor.getDepartment().getId());
        doctorDTO.setEmail(doctor.getEmail());
        resultDTO.setDoctor(doctorDTO);


        return resultDTO;
    }
    public ResultDTO updateResult(Long id, ResultDTO request) {
        // Tìm kết quả cần cập nhật
        Result resultToUpdate = resultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Result not found with id: " + id));


        // Cập nhật thông tin của kết quả
        resultToUpdate.setRequestTest(request.getRequestTest());
        resultToUpdate.setExpense(request.getExpense());
        resultToUpdate.setDiagnoseEnd(request.getDiagnoseEnd());

        // Lưu kết quả đã cập nhật vào cơ sở dữ liệu
        Result updatedResult = resultRepository.save(resultToUpdate);

        // Tạo DTO từ kết quả đã cập nhật
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setId(updatedResult.getId());
        resultDTO.setRequestTest(updatedResult.getRequestTest());
        resultDTO.setExpense(updatedResult.getExpense());
        resultDTO.setDiagnoseEnd(updatedResult.getDiagnoseEnd());
        resultDTO.setBookingId(updatedResult.getBooking().getId());
        resultDTO.setDoctorId(updatedResult.getDoctor().getId());

        BookingDTO bookingDTO = new BookingDTO();
        Booking booking = updatedResult.getBooking();
        bookingDTO.setId(booking.getId());
        bookingDTO.setDate(booking.getDate());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setBookingAt(booking.getBookingAt());
        bookingDTO.setDepartmentId(booking.getDepartment().getId());
        bookingDTO.setPatientId(booking.getPatient().getId());
        bookingDTO.setShiftId(booking.getShift().getId());
        resultDTO.setBooking(bookingDTO);

        DoctorDTO doctorDTO = new DoctorDTO();
        Doctor doctor = updatedResult.getDoctor();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setPhonenumber(doctor.getPhonenumber());
        doctorDTO.setDepartmentId(doctor.getDepartment().getId());
        resultDTO.setDoctor(doctorDTO);

        return resultDTO;
    }

    public List<ResultDTO> getByBookingSuccess() {
        List<Result> results = resultRepository.findResultsByBookingStatus();

        if (results.isEmpty()) {
            throw new IllegalArgumentException("No Result found with Booking status = 4");
        }

        List<ResultDTO> resultDTOs = new ArrayList<>();

        for (Result result : results) {
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setId(result.getId());
            resultDTO.setRequestTest(result.getRequestTest());
            resultDTO.setExpense(result.getExpense());
            resultDTO.setDiagnoseEnd(result.getDiagnoseEnd());
            resultDTO.setBookingId(result.getBooking().getId());
            resultDTO.setDoctorId(result.getDoctor().getId());

            BookingDTO bookingDTO = new BookingDTO();
            Booking booking = result.getBooking();
            bookingDTO.setId(booking.getId());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setShiftId(booking.getShift().getId());
            resultDTO.setBooking(bookingDTO);

            DoctorDTO doctorDTO = new DoctorDTO();
            Doctor doctor = result.getDoctor();
            doctorDTO.setId(doctor.getId());
            doctorDTO.setName(doctor.getName());
            doctorDTO.setPhonenumber(doctor.getPhonenumber());
            doctorDTO.setDepartmentId(doctor.getDepartment().getId());
            resultDTO.setDoctor(doctorDTO);

            resultDTOs.add(resultDTO);
        }

        return resultDTOs;
    }

    public List<ResultDTO> getResultsByBookingId(Long bookingId) {
        List<Result> results = resultRepository.findByBookingId(bookingId);

        if (results.isEmpty()) {
            throw new IllegalArgumentException("No Result found with Booking ID = " + bookingId);
        }

        List<ResultDTO> resultDTOs = new ArrayList<>();

        for (Result result : results) {
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setId(result.getId());
            resultDTO.setRequestTest(result.getRequestTest());
            resultDTO.setExpense(result.getExpense());
            resultDTO.setDiagnoseEnd(result.getDiagnoseEnd());
            resultDTO.setBookingId(result.getBooking().getId());
            resultDTO.setDoctorId(result.getDoctor().getId());

            BookingDTO bookingDTO = new BookingDTO();
            Booking booking = result.getBooking();
            bookingDTO.setId(booking.getId());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setShiftId(booking.getShift().getId());
            resultDTO.setBooking(bookingDTO);

            DoctorDTO doctorDTO = new DoctorDTO();
            Doctor doctor = result.getDoctor();
            doctorDTO.setId(doctor.getId());
            doctorDTO.setName(doctor.getName());
            doctorDTO.setPhonenumber(doctor.getPhonenumber());
            doctorDTO.setDepartmentId(doctor.getDepartment().getId());
            resultDTO.setDoctor(doctorDTO);

            resultDTOs.add(resultDTO);
        }

        return resultDTOs;
    }
    public List<ResultDTO> getResultsByPatientId(Long patientId) {
        List<Result> results = resultRepository.findByPatientId(patientId);

        if (results.isEmpty()) {
            throw new IllegalArgumentException("No Result found with Patient ID = " + patientId);
        }

        List<ResultDTO> resultDTOs = new ArrayList<>();

        for (Result result : results) {
            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setId(result.getId());
            resultDTO.setRequestTest(result.getRequestTest());
            resultDTO.setExpense(result.getExpense());
            resultDTO.setDiagnoseEnd(result.getDiagnoseEnd());
            resultDTO.setBookingId(result.getBooking().getId());
            resultDTO.setDoctorId(result.getDoctor().getId());

            BookingDTO bookingDTO = new BookingDTO();
            Booking booking = result.getBooking();
            bookingDTO.setId(booking.getId());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setShiftId(booking.getShift().getId());
            resultDTO.setBooking(bookingDTO);

            DoctorDTO doctorDTO = new DoctorDTO();
            Doctor doctor = result.getDoctor();
            doctorDTO.setId(doctor.getId());
            doctorDTO.setName(doctor.getName());
            doctorDTO.setPhonenumber(doctor.getPhonenumber());
            doctorDTO.setDepartmentId(doctor.getDepartment().getId());
            resultDTO.setDoctor(doctorDTO);

            resultDTOs.add(0,resultDTO);
        }

        return resultDTOs;
    }
}

