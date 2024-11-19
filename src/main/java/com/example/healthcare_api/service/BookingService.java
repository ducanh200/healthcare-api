package com.example.healthcare_api.service;

import com.example.healthcare_api.dtos.BookingDTO;
import com.example.healthcare_api.dtos.DepartmentDTO;
import com.example.healthcare_api.dtos.PatientDTO;
import com.example.healthcare_api.dtos.ShiftDTO;
import com.example.healthcare_api.entities.Booking;
import com.example.healthcare_api.entities.Department;
import com.example.healthcare_api.entities.Patient;
import com.example.healthcare_api.entities.Shift;
import com.example.healthcare_api.repositories.BookingRepository;
import com.example.healthcare_api.repositories.DepartmentRepository;
import com.example.healthcare_api.repositories.PatientRepository;
import com.example.healthcare_api.repositories.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ShiftRepository shiftRepository;

    public List<BookingDTO> getAll(){
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDTO> bookingDTOs = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());



            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setShiftId(booking.getShift().getId());

            Patient patient = booking.getPatient();
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setId(patient.getId());
            patientDTO.setName(patient.getName());
            patientDTO.setEmail(patient.getEmail());
            patientDTO.setBirthday(patient.getBirthday());
            patientDTO.setGender(patient.getGender());
            patientDTO.setCity(patient.getCity());
            patientDTO.setPhonenumber(patient.getPhonenumber());
            patientDTO.setAddress(patient.getAddress());

            bookingDTO.setPatient(patientDTO);

            Department department = booking.getDepartment();
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(department.getId());
            departmentDTO.setName(department.getName());
            departmentDTO.setMaxBooking(department.getMaxBooking());
            departmentDTO.setThumbnail(department.getThumbnail());

            bookingDTO.setDepartment(departmentDTO);

            Shift shift = booking.getShift();
            ShiftDTO shiftDTO = new ShiftDTO();
            shiftDTO.setId(shift.getId());
            shiftDTO.setTime(shift.getTime());
            shiftDTO.setSession(shift.getSession());

            bookingDTO.setShift(shiftDTO);

            bookingDTOs.add(0,bookingDTO);
        }

        return bookingDTOs;
    }

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = new Booking();

        booking.setBookingAt(Timestamp.valueOf(LocalDateTime.now()));
        booking.setStatus(1);
        booking.setDate(bookingDTO.getDate());


        Patient patient = patientRepository.findById(bookingDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + bookingDTO.getPatientId()));
        booking.setPatient(patient);

        Department department = departmentRepository.findById(bookingDTO.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + bookingDTO.getDepartmentId()));
        booking.setDepartment(department);

        // Find and set shift
        Shift shift = shiftRepository.findById(bookingDTO.getShiftId())
                .orElseThrow(() -> new IllegalArgumentException("Shift not found with id: " + bookingDTO.getShiftId()));
        booking.setShift(shift);

        Booking savedBooking = bookingRepository.save(booking);

        BookingDTO savedBookingDTO = new BookingDTO();
        savedBookingDTO.setId(savedBooking.getId());
        savedBookingDTO.setBookingAt(savedBooking.getBookingAt());
        savedBookingDTO.setStatus(savedBooking.getStatus());
        savedBookingDTO.setDate(savedBooking.getDate());
        savedBookingDTO.setPatientId(savedBooking.getPatient().getId());
        savedBookingDTO.setDepartmentId(savedBooking.getDepartment().getId());
        savedBookingDTO.setShiftId(savedBooking.getShift().getId());

        return savedBookingDTO;
    }

    public BookingDTO updateStatus(Long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            int currentStatus = booking.getStatus();
            int newStatus = currentStatus + 1;
            booking.setStatus(newStatus);

            Booking updatedBooking = bookingRepository.save(booking);

            BookingDTO updatedBookingDTO = new BookingDTO();
            updatedBookingDTO.setId(updatedBooking.getId());
            updatedBookingDTO.setBookingAt(updatedBooking.getBookingAt());
            updatedBookingDTO.setStatus(updatedBooking.getStatus());
            updatedBookingDTO.setDate(updatedBooking.getDate());
            updatedBookingDTO.setPatientId(updatedBooking.getPatient().getId());
            updatedBookingDTO.setDepartmentId(updatedBooking.getDepartment().getId());
            updatedBookingDTO.setShiftId(updatedBooking.getShift().getId());

            return updatedBookingDTO;
        } else {
            throw new RuntimeException("Booking not found with ID: " + id);
        }
    }

    public BookingDTO cancelBooking(Long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            booking.setStatus(6);

            Booking updatedBooking = bookingRepository.save(booking);

            BookingDTO updatedBookingDTO = new BookingDTO();
            updatedBookingDTO.setId(updatedBooking.getId());
            updatedBookingDTO.setBookingAt(updatedBooking.getBookingAt());
            updatedBookingDTO.setStatus(updatedBooking.getStatus());
            updatedBookingDTO.setDate(updatedBooking.getDate());
            updatedBookingDTO.setPatientId(updatedBooking.getPatient().getId());
            updatedBookingDTO.setDepartmentId(updatedBooking.getDepartment().getId());
            updatedBookingDTO.setShiftId(updatedBooking.getShift().getId());

            return updatedBookingDTO;
        } else {
            throw new RuntimeException("Booking not found with ID: " + id);
        }
    }
    public BookingDTO getById(Long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());

            Patient patient = booking.getPatient();
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setId(patient.getId());
            patientDTO.setName(patient.getName());
            patientDTO.setEmail(patient.getEmail());
            patientDTO.setBirthday(patient.getBirthday());
            patientDTO.setGender(patient.getGender());
            patientDTO.setCity(patient.getCity());
            patientDTO.setPhonenumber(patient.getPhonenumber());
            patientDTO.setAddress(patient.getAddress());

            bookingDTO.setPatient(patientDTO);

            Department department = booking.getDepartment();
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(department.getId());
            departmentDTO.setName(department.getName());
            departmentDTO.setMaxBooking(department.getMaxBooking());

            bookingDTO.setDepartment(departmentDTO);

            Shift shift = booking.getShift();
            ShiftDTO shiftDTO = new ShiftDTO();
            shiftDTO.setId(shift.getId());
            shiftDTO.setTime(shift.getTime());
            shiftDTO.setSession(shift.getSession());

            bookingDTO.setShift(shiftDTO);

            return bookingDTO;
        } else {
            // Xử lý trường hợp không tìm thấy booking với id đã cung cấp
            // Ví dụ: có thể ném một ngoại lệ, hoặc trả về null hoặc một đối tượng BookingDTO trống, tùy thuộc vào yêu cầu của ứng dụng của bạn
            return null;
        }
    }

    public List<BookingDTO> getByPatientId(Long id){
        List<Booking> list = bookingRepository.findByPatientId(id);
        if (list.isEmpty()) {
            return null;
        }

        List<BookingDTO> bookingDTOs = new ArrayList<>();

        // 4. Iterate through each booking and create a BookingDTO:
        for (Booking booking : list) {
            // Inline DTO creation:
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());

            // Populate PatientDTO, DepartmentDTO, and ShiftDTO (with error handling):
            Patient patient = booking.getPatient();
            if (patient != null) {
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setId(patient.getId());
                patientDTO.setName(patient.getName());
                patientDTO.setEmail(patient.getEmail());
                patientDTO.setBirthday(patient.getBirthday());
                patientDTO.setGender(patient.getGender());
                patientDTO.setCity(patient.getCity());
                patientDTO.setPhonenumber(patient.getPhonenumber());
                patientDTO.setAddress(patient.getAddress());
                bookingDTO.setPatient(patientDTO);
            }

            Department department = booking.getDepartment();
            if (department != null) {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(department.getId());
                departmentDTO.setName(department.getName());
                departmentDTO.setMaxBooking(department.getMaxBooking());
                bookingDTO.setDepartment(departmentDTO);
            }

            Shift shift = booking.getShift();
            if (shift != null) {
                ShiftDTO shiftDTO = new ShiftDTO();
                shiftDTO.setId(shift.getId());
                shiftDTO.setTime(shift.getTime());
                shiftDTO.setSession(shift.getSession());
                bookingDTO.setShift(shiftDTO);
            }

            // Add the BookingDTO to the list:
            bookingDTOs.add(0,bookingDTO);
        }
        return bookingDTOs;
    }

    public List<BookingDTO> getByDepartmentId(Long id) {
        // Tìm tất cả các booking theo departmentId từ repository:
        List<Booking> bookings = bookingRepository.findByDepartmentId(id);

        // Kiểm tra nếu danh sách rỗng, trả về null hoặc danh sách trống tùy thuộc vào yêu cầu của ứng dụng của bạn.
        if (bookings.isEmpty()) {
            return null;
        }

        // Tạo danh sách mới để chứa các đối tượng BookingDTO:
        List<BookingDTO> bookingDTOs = new ArrayList<>();

        // Lặp qua danh sách các booking và tạo các đối tượng BookingDTO tương ứng:
        for (Booking booking : bookings) {
            // Tạo một đối tượng BookingDTO mới:
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setShiftId(booking.getShift().getId());

            // Lấy thông tin của bệnh nhân từ booking và thiết lập cho BookingDTO:
            Patient patient = booking.getPatient();
            if (patient != null) {
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setId(patient.getId());
                patientDTO.setName(patient.getName());
                patientDTO.setEmail(patient.getEmail());
                patientDTO.setBirthday(patient.getBirthday());
                patientDTO.setGender(patient.getGender());
                patientDTO.setCity(patient.getCity());
                patientDTO.setPhonenumber(patient.getPhonenumber());
                patientDTO.setAddress(patient.getAddress());
                bookingDTO.setPatient(patientDTO);
            }

            // Lấy thông tin của phòng ban từ booking và thiết lập cho BookingDTO:
            Department department = booking.getDepartment();
            if (department != null) {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(department.getId());
                departmentDTO.setName(department.getName());
                departmentDTO.setMaxBooking(department.getMaxBooking());
                bookingDTO.setDepartment(departmentDTO);
            }

            // Lấy thông tin của ca làm việc từ booking và thiết lập cho BookingDTO:
            Shift shift = booking.getShift();
            if (shift != null) {
                ShiftDTO shiftDTO = new ShiftDTO();
                shiftDTO.setId(shift.getId());
                shiftDTO.setTime(shift.getTime());
                shiftDTO.setSession(shift.getSession());
                bookingDTO.setShift(shiftDTO);
            }

            // Thêm BookingDTO vào danh sách:
            bookingDTOs.add(bookingDTO);
        }

        // Trả về danh sách các BookingDTO:
        return bookingDTOs;
    }
    public List<BookingDTO> getByDate(Date date) {
        List<Booking> bookings = bookingRepository.findByDate(date);
        if (bookings.isEmpty()) {
            return null;
        }

        List<BookingDTO> bookingDTOs = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setShiftId(booking.getShift().getId());

            Patient patient = booking.getPatient();
            if (patient != null) {
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setId(patient.getId());
                patientDTO.setName(patient.getName());
                patientDTO.setEmail(patient.getEmail());
                patientDTO.setBirthday(patient.getBirthday());
                patientDTO.setGender(patient.getGender());
                patientDTO.setCity(patient.getCity());
                patientDTO.setPhonenumber(patient.getPhonenumber());
                patientDTO.setAddress(patient.getAddress());
                bookingDTO.setPatient(patientDTO);
            }

            Department department = booking.getDepartment();
            if (department != null) {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(department.getId());
                departmentDTO.setName(department.getName());
                departmentDTO.setMaxBooking(department.getMaxBooking());
                bookingDTO.setDepartment(departmentDTO);
            }

            Shift shift = booking.getShift();
            if (shift != null) {
                ShiftDTO shiftDTO = new ShiftDTO();
                shiftDTO.setId(shift.getId());
                shiftDTO.setTime(shift.getTime());
                shiftDTO.setSession(shift.getSession());
                bookingDTO.setShift(shiftDTO);
            }

            bookingDTOs.add(bookingDTO);
        }

        return bookingDTOs;
    }

    public List<BookingDTO> getByDateRange(Date startDate, Date endDate) {
        List<Booking> bookings = bookingRepository.findByDateBetween(startDate, endDate);
        return bookings.stream().map(booking -> {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());

            bookingDTO.setPatientId(booking.getPatient().getId());
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setId(booking.getPatient().getId());
            patientDTO.setName(booking.getPatient().getName());
            patientDTO.setGender(booking.getPatient().getGender());
            patientDTO.setPhonenumber(booking.getPatient().getPhonenumber());
            bookingDTO.setPatient(patientDTO);

            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(booking.getDepartment().getId());
            departmentDTO.setName(booking.getDepartment().getName());
            bookingDTO.setDepartment(departmentDTO);

            bookingDTO.setShiftId(booking.getShift().getId());
            ShiftDTO shiftDTO = new ShiftDTO();
            shiftDTO.setId(booking.getShift().getId());
            shiftDTO.setSession(booking.getShift().getSession());
            shiftDTO.setTime(booking.getShift().getTime());
            bookingDTO.setShift(shiftDTO);
            return bookingDTO;
        }).collect(Collectors.toList());
    }

    public List<BookingDTO> getByMonthAndCurrentYear(int month) {
        // Lấy năm hiện tại:
        int currentYear = LocalDate.now().getYear();

        // Tìm tất cả các booking theo tháng và năm hiện tại từ repository:
        List<Booking> bookings = bookingRepository.findByMonthAndYear(month, currentYear);

        // Kiểm tra nếu danh sách rỗng, trả về null hoặc danh sách trống tùy thuộc vào yêu cầu của ứng dụng của bạn.
        if (bookings.isEmpty()) {
            return null;
        }

        // Tạo danh sách mới để chứa các đối tượng BookingDTO:
        List<BookingDTO> bookingDTOs = new ArrayList<>();

        // Lặp qua danh sách các booking và tạo các đối tượng BookingDTO tương ứng:
        for (Booking booking : bookings) {

//            if (booking.getStatus() != 3) {
//                continue;
//            }
            // Tạo một đối tượng BookingDTO mới:
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setShiftId(booking.getShift().getId());

            // Lấy thông tin của bệnh nhân từ booking và thiết lập cho BookingDTO:
            Patient patient = booking.getPatient();
            if (patient != null) {
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setId(patient.getId());
                patientDTO.setName(patient.getName());
                patientDTO.setEmail(patient.getEmail());
                patientDTO.setBirthday(patient.getBirthday());
                patientDTO.setGender(patient.getGender());
                patientDTO.setCity(patient.getCity());
                patientDTO.setPhonenumber(patient.getPhonenumber());
                patientDTO.setAddress(patient.getAddress());
                bookingDTO.setPatient(patientDTO);
            }

            // Lấy thông tin của phòng ban từ booking và thiết lập cho BookingDTO:
            Department department = booking.getDepartment();
            if (department != null) {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(department.getId());
                departmentDTO.setName(department.getName());
                departmentDTO.setMaxBooking(department.getMaxBooking());
                bookingDTO.setDepartment(departmentDTO);
            }

            // Lấy thông tin của ca làm việc từ booking và thiết lập cho BookingDTO:
            Shift shift = booking.getShift();
            if (shift != null) {
                ShiftDTO shiftDTO = new ShiftDTO();
                shiftDTO.setId(shift.getId());
                shiftDTO.setTime(shift.getTime());
                shiftDTO.setSession(shift.getSession());
                bookingDTO.setShift(shiftDTO);
            }

            // Thêm BookingDTO vào danh sách:
            bookingDTOs.add(bookingDTO);
        }

        // Trả về danh sách các BookingDTO:
        return bookingDTOs;
    }


    public List<BookingDTO> getBookingByStatus1() {
        List<Booking> bookings = bookingRepository.findByStatus(1);
        if (bookings.isEmpty()) {
            return null;
        }

        List<BookingDTO> bookingDTOs = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setShiftId(booking.getShift().getId());

            Patient patient = booking.getPatient();
            if (patient != null) {
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setId(patient.getId());
                patientDTO.setName(patient.getName());
                patientDTO.setEmail(patient.getEmail());
                patientDTO.setBirthday(patient.getBirthday());
                patientDTO.setGender(patient.getGender());
                patientDTO.setCity(patient.getCity());
                patientDTO.setPhonenumber(patient.getPhonenumber());
                patientDTO.setAddress(patient.getAddress());
                bookingDTO.setPatient(patientDTO);
            }

            Department department = booking.getDepartment();
            if (department != null) {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(department.getId());
                departmentDTO.setName(department.getName());
                departmentDTO.setMaxBooking(department.getMaxBooking());
                bookingDTO.setDepartment(departmentDTO);
            }

            Shift shift = booking.getShift();
            if (shift != null) {
                ShiftDTO shiftDTO = new ShiftDTO();
                shiftDTO.setId(shift.getId());
                shiftDTO.setTime(shift.getTime());
                shiftDTO.setSession(shift.getSession());
                bookingDTO.setShift(shiftDTO);
            }

            bookingDTOs.add(bookingDTO);
        }

        return bookingDTOs;
    }

    public List<BookingDTO> getBookingByStatus2() {
        List<Booking> bookings = bookingRepository.findByStatus(2);
        if (bookings.isEmpty()) {
            return null;
        }

        List<BookingDTO> bookingDTOs = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setShiftId(booking.getShift().getId());

            Patient patient = booking.getPatient();
            if (patient != null) {
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setId(patient.getId());
                patientDTO.setName(patient.getName());
                patientDTO.setEmail(patient.getEmail());
                patientDTO.setBirthday(patient.getBirthday());
                patientDTO.setGender(patient.getGender());
                patientDTO.setCity(patient.getCity());
                patientDTO.setPhonenumber(patient.getPhonenumber());
                patientDTO.setAddress(patient.getAddress());
                bookingDTO.setPatient(patientDTO);
            }

            Department department = booking.getDepartment();
            if (department != null) {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(department.getId());
                departmentDTO.setName(department.getName());
                departmentDTO.setMaxBooking(department.getMaxBooking());
                bookingDTO.setDepartment(departmentDTO);
            }

            Shift shift = booking.getShift();
            if (shift != null) {
                ShiftDTO shiftDTO = new ShiftDTO();
                shiftDTO.setId(shift.getId());
                shiftDTO.setTime(shift.getTime());
                shiftDTO.setSession(shift.getSession());
                bookingDTO.setShift(shiftDTO);
            }

            bookingDTOs.add(bookingDTO);
        }

        return bookingDTOs;
    }
    public List<BookingDTO> getBookingByStatus3() {
        List<Booking> bookings = bookingRepository.findByStatus(3);
        if (bookings.isEmpty()) {
            return null;
        }

        List<BookingDTO> bookingDTOs = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setShiftId(booking.getShift().getId());

            Patient patient = booking.getPatient();
            if (patient != null) {
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setId(patient.getId());
                patientDTO.setName(patient.getName());
                patientDTO.setEmail(patient.getEmail());
                patientDTO.setBirthday(patient.getBirthday());
                patientDTO.setGender(patient.getGender());
                patientDTO.setCity(patient.getCity());
                patientDTO.setPhonenumber(patient.getPhonenumber());
                patientDTO.setAddress(patient.getAddress());
                bookingDTO.setPatient(patientDTO);
            }

            Department department = booking.getDepartment();
            if (department != null) {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(department.getId());
                departmentDTO.setName(department.getName());
                departmentDTO.setMaxBooking(department.getMaxBooking());
                bookingDTO.setDepartment(departmentDTO);
            }

            Shift shift = booking.getShift();
            if (shift != null) {
                ShiftDTO shiftDTO = new ShiftDTO();
                shiftDTO.setId(shift.getId());
                shiftDTO.setTime(shift.getTime());
                shiftDTO.setSession(shift.getSession());
                bookingDTO.setShift(shiftDTO);
            }

            bookingDTOs.add(bookingDTO);
        }

        return bookingDTOs;
    }
    public List<BookingDTO> getBookingByStatus4() {
        List<Booking> bookings = bookingRepository.findByStatus(4);
        if (bookings.isEmpty()) {
            return null;
        }

        List<BookingDTO> bookingDTOs = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingAt(booking.getBookingAt());
            bookingDTO.setDate(booking.getDate());
            bookingDTO.setStatus(booking.getStatus());
            bookingDTO.setPatientId(booking.getPatient().getId());
            bookingDTO.setDepartmentId(booking.getDepartment().getId());
            bookingDTO.setShiftId(booking.getShift().getId());

            Patient patient = booking.getPatient();
            if (patient != null) {
                PatientDTO patientDTO = new PatientDTO();
                patientDTO.setId(patient.getId());
                patientDTO.setName(patient.getName());
                patientDTO.setEmail(patient.getEmail());
                patientDTO.setBirthday(patient.getBirthday());
                patientDTO.setGender(patient.getGender());
                patientDTO.setCity(patient.getCity());
                patientDTO.setPhonenumber(patient.getPhonenumber());
                patientDTO.setAddress(patient.getAddress());
                bookingDTO.setPatient(patientDTO);
            }

            Department department = booking.getDepartment();
            if (department != null) {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(department.getId());
                departmentDTO.setName(department.getName());
                departmentDTO.setMaxBooking(department.getMaxBooking());
                bookingDTO.setDepartment(departmentDTO);
            }

            Shift shift = booking.getShift();
            if (shift != null) {
                ShiftDTO shiftDTO = new ShiftDTO();
                shiftDTO.setId(shift.getId());
                shiftDTO.setTime(shift.getTime());
                shiftDTO.setSession(shift.getSession());
                bookingDTO.setShift(shiftDTO);
            }

            bookingDTOs.add(bookingDTO);
        }

        return bookingDTOs;
    }
}
