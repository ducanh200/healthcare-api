package com.example.healthcare_api.controllers;

import com.example.healthcare_api.dtos.DepartmentDTO;
import com.example.healthcare_api.entities.Department;
import com.example.healthcare_api.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.io.IOException;
@RestController
@RequestMapping("/api/v3/departments")
public class DepartmentController {


    @Autowired
    private DepartmentService departmentService;


    @GetMapping()
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<Department> getAll(){
        return departmentService.getAll();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public Department createDepartment(@RequestParam("name") String name,
                                       @RequestParam("maxBooking") Integer maxBooking,
                                       @RequestParam("description") String description,
                                       @RequestParam("thumbnail") MultipartFile file) {
        DepartmentDTO request = new DepartmentDTO();
        request.setName(name);
        request.setMaxBooking(maxBooking);
        request.setDescription(description);

        try {
            return departmentService.createDepartment(request, file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @CrossOrigin(origins = "*")
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public Department updateDepartment(@PathVariable Long id,
                                       @RequestParam("name") String name,
                                       @RequestParam("maxBooking") Integer maxBooking,
                                       @RequestParam("description") String description,
                                       @RequestParam(value = "thumbnail", required = false) MultipartFile file) {
        DepartmentDTO request = new DepartmentDTO();
        request.setName(name);
        request.setMaxBooking(maxBooking);
        request.setDescription(description);

        try {
            Department updatedDepartment = departmentService.updateDepartment(id, request, file);
            if (updatedDepartment != null) {
                return updatedDepartment;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id){
        return departmentService.getById(id);
    }
}
