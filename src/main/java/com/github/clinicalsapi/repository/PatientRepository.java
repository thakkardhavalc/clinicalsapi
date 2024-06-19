package com.github.clinicalsapi.repository;

import com.github.clinicalsapi.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created By dhhaval thakkar on 2024-06-19
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}