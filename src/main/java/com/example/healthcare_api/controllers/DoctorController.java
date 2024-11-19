package com.example.healthcare_api.controllers;

import com.example.healthcare_api.dtos.DoctorDTO;
import com.example.healthcare_api.dtos.response_model.LoginResponse;
import com.example.healthcare_api.entities.Doctor;
import com.example.healthcare_api.service.DoctorService;
import com.example.healthcare_api.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v3/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final JwtService jwtService;

    public DoctorController(DoctorService doctorService, JwtService jwtService) {
        this.doctorService = doctorService;
        this.jwtService = jwtService;
    }

    @GetMapping()
    public List<DoctorDTO> getAllDoctor(){
        return doctorService.getAll();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public DoctorDTO createDoctor(@RequestParam("name") String name,
                                  @RequestParam("email") String email,
                                  @RequestParam("password") String password,
                                  @RequestParam("phonenumber") String phonenumber,
                                  @RequestParam("departmentId") Long departmentId,
                                  @RequestParam("thumbnail") MultipartFile file) throws IOException {

        DoctorDTO request = new DoctorDTO();
        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setPhonenumber(phonenumber);
        request.setDepartmentId(departmentId);

        try {
            return doctorService.createDoctor(request, file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public DoctorDTO updateDoctor(
                                @PathVariable Long id,
                                @RequestParam("name") String name,
                                @RequestParam("phonenumber") String phonenumber,
                                @RequestParam("departmentId") Long departmentId,
                                @RequestParam(value = "thumbnail", required = false) MultipartFile file) throws IOException {
        DoctorDTO request = new DoctorDTO();
        request.setName(name);
        request.setPhonenumber(phonenumber);
        request.setDepartmentId(departmentId);

        try {
            return doctorService.update(id,request, file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping("/changePasswordById/{id}")
    public DoctorDTO changePassword(@PathVariable Long id, @RequestBody DoctorDTO request) {
        return doctorService.changePassword(id, request);
    }


    @GetMapping("/{id}")
    public DoctorDTO getDoctorById(@PathVariable Long id){
        return doctorService.findById(id);
    }

    @GetMapping("/departmentId/{departmentId}")
    public ResponseEntity<List<DoctorDTO>> getByDepartmentId(@PathVariable Long departmentId) {
        List<DoctorDTO> doctorDTOs = doctorService.getByDepartmentId(departmentId);
        return ResponseEntity.ok().body(doctorDTOs);
    }
    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id){
        doctorService.deleteDoctor(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody DoctorDTO repuest){
        Doctor doctor = doctorService.authenticate(repuest);
        String jwtToken = jwtService.generateToken(doctor);
        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());
        return  ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<Doctor> profile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Doctor currenUser = (Doctor) auth.getPrincipal();
        return ResponseEntity.ok(currenUser);
    }
}
