package com.example.healthcare_api.controllers;

import com.example.healthcare_api.dtos.AdminDTO;
import com.example.healthcare_api.dtos.response_model.LoginResponse;
import com.example.healthcare_api.entities.Admin;
import com.example.healthcare_api.service.AdminService;
import com.example.healthcare_api.service.JwtService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/admin")
public class AdminController {
    private final AdminService adminService;
    private final JwtService jwtService;

    public AdminController(AdminService adminService, JwtService jwtService) {
        this.adminService = adminService;
        this.jwtService = jwtService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping()
    public List<Admin> getAllAdmin(){
        return adminService.getAll();
    }
    @PostMapping("/register")
    public AdminDTO createAdmin(@RequestBody AdminDTO adminDTO){
        return adminService.create(adminDTO);
    }

    @PutMapping("/{id}")
    public AdminDTO updateAdmin(@PathVariable Long id,@RequestBody AdminDTO adminDTO){
        return adminService.update(id, adminDTO);
    }
    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id){
        adminService.deleteAdmin(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AdminDTO repuest){
        Admin admin = adminService.authenticate(repuest);
        String jwtToken = jwtService.generateToken(admin);
        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());
        return  ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<Admin> profile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Admin currenUser = (Admin) auth.getPrincipal();
        return ResponseEntity.ok(currenUser);
    }
}
