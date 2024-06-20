package com.github.clinicalsapi.controller;

import com.github.clinicalsapi.models.ClinicalData;
import com.github.clinicalsapi.repository.ClinicalDataRepository;
import com.github.clinicalsapi.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created By dhhaval thakkar on 2024-06-20
 */
class ClinicalDataControllerTests {

    @Mock
    private ClinicalDataRepository clinicalDataRepository;

    @Mock
    private PatientRepository patientRepository;

    private ClinicalDataController clinicalDataController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        clinicalDataController = new ClinicalDataController(clinicalDataRepository, patientRepository);
    }

    @Test
    public void createClinicalData_returnsCreatedClinicalData() {
        ClinicalData clinicalData = new ClinicalData();
        when(clinicalDataRepository.save(any(ClinicalData.class))).thenReturn(clinicalData);

        ResponseEntity<ClinicalData> response = clinicalDataController.createClinicalData(clinicalData);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clinicalData, response.getBody());
    }

    @Test
    public void getAllClinicalData_returnsAllClinicalData() {
        List<ClinicalData> clinicalData = Arrays.asList(new ClinicalData(), new ClinicalData());
        when(clinicalDataRepository.findAll()).thenReturn(clinicalData);

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getAllClinicalData();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clinicalData, response.getBody());
    }

    @Test
    public void getClinicalDataById_returnsClinicalData_whenClinicalDataExists() {
        ClinicalData clinicalData = new ClinicalData();
        when(clinicalDataRepository.findById(anyLong())).thenReturn(Optional.of(clinicalData));

        ResponseEntity<ClinicalData> response = clinicalDataController.getClinicalDataById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clinicalData, response.getBody());
    }

    @Test
    public void getClinicalDataById_returnsNotFound_whenClinicalDataDoesNotExist() {
        when(clinicalDataRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<ClinicalData> response = clinicalDataController.getClinicalDataById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateClinicalData_updatesAndReturnsClinicalData_whenClinicalDataExists() {
        ClinicalData existingClinicalData = new ClinicalData();
        ClinicalData updatedClinicalData = new ClinicalData();
        when(clinicalDataRepository.findById(anyLong())).thenReturn(Optional.of(existingClinicalData));
        when(clinicalDataRepository.save(any(ClinicalData.class))).thenReturn(updatedClinicalData);

        ResponseEntity<ClinicalData> response = clinicalDataController.updateClinicalData(1L, updatedClinicalData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedClinicalData, response.getBody());
    }

    @Test
    public void updateClinicalData_returnsNotFound_whenClinicalDataDoesNotExist() {
        ClinicalData updatedClinicalData = new ClinicalData();
        when(clinicalDataRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<ClinicalData> response = clinicalDataController.updateClinicalData(1L, updatedClinicalData);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteClinicalData_returnsNoContent_whenClinicalDataExists() {
        when(clinicalDataRepository.findById(anyLong())).thenReturn(Optional.of(new ClinicalData()));

        ResponseEntity<HttpStatus> response = clinicalDataController.deleteClinicalData(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clinicalDataRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void deleteClinicalData_returnsNotFound_whenClinicalDataDoesNotExist() {
        when(clinicalDataRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<HttpStatus> response = clinicalDataController.deleteClinicalData(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clinicalDataRepository, times(0)).deleteById(anyLong());
    }
}
