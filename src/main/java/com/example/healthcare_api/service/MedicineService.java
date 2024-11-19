package com.example.healthcare_api.service;

import com.example.healthcare_api.entities.Medicine;
import com.example.healthcare_api.repositories.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

    public List<Medicine> getAll(){
        return medicineRepository.findAll();
    }

    public Medicine createMedicine(@RequestBody Medicine medicine){
        return  medicineRepository.save(medicine);
    }

    public Medicine updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine){
        return medicineRepository.findById(id)
                .map(m->{
                   m.setName(medicine.getName());
                   return medicineRepository.save(m);
                })
                .orElseGet(()->{
                    medicine.setId(id);
                    return medicineRepository.save(medicine);
                });
    }

    public void delete(@PathVariable Long id){
        medicineRepository.deleteById(id);
    }
}
