package com.example.healthcare_api.service;

import com.example.healthcare_api.dtos.MedicineDTO;
import com.example.healthcare_api.dtos.ResultDTO;
import com.example.healthcare_api.dtos.ResultMedicineDTO;
import com.example.healthcare_api.entities.Medicine;
import com.example.healthcare_api.entities.Result;
import com.example.healthcare_api.entities.ResultMedicine;
import com.example.healthcare_api.repositories.MedicineRepository;
import com.example.healthcare_api.repositories.ResultMedicineRepository;
import com.example.healthcare_api.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultMedicineService {
    @Autowired
    private ResultMedicineRepository resultMedicineRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private MedicineRepository medicineRepository;

    public List<ResultMedicineDTO> getAll() {
        List<ResultMedicine> resultMedicines = resultMedicineRepository.findAll();
        List<ResultMedicineDTO> resultMedicineDTOs = new ArrayList<>();

        for (ResultMedicine resultMedicine : resultMedicines) {
            ResultMedicineDTO resultMedicineDTO = new ResultMedicineDTO();
            resultMedicineDTO.setId(resultMedicine.getId());
            resultMedicineDTO.setResultId(resultMedicine.getResult().getId());
            resultMedicineDTO.setMedicineId(resultMedicine.getMedicine().getId());
            resultMedicineDTO.setQuantity(resultMedicine.getQuantity());
            resultMedicineDTO.setDescription(resultMedicine.getDescription());

            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setId(resultMedicine.getResult().getId());
            resultDTO.setRequestTest(resultMedicine.getResult().getRequestTest());
            resultDTO.setExpense(resultMedicine.getResult().getExpense());
            resultDTO.setDiagnoseEnd(resultMedicine.getResult().getDiagnoseEnd());
            resultDTO.setDoctorId(resultMedicine.getResult().getDoctor().getId());
            resultDTO.setBookingId(resultMedicine.getResult().getBooking().getId());
            resultMedicineDTO.setResult(resultDTO);

            MedicineDTO medicineDTO = new MedicineDTO();
            medicineDTO.setId(resultMedicine.getMedicine().getId());
            medicineDTO.setName(resultMedicine.getMedicine().getName());
            resultMedicineDTO.setMedicine(medicineDTO);



            resultMedicineDTOs.add(resultMedicineDTO);
        }

        return resultMedicineDTOs;
    }

    public ResultMedicineDTO create(ResultMedicineDTO request) {
        Result result = resultRepository.findById(request.getResultId())
                .orElseThrow(() -> new IllegalArgumentException("Result not found with id: " + request.getResultId()));

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id: " + request.getMedicineId()));

        ResultMedicine resultMedicine = new ResultMedicine();
        resultMedicine.setResult(result);
        resultMedicine.setMedicine(medicine);
        resultMedicine.setQuantity(request.getQuantity());
        resultMedicine.setDescription(request.getDescription());

        ResultMedicine savedResultMedicine = resultMedicineRepository.save(resultMedicine);

        ResultMedicineDTO savedResultMedicineDTO = new ResultMedicineDTO();
        savedResultMedicineDTO.setResultId(savedResultMedicine.getResult().getId());
        savedResultMedicineDTO.setMedicineId(savedResultMedicine.getMedicine().getId());
        savedResultMedicineDTO.setQuantity(savedResultMedicine.getQuantity());
        savedResultMedicineDTO.setDescription(savedResultMedicine.getDescription());

        return savedResultMedicineDTO;
    }

    public ResultMedicineDTO update(Long resultMedicineId, ResultMedicineDTO request) {
        ResultMedicine resultMedicine = resultMedicineRepository.findById(resultMedicineId)
                .orElseThrow(() -> new IllegalArgumentException("ResultMedicine not found with id: " + resultMedicineId));

        resultMedicine.setQuantity(request.getQuantity());
        resultMedicine.setDescription(request.getDescription());

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found with id: " + request.getMedicineId()));

        resultMedicine.setMedicine(medicine);

        ResultMedicine updatedResultMedicine = resultMedicineRepository.save(resultMedicine);

        ResultMedicineDTO updatedResultMedicineDTO = new ResultMedicineDTO();
        updatedResultMedicineDTO.setResultId(updatedResultMedicine.getResult().getId());
        updatedResultMedicineDTO.setMedicineId(updatedResultMedicine.getMedicine().getId());
        updatedResultMedicineDTO.setQuantity(updatedResultMedicine.getQuantity());
        updatedResultMedicineDTO.setDescription(updatedResultMedicine.getDescription());

        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setId(updatedResultMedicine.getResult().getId());
        resultDTO.setId(updatedResultMedicine.getResult().getId());
        resultDTO.setRequestTest(updatedResultMedicine.getResult().getRequestTest());
        resultDTO.setDiagnoseEnd(updatedResultMedicine.getResult().getDiagnoseEnd());
        resultDTO.setDoctorId(updatedResultMedicine.getResult().getDoctor().getId());
        resultDTO.setBookingId(updatedResultMedicine.getResult().getBooking().getId());

        MedicineDTO updatedMedicineDTO = new MedicineDTO();
        updatedMedicineDTO.setId(updatedResultMedicine.getMedicine().getId());
        updatedMedicineDTO.setName(updatedResultMedicine.getMedicine().getName());

        updatedResultMedicineDTO.setResult(resultDTO);
        updatedResultMedicineDTO.setMedicine(updatedMedicineDTO);

        return updatedResultMedicineDTO;
    }
    public List<ResultMedicineDTO> getByResultId(Long resultId) {
        List<ResultMedicine> resultMedicines = resultMedicineRepository.findByResultId(resultId);
        List<ResultMedicineDTO> resultMedicineDTOs = new ArrayList<>();

        for (ResultMedicine resultMedicine : resultMedicines) {
            ResultMedicineDTO resultMedicineDTO = new ResultMedicineDTO();
            resultMedicineDTO.setId(resultMedicine.getId());
            resultMedicineDTO.setResultId(resultMedicine.getResult().getId());
            resultMedicineDTO.setMedicineId(resultMedicine.getMedicine().getId());
            resultMedicineDTO.setQuantity(resultMedicine.getQuantity());
            resultMedicineDTO.setDescription(resultMedicine.getDescription());

            ResultDTO resultDTO = new ResultDTO();
            resultDTO.setId(resultMedicine.getResult().getId());
            resultDTO.setDoctorId(resultMedicine.getResult().getDoctor().getId());
            resultDTO.setBookingId(resultMedicine.getResult().getBooking().getId());
            resultDTO.setRequestTest(resultMedicine.getResult().getRequestTest());
            resultDTO.setDiagnoseEnd(resultMedicine.getResult().getDiagnoseEnd());

            MedicineDTO medicineDTO = new MedicineDTO();
            medicineDTO.setId(resultMedicine.getMedicine().getId());
            medicineDTO.setName(resultMedicine.getMedicine().getName());

            resultMedicineDTO.setResult(resultDTO);
            resultMedicineDTO.setMedicine(medicineDTO);

            resultMedicineDTOs.add(resultMedicineDTO);
        }

        return resultMedicineDTOs;
    }
    public void delete(Long id) {
        ResultMedicine resultMedicine = resultMedicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ResultMedicine not found with id: " + id));

        resultMedicineRepository.delete(resultMedicine);
    }
}