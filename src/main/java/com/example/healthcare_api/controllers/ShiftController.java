package com.example.healthcare_api.controllers;

import com.example.healthcare_api.dtos.ShiftDTO;
import com.example.healthcare_api.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v3/shifts")
public class ShiftController {
    @Autowired
    private ShiftService shiftService;

    @GetMapping()
    public List<ShiftDTO> getAllShifts() {
        return shiftService.getAllShifts();
    }

    @PostMapping("")
    public ShiftDTO createShift(@RequestBody ShiftDTO shiftDTO) {
        return shiftService.createShift(shiftDTO);
    }

    @GetMapping("/{id}")
    public ShiftDTO getById(@PathVariable Long id){
        return shiftService.getShiftById(id);
    }
    @GetMapping("/available")
    public List<ShiftDTO> getAvailableShifts(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("departmentId") Long departmentId) {
        return shiftService.getShiftsByDateAndDepartment(date, departmentId);
    }
}
