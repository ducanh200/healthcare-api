package com.example.healthcare_api.service;

import com.example.healthcare_api.dtos.AdminDTO;
import com.example.healthcare_api.entities.Admin;
import com.example.healthcare_api.repositories.AdminRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public List<Admin> getAll(){
        return adminRepository.findAll();
    }

    public AdminDTO create(AdminDTO adminDTO) {
        Admin admin = new Admin();
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        admin.setRole("ADMIN");

        Admin savedAdmin = adminRepository.save(admin);

        AdminDTO savedAdminDTO = new AdminDTO();
        savedAdminDTO.setId(savedAdmin.getId());
        savedAdminDTO.setName(savedAdmin.getName());
        savedAdminDTO.setEmail(savedAdmin.getEmail());
        savedAdminDTO.setPassword(savedAdmin.getPassword());
        savedAdminDTO.setRole(savedAdmin.getRole());

        return savedAdminDTO;
    }

    public AdminDTO update(Long id, AdminDTO adminDTO) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));

        existingAdmin.setName(adminDTO.getName());

        existingAdmin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));

        Admin updatedAdmin = adminRepository.save(existingAdmin);

        AdminDTO updateAdminDTO = new AdminDTO();
        updateAdminDTO.setId(updatedAdmin.getId());
        updateAdminDTO.setName(updatedAdmin.getName());
        updateAdminDTO.setEmail(updatedAdmin.getEmail());
        updateAdminDTO.setPassword(updatedAdmin.getPassword());
        updateAdminDTO.setRole(updatedAdmin.getRole());
        return updateAdminDTO;
    }
    public void deleteAdmin(@PathVariable Long id){
        adminRepository.deleteById(id);
    }

    public Admin authenticate(AdminDTO input){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        Admin user = adminRepository.findByEmail(input.getEmail());
        if (user==null)
            throw new UsernameNotFoundException("Email or Password is not correct!");
        return user;
    }

}
