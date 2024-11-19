package com.example.healthcare_api.config;

import com.example.healthcare_api.entities.Admin;
import com.example.healthcare_api.entities.Doctor;
import com.example.healthcare_api.entities.Patient;
import com.example.healthcare_api.repositories.AdminRepository;
import com.example.healthcare_api.repositories.DoctorRepository;
import com.example.healthcare_api.repositories.PatientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    private final AdminRepository adminRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public ApplicationConfiguration(AdminRepository adminRepository, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.adminRepository = adminRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return email->{
            Admin admin = adminRepository.findByEmail(email);
            Patient patient = patientRepository.findByEmail(email);
            Doctor doctor =  doctorRepository.findByEmail(email);
            if (admin != null) {
                return admin;
            } else if (patient != null) {
                return patient;
            } else if (doctor != null) {
                return doctor;
            } else {
                throw new UsernameNotFoundException("Email or password is not correct!");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
