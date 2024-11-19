package com.example.healthcare_api.service;

import com.example.healthcare_api.dtos.DepartmentDTO;
import com.example.healthcare_api.dtos.DoctorDTO;
import com.example.healthcare_api.entities.Department;
import com.example.healthcare_api.entities.Doctor;
import com.example.healthcare_api.repositories.DepartmentRepository;
import com.example.healthcare_api.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {
    @Value("${server.url}")
    private String serverUrl;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public DoctorService(DoctorRepository doctorRepository, DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    public List<DoctorDTO> getAll() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorDTO> doctorDTOs = new ArrayList<>();

        for (Doctor doctor : doctors) {
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setId(doctor.getId());
            doctorDTO.setName(doctor.getName());
            doctorDTO.setEmail(doctor.getEmail());
            doctorDTO.setPassword(doctor.getPassword());
            doctorDTO.setThumbnail(doctor.getThumbnail());
            doctorDTO.setPhonenumber(doctor.getPhonenumber());
            doctorDTO.setRole(doctor.getRole());
            doctorDTO.setDepartmentId(doctor.getDepartment().getId());

            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(doctor.getDepartment().getId());
            departmentDTO.setName(doctor.getDepartment().getName());
            departmentDTO.setMaxBooking(doctor.getDepartment().getMaxBooking());
            departmentDTO.setDescription(doctor.getDepartment().getDescription());
            departmentDTO.setThumbnail(doctor.getDepartment().getThumbnail());

            doctorDTO.setDepartment(departmentDTO);

            doctorDTOs.add(doctorDTO);
        }

        return doctorDTOs;
    }

    public DoctorDTO createDoctor(DoctorDTO request, MultipartFile file) throws IOException {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + request.getDepartmentId()));

        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setEmail(request.getEmail());
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        String fileName = file.getOriginalFilename();
        String filePath =  serverUrl + "/uploads/" + fileName;// Đường dẫn tới thư mục uploads
        doctor.setThumbnail(filePath);
        byte[] bytes = file.getBytes();
        Path path = Paths.get("uploads/" + fileName); // Đường dẫn thư mục uploads
        Files.write(path, bytes);
        doctor.setPhonenumber(request.getPhonenumber());
        doctor.setRole("DOCTOR");
        doctor.setDepartment(department);

        Doctor savedDoctor = doctorRepository.save(doctor);

        // Create a new DoctorDTO with clinic and department information
        DoctorDTO savedDoctorDTO = new DoctorDTO();
        savedDoctorDTO.setId(savedDoctor.getId());
        savedDoctorDTO.setName(savedDoctor.getName());
        savedDoctorDTO.setEmail(savedDoctor.getEmail());
        savedDoctorDTO.setPassword(passwordEncoder.encode(savedDoctor.getPassword()));
        savedDoctorDTO.setThumbnail(savedDoctor.getThumbnail());
        savedDoctorDTO.setPhonenumber(savedDoctor.getPhonenumber());
        savedDoctorDTO.setRole(savedDoctor.getRole());
        savedDoctorDTO.setDepartmentId(savedDoctor.getDepartment().getId()); // Set departmentId

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(savedDoctor.getDepartment().getId());
        departmentDTO.setName(savedDoctor.getDepartment().getName());
        departmentDTO.setMaxBooking(savedDoctor.getDepartment().getMaxBooking());
        departmentDTO.setDescription(savedDoctor.getDepartment().getDescription());
        departmentDTO.setThumbnail(savedDoctor.getDepartment().getThumbnail());

        savedDoctorDTO.setDepartment(departmentDTO);

        return savedDoctorDTO;
    }


    public DoctorDTO update(Long id, DoctorDTO request, MultipartFile file) throws IOException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        doctor.setName(request.getName());
        if (file != null) {
            String fileName = file.getOriginalFilename();
            String filePath = serverUrl + "/uploads/" + fileName;
            doctor.setThumbnail(filePath);

            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + fileName);
            Files.write(path, bytes);
        }
        doctor.setPhonenumber(request.getPhonenumber());

        if (!doctor.getDepartment().getId().equals(request.getDepartmentId())) {
            Department newDepartment = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()));
            doctor.setDepartment(newDepartment);
        }


        // Save the updated doctor
        Doctor updateDoctor = doctorRepository.save(doctor);

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(updateDoctor.getId());
        doctorDTO.setName(updateDoctor.getName());
        doctorDTO.setEmail(updateDoctor.getEmail());
        doctorDTO.setPassword(updateDoctor.getPassword());
        doctorDTO.setThumbnail(updateDoctor.getThumbnail());
        doctorDTO.setPhonenumber(updateDoctor.getPhonenumber());
        doctorDTO.setRole(updateDoctor.getRole());
        doctorDTO.setDepartmentId(updateDoctor.getDepartment().getId());

        return doctorDTO;
    }

    public DoctorDTO changePassword(@PathVariable Long id,@RequestBody DoctorDTO request){

        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        doctor.setPassword(passwordEncoder.encode(request.getPassword()));

        Doctor updateDoctor = doctorRepository.save(doctor);

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(updateDoctor.getId());
        doctorDTO.setName(updateDoctor.getName());
        doctorDTO.setEmail(updateDoctor.getEmail());
        doctorDTO.setPassword(passwordEncoder.encode(updateDoctor.getPassword()));
        doctorDTO.setThumbnail(updateDoctor.getThumbnail());
        doctorDTO.setPhonenumber(updateDoctor.getPhonenumber());
        doctorDTO.setRole(updateDoctor.getRole());
        doctorDTO.setDepartmentId(updateDoctor.getDepartment().getId());

        return doctorDTO;
    }


    public DoctorDTO findById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setPassword(doctor.getPassword());
        doctorDTO.setThumbnail(doctor.getThumbnail());
        doctorDTO.setPhonenumber(doctor.getPhonenumber());
        doctorDTO.setRole(doctor.getRole());
        doctorDTO.setDepartmentId(doctor.getDepartment().getId());


        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(doctor.getDepartment().getId());
        departmentDTO.setName(doctor.getDepartment().getName());
        departmentDTO.setDescription(doctor.getDepartment().getDescription());
        departmentDTO.setThumbnail(doctor.getDepartment().getThumbnail());
        doctorDTO.setDepartment(departmentDTO);

        return doctorDTO;
    }

    public List<DoctorDTO> getByDepartmentId(Long departmentId) {
        List<Doctor> doctors = doctorRepository.findByDepartmentId(departmentId);
        List<DoctorDTO> doctorDTOs = new ArrayList<>();

        for (Doctor doctor : doctors) {
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setId(doctor.getId());
            doctorDTO.setName(doctor.getName());
            doctorDTO.setEmail(doctor.getEmail());
            doctorDTO.setPassword(doctor.getPassword());
            doctorDTO.setThumbnail(doctor.getThumbnail());
            doctorDTO.setPhonenumber(doctor.getPhonenumber());
            doctorDTO.setRole(doctor.getRole());
            doctorDTO.setDepartmentId(doctor.getDepartment().getId());

            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(doctor.getDepartment().getId());
            departmentDTO.setName(doctor.getDepartment().getName());
            departmentDTO.setDescription(doctor.getDepartment().getDescription());
            departmentDTO.setThumbnail(doctor.getDepartment().getThumbnail());

            doctorDTO.setDepartment(departmentDTO);

            doctorDTOs.add(doctorDTO);
        }

        return doctorDTOs;
    }

    public void deleteDoctor(@PathVariable Long id){
        doctorRepository.deleteById(id);
        throw new RuntimeException("Doctor deleted have id :"+ id);
    }

    public Doctor authenticate(DoctorDTO input){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        Doctor doctor = doctorRepository.findByEmail(input.getEmail());
        if (doctor==null)
            throw new UsernameNotFoundException("Email or Password is not correct!");
        return doctor;
    }
}
