package com.github.clinicalsapi.controller;

import com.github.clinicalsapi.models.Patient;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PatientControllerTests {

    @Mock
    private PatientRepository patientRepository;

    private PatientController patientController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        patientController = new PatientController(patientRepository);
    }

    @Test
    public void createPatient_returnsCreatedPatient() {
        Patient patient = new Patient();
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        ResponseEntity<Patient> response = patientController.createPatient(patient);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(patient, response.getBody());
    }

    @Test
    public void getAllPatients_returnsAllPatients() {
        List<Patient> patients = Arrays.asList(new Patient(), new Patient());
        when(patientRepository.findAll()).thenReturn(patients);

        ResponseEntity<List<Patient>> response = patientController.getAllPatients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patients, response.getBody());
    }

    @Test
    public void getPatientById_returnsPatient_whenPatientExists() {
        Patient patient = new Patient();
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        ResponseEntity<Patient> response = patientController.getPatientById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
    }

    @Test
    public void getPatientById_returnsNotFound_whenPatientDoesNotExist() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Patient> response = patientController.getPatientById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updatePatient_updatesAndReturnsPatient_whenPatientExists() {
        Patient existingPatient = new Patient();
        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Updated");
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);

        ResponseEntity<Patient> response = patientController.updatePatient(1L, updatedPatient);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPatient, response.getBody());
    }

    @Test
    public void updatePatient_returnsNotFound_whenPatientDoesNotExist() {
        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Updated");
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Patient> response = patientController.updatePatient(1L, updatedPatient);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deletePatient_returnsNoContent_whenPatientExists() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(new Patient()));

        ResponseEntity<HttpStatus> response = patientController.deletePatient(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patientRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void deletePatient_returnsNotFound_whenPatientDoesNotExist() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<HttpStatus> response = patientController.deletePatient(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(patientRepository, times(0)).deleteById(anyLong());
    }
}