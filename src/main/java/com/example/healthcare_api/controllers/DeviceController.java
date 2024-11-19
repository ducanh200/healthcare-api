package com.example.healthcare_api.controllers;

import com.example.healthcare_api.dtos.DeviceDTO;
import com.example.healthcare_api.entities.Device;
import com.example.healthcare_api.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping()
    public List<Device> getAllDevice(){
        return deviceService.getAll();
    }

    @PostMapping()
    public DeviceDTO createDevice(@RequestBody @Valid DeviceDTO request) {
        return deviceService.createDevice(request);
    }

    @PutMapping("/{id}")
    public DeviceDTO updateDevice(@PathVariable Long id ,@RequestBody @Valid DeviceDTO request) {
        return deviceService.updateDevice(id,request);
    }
    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable Long id){
        deviceService.deteteDevice(id);
    }
}
