package com.example.healthcare_api.controllers;

import com.example.healthcare_api.dtos.ResultDTO;
import com.example.healthcare_api.entities.Result;
import com.example.healthcare_api.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/results")
public class ResultController {
    @Autowired
    private ResultService resultService;

    @GetMapping()
    public List<ResultDTO> getAll(){
        return resultService.getAllResults();
    }

    @PostMapping()
    public ResultDTO createResult(@RequestBody ResultDTO request){
        return resultService.createResult(request);
    }

    @GetMapping("/findById/{id}")
    public ResultDTO findById(@PathVariable Long id){
        return resultService.getById(id);
    }
    @PutMapping("/{id}")
    public ResultDTO update(@PathVariable Long id, @RequestBody ResultDTO request){
        return resultService.updateResult(id,request);
    }
    @GetMapping("/bookingSuccess")
    public List<ResultDTO> getByBookingSuccess() {
        return resultService.getByBookingSuccess();
    }

    @GetMapping("/GetByBookingId/{bookingId}")
    public List<ResultDTO> getResultsByBookingId(@PathVariable Long bookingId) {
        return resultService.getResultsByBookingId(bookingId);
    }
    @GetMapping("/GetByPatientId/{patientId}")
    public List<ResultDTO> getResultsByPatientId(@PathVariable Long patientId) {
        return resultService.getResultsByPatientId(patientId);
    }
}
