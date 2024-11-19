package com.example.healthcare_api.service;

import com.example.healthcare_api.dtos.PatientDTO;
import com.example.healthcare_api.entities.Patient;
import com.example.healthcare_api.repositories.PatientRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public List<Patient> getAll(){
        return patientRepository.findAll();
    }

    public Patient createPatient(@RequestBody PatientDTO request){
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setGender(request.getGender());
        patient.setBirthday(request.getBirthday());
        patient.setPhonenumber(request.getPhonenumber());
        patient.setAddress(request.getAddress());
        patient.setCity(request.getCity());
        patient.setRole("PATIENT");

        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, PatientDTO request) {
        Patient patient = getById(id);

        patient.setName(request.getName());
        patient.setGender(request.getGender());
        patient.setBirthday(request.getBirthday());
        patient.setPhonenumber(request.getPhonenumber());
        patient.setAddress(request.getAddress());
        patient.setCity(request.getCity());

        return patientRepository.save(patient);
    }

    public Patient changePassword(@PathVariable Long id,@RequestBody PatientDTO request){

        Patient patient = getById(id);

        patient.setPassword(passwordEncoder.encode(request.getPassword()));

        return patientRepository.save(patient);
    }

    public void deletePatient(@PathVariable Long id){
        patientRepository.deleteById(id);
        throw new RuntimeException("Patient has been deleted");
    }

    public List<Patient> findByName(String name){
        return patientRepository.findAllByNameContaining(name);
    }

    public Patient getById(Long id){
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public Patient authenticate(PatientDTO input){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        Patient patient = patientRepository.findByEmail(input.getEmail());
        if (patient==null)
            throw new UsernameNotFoundException("Email or Password is not correct!");
        return patient;
    }
}
