package com.github.clinicalsapi.controller;

import com.github.clinicalsapi.models.ClinicalData;
import com.github.clinicalsapi.repository.ClinicalDataRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clinicaldata")
public class ClinicalDataController {

    private final ClinicalDataRepository clinicalDataRepository;

    public ClinicalDataController(ClinicalDataRepository clinicalDataRepository) {
        this.clinicalDataRepository = clinicalDataRepository;
    }

    @PostMapping
    public ResponseEntity<ClinicalData> createClinicalData(@RequestBody ClinicalData clinicalData) {
        ClinicalData savedClinicalData = clinicalDataRepository.save(clinicalData);
        return new ResponseEntity<>(savedClinicalData, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClinicalData>> getAllClinicalData() {
        List<ClinicalData> clinicalData = clinicalDataRepository.findAll();
        return new ResponseEntity<>(clinicalData, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalData> getClinicalDataById(@PathVariable Long id) {
        Optional<ClinicalData> clinicalData = clinicalDataRepository.findById(id);
        return clinicalData.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicalData> updateClinicalData(@PathVariable Long id,
                                                           @RequestBody ClinicalData clinicalDataDetails) {
        Optional<ClinicalData> clinicalData = clinicalDataRepository.findById(id);
        if (clinicalData.isPresent()) {
            ClinicalData updatedClinicalData = clinicalData.get();
            // update fields of updatedClinicalData based on clinicalDataDetails
            clinicalDataRepository.save(updatedClinicalData);
            return new ResponseEntity<>(updatedClinicalData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClinicalData(@PathVariable Long id) {
        Optional<ClinicalData> clinicalData = clinicalDataRepository.findById(id);
        if (clinicalData.isPresent()) {
            clinicalDataRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}