package com.example.healthcare_api.service;

import com.example.healthcare_api.dtos.DepartmentDTO;
import com.example.healthcare_api.entities.Department;
import com.example.healthcare_api.repositories.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Value("${server.url}")
    private String serverUrl;
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAll(){
        return departmentRepository.findAll();
    }

    public Department createDepartment(DepartmentDTO request, MultipartFile file) throws IOException {
        Department department = new Department();
        department.setName(request.getName());
        department.setMaxBooking(request.getMaxBooking());
        department.setDescription(request.getDescription());

        String fileName = file.getOriginalFilename();
        String filePath =  serverUrl + "/uploads/" + fileName;// Đường dẫn tới thư mục uploads
        department.setThumbnail(filePath);

        // Lưu file vào thư mục uploads
        byte[] bytes = file.getBytes();
        Path path = Paths.get("uploads/" + fileName); // Đường dẫn thư mục uploads
        Files.write(path, bytes);
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, DepartmentDTO request, MultipartFile file) throws IOException {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            department.setName(request.getName());
            department.setMaxBooking(request.getMaxBooking());
            department.setDescription(request.getDescription());

            if (file != null) {
                String fileName = file.getOriginalFilename();
                String filePath = serverUrl + "/uploads/" + fileName;
                department.setThumbnail(filePath);

                byte[] bytes = file.getBytes();
                Path path = Paths.get("uploads/" + fileName);
                Files.write(path, bytes);
            }

            return departmentRepository.save(department);
        } else {
            // Thông báo lỗi hoặc xử lý tùy vào yêu cầu
            throw new EntityNotFoundException("Department with id " + id + " not found");
        }
    }

    public void deleteDepartment(@PathVariable Long id){
        departmentRepository.deleteById(id);
    }

    public Department getById(@PathVariable Long id) {
        return departmentRepository.findById(id).orElseThrow(()-> new RuntimeException("Department not found"));
    }
}
