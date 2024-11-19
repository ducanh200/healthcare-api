package com.example.healthcare_api.service;

import com.example.healthcare_api.dtos.DeviceDTO;
import com.example.healthcare_api.dtos.DoctorDTO;
import com.example.healthcare_api.dtos.ShiftDTO;
import com.example.healthcare_api.dtos.TestDTO;
import com.example.healthcare_api.entities.*;
import com.example.healthcare_api.repositories.DeviceRepository;
import com.example.healthcare_api.repositories.DoctorRepository;
import com.example.healthcare_api.repositories.ResultRepository;
import com.example.healthcare_api.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ResultRepository resultRepository;
    @Value("${server.url}")
    private String serverUrl;

    public List<TestDTO> getAllTests() {
        List<Test> tests = testRepository.findAll();
        List<TestDTO> testDTOs = new ArrayList<>(tests.size());

        tests.forEach(test -> {
            TestDTO testDTO = new TestDTO();
            testDTO.setId(test.getId());
            testDTO.setDiagnose(test.getDiagnose());
            testDTO.setThumbnail(test.getThumbnail());
            testDTO.setTestAt(test.getTestAt());
            testDTO.setExpense(test.getExpense());
            testDTO.setDoctorId(test.getDoctor().getId());
            testDTO.setDeviceId(test.getDevice().getId());
            testDTO.setResultId(test.getResult().getId());

            Device device = test.getDevice();
            DeviceDTO deviceDTO = new DeviceDTO();
            deviceDTO.setId(device.getId());
            deviceDTO.setName(device.getName());
            deviceDTO.setDescription(device.getDescription());
            deviceDTO.setExpense(device.getExpense());
            deviceDTO.setDepartmentId(device.getDepartment().getId());

            testDTO.setDevice(deviceDTO);
            DoctorDTO doctorDTO = new DoctorDTO();
            Doctor doctor = test.getDoctor();
            doctorDTO.setId(doctor.getId());
            doctorDTO.setName(doctor.getName());
            doctorDTO.setPhonenumber(doctor.getPhonenumber());
            doctorDTO.setDepartmentId(doctor.getDepartment().getId());
            testDTO.setDoctor(doctorDTO);

            testDTOs.add(testDTO);
        });

        return testDTOs;
    }

    public TestDTO createTest(@RequestBody TestDTO testDTO){
        // Tạo một đối tượng Test mới từ DTO
        Test test = new Test();
        test.setDiagnose(testDTO.getDiagnose());
        test.setTestAt(Timestamp.valueOf(LocalDateTime.now()));
        test.setExpense(testDTO.getExpense());

        // Tìm và ánh xạ đối tượng Device từ DTO
        Device device = deviceRepository.findById(testDTO.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + testDTO.getDeviceId()));
        test.setDevice(device);

        // Tìm và ánh xạ đối tượng Doctor từ DTO
        Doctor doctor = doctorRepository.findById(testDTO.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + testDTO.getDoctorId()));
        test.setDoctor(doctor);

        // Tìm và ánh xạ đối tượng Result từ DTO
        Result result = resultRepository.findById(testDTO.getResultId())
                .orElseThrow(() -> new IllegalArgumentException("Result not found with id: " + testDTO.getResultId()));
        test.setResult(result);

        // Lưu đối tượng Test mới vào cơ sở dữ liệu
        Test savedTest = testRepository.save(test);

        // Tạo DTO từ đối tượng Test đã lưu
        TestDTO savedTestDTO = new TestDTO();
        savedTestDTO.setId(savedTest.getId());
        savedTestDTO.setDiagnose(savedTest.getDiagnose());
        savedTestDTO.setThumbnail(savedTest.getThumbnail());
        savedTestDTO.setTestAt(savedTest.getTestAt());
        savedTestDTO.setExpense(savedTest.getExpense());
        savedTestDTO.setDoctorId(savedTest.getDoctor().getId());
        savedTestDTO.setDeviceId(savedTest.getDevice().getId());
        savedTestDTO.setResultId(savedTest.getResult().getId());

        // Thực hiện các thao tác khác nếu cần và set các thông tin vào DTO

        return savedTestDTO;
    }

    public TestDTO updateTest(Long id, TestDTO request, MultipartFile file) throws IOException {
        // Tìm test cần cập nhật
        Test testToUpdate = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with id: " + id));

        // Kiểm tra xem file mới có được cung cấp không và có rỗng không
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String filePath =  serverUrl + "/uploads/" + fileName;
            testToUpdate.setThumbnail(filePath);

            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + fileName); // Đường dẫn thư mục uploads
            Files.write(path, bytes);
        }

        testToUpdate.setTestAt(Timestamp.valueOf(LocalDateTime.now()));
        testToUpdate.setExpense(request.getExpense());

        // Tìm và ánh xạ đối tượng Device từ DTO
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + request.getDeviceId()));
        testToUpdate.setDevice(device);

        // Tìm và ánh xạ đối tượng Result từ DTO
        Result result = resultRepository.findById(request.getResultId())
                .orElseThrow(() -> new IllegalArgumentException("Result not found with id: " + request.getResultId()));
        testToUpdate.setResult(result);


        // Lưu test đã cập nhật vào cơ sở dữ liệu
        Test updatedTest = testRepository.save(testToUpdate);

        // Tạo DTO từ test đã cập nhật
        TestDTO updatedTestDTO = new TestDTO();
        updatedTestDTO.setId(updatedTest.getId());
        updatedTestDTO.setDiagnose(updatedTest.getDiagnose());
        updatedTestDTO.setThumbnail(updatedTest.getThumbnail());
        updatedTestDTO.setTestAt(updatedTest.getTestAt());
        updatedTestDTO.setExpense(updatedTest.getExpense());
        updatedTestDTO.setDoctorId(updatedTest.getDoctor().getId());
        updatedTestDTO.setDeviceId(updatedTest.getDevice().getId());
        updatedTestDTO.setResultId(updatedTest.getResult().getId());

        // Thực hiện các thao tác khác nếu cần và set các thông tin vào DTO

        return updatedTestDTO;
    }
    public TestDTO getById(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with id: " + id));

        TestDTO testDTO = new TestDTO();
        testDTO.setId(test.getId());
        testDTO.setDiagnose(test.getDiagnose());
        testDTO.setThumbnail(test.getThumbnail());
        testDTO.setTestAt(test.getTestAt());
        testDTO.setExpense(test.getExpense());
        testDTO.setDoctorId(test.getDoctor().getId());
        testDTO.setDeviceId(test.getDevice().getId());
        testDTO.setResultId(test.getResult().getId());

        Device device = test.getDevice();
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(device.getId());
        deviceDTO.setName(device.getName());
        deviceDTO.setDescription(device.getDescription());
        deviceDTO.setExpense(device.getExpense());
        deviceDTO.setDepartmentId(device.getDepartment().getId());

        testDTO.setDevice(deviceDTO);

        DoctorDTO doctorDTO = new DoctorDTO();
        Doctor doctor = test.getDoctor();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setPhonenumber(doctor.getPhonenumber());
        doctorDTO.setDepartmentId(doctor.getDepartment().getId());
        testDTO.setDoctor(doctorDTO);

        return testDTO;
    }

    public TestDTO updateTest(Long id, TestDTO request) {
        // Tìm test cần cập nhật
        Test testToUpdate = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with id: " + id));

        // Cập nhật thông tin của test
        testToUpdate.setDiagnose(request.getDiagnose());
        testToUpdate.setThumbnail(request.getThumbnail());
        testToUpdate.setTestAt(Timestamp.valueOf(LocalDateTime.now()));
        testToUpdate.setExpense(request.getExpense());

        // Tìm và ánh xạ đối tượng Device từ DTO
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + request.getDeviceId()));
        testToUpdate.setDevice(device);

        // Tìm và ánh xạ đối tượng Doctor từ DTO
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + request.getDoctorId()));
        testToUpdate.setDoctor(doctor);

        // Tìm và ánh xạ đối tượng Result từ DTO
        Result result = resultRepository.findById(request.getResultId())
                .orElseThrow(() -> new IllegalArgumentException("Result not found with id: " + request.getResultId()));
        testToUpdate.setResult(result);

        // Lưu test đã cập nhật vào cơ sở dữ liệu
        Test updatedTest = testRepository.save(testToUpdate);

        // Tạo DTO từ test đã cập nhật
        TestDTO updatedTestDTO = new TestDTO();
        updatedTestDTO.setId(updatedTest.getId());
        updatedTestDTO.setDiagnose(updatedTest.getDiagnose());
        updatedTestDTO.setThumbnail(updatedTest.getThumbnail());
        updatedTestDTO.setTestAt(updatedTest.getTestAt());
        updatedTestDTO.setExpense(updatedTest.getExpense());
        updatedTestDTO.setDoctorId(updatedTest.getDoctor().getId());
        updatedTestDTO.setDeviceId(updatedTest.getDevice().getId());
        updatedTestDTO.setResultId(updatedTest.getResult().getId());

        // Thực hiện các thao tác khác nếu cần và set các thông tin vào DTO

        return updatedTestDTO;
    }
    public List<TestDTO> getTestsByResultId(Long resultId) {
        List<Test> tests = testRepository.findByResultId(resultId);
        List<TestDTO> testDTOs = new ArrayList<>(tests.size());

        tests.forEach(test -> {
            TestDTO testDTO = new TestDTO();
            testDTO.setId(test.getId());
            testDTO.setDiagnose(test.getDiagnose());
            testDTO.setThumbnail(test.getThumbnail());
            testDTO.setTestAt(test.getTestAt());
            testDTO.setExpense(test.getExpense());
            testDTO.setDoctorId(test.getDoctor().getId());
            testDTO.setDeviceId(test.getDevice().getId());
            testDTO.setResultId(test.getResult().getId());

            Device device = test.getDevice();
            DeviceDTO deviceDTO = new DeviceDTO();
            deviceDTO.setId(device.getId());
            deviceDTO.setName(device.getName());
            deviceDTO.setDescription(device.getDescription());
            deviceDTO.setExpense(device.getExpense());
            deviceDTO.setDepartmentId(device.getDepartment().getId());

            testDTO.setDevice(deviceDTO);

            DoctorDTO doctorDTO = new DoctorDTO();
            Doctor doctor = test.getDoctor();
            doctorDTO.setId(doctor.getId());
            doctorDTO.setName(doctor.getName());
            doctorDTO.setEmail(doctor.getEmail());
            doctorDTO.setPhonenumber(doctor.getPhonenumber());
            doctorDTO.setDepartmentId(doctor.getDepartment().getId());
            testDTO.setDoctor(doctorDTO);

            testDTOs.add(testDTO);
        });

        return testDTOs;
    }
}
