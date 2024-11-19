package com.example.healthcare_api.controllers;

import com.example.healthcare_api.dtos.PatientDTO;
import com.example.healthcare_api.dtos.response_model.LoginResponse;
import com.example.healthcare_api.entities.Patient;
import com.example.healthcare_api.service.JwtService;
import com.example.healthcare_api.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/patients")
public class PatientController {
    private final PatientService patientService;

    private final JwtService jwtService;

    public PatientController(PatientService patientService, JwtService jwtService) {
        this.patientService = patientService;
        this.jwtService = jwtService;
    }

    @GetMapping()
    public List<Patient> getAllPatient(){
        return patientService.getAll();
    }

    @PostMapping()
    public Patient createPatient(@RequestBody @Valid PatientDTO request){
        return patientService.createPatient(request);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id,@RequestBody PatientDTO request){
        return patientService.updatePatient(id, request);
    }

    @PutMapping("/changePasswordById/{id}")
    public Patient changePassword(@PathVariable Long id,@RequestBody PatientDTO request){
        return patientService.changePassword(id,request);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id){
        patientService.deletePatient(id);
    }

    @GetMapping("/findByName")
    public List<Patient> PatientFindByName(String name){
        return patientService.findByName(name);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public Patient patientById(@PathVariable Long id){
        return patientService.getById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody PatientDTO repuest){
        Patient patient = patientService.authenticate(repuest);
        String jwtToken = jwtService.generateToken(patient);
        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());
        return  ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<Patient> profile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Patient currenUser = (Patient) auth.getPrincipal();
        return ResponseEntity.ok(currenUser);
    }
}
