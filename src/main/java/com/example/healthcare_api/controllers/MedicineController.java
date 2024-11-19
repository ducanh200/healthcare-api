package com.example.healthcare_api.controllers;

import com.example.healthcare_api.entities.Medicine;
import com.example.healthcare_api.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/medicine")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    @GetMapping()
    public List<Medicine> getAll(){
        return medicineService.getAll();
    }

    @PostMapping()
    public Medicine createMedicine(@RequestBody Medicine medicine){
        return medicineService.createMedicine(medicine);
    }
    @CrossOrigin(origins = "*")
    @PutMapping("/{id}")
    public Medicine updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine){
        return medicineService.updateMedicine(id, medicine);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        medicineService.delete(id);
    }
}
