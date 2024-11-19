package com.example.healthcare_api.service;

import com.example.healthcare_api.dtos.DeviceDTO;
import com.example.healthcare_api.entities.Department;
import com.example.healthcare_api.entities.Device;
import com.example.healthcare_api.repositories.DepartmentRepository;
import com.example.healthcare_api.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Device> getAll(){
        return deviceRepository.findAllWithDepartment();
    }

    public DeviceDTO createDevice(@RequestBody DeviceDTO request){
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + request.getDepartmentId()));

        Device device = new Device();
        device.setName(request.getName());
        device.setDescription(request.getDescription());
        device.setExpense(request.getExpense());
        device.setDepartment(department);

        Device deviceSaved = deviceRepository.save(device);

        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(deviceSaved.getId());
        deviceDTO.setName(deviceSaved.getName());
        deviceDTO.setDescription(deviceSaved.getDescription());
        deviceDTO.setExpense(deviceSaved.getExpense());
        deviceDTO.setDepartmentId(deviceSaved.getDepartment().getId());

        return deviceDTO;
    }

    public DeviceDTO updateDevice(Long id, DeviceDTO request) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        device.setName(request.getName());
        device.setDescription(request.getDescription());
        device.setExpense(request.getExpense());

        if (!device.getDepartment().getId().equals(request.getDepartmentId())) {
            Department newDepartment = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()));
            device.setDepartment(newDepartment);
        }


        Device updateDevice = deviceRepository.save(device);

        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(updateDevice.getId());
        deviceDTO.setName(updateDevice.getName());
        deviceDTO.setDescription(updateDevice.getDescription());
        deviceDTO.setExpense(updateDevice.getExpense());

        deviceDTO.setDepartmentId(updateDevice.getDepartment().getId());

        return deviceDTO;
    }

    public void deteteDevice(@PathVariable Long id){
        deviceRepository.deleteById(id);
        throw new RuntimeException("Deleted Device have id : "+id);
    }
}
