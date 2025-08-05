package com.narde.gestionaleosteopatabetto.data.database.repository

import com.narde.gestionaleosteopatabetto.data.database.models.Patient
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow

/**
 * Interface for Patient repository operations
 * Provides a clean API for patient data access and management
 */
interface PatientRepositoryInterface {
    
    /**
     * Get all patients as a Flow for reactive UI updates
     * Ordered by surname, then name
     */
    fun getAllPatientsFlow(): Flow<ResultsChange<Patient>>
    
    /**
     * Get all patients as a list (one-time query)
     */
    fun getAllPatients(): List<Patient>
    
    /**
     * Find patient by ID
     */
    fun getPatientById(patientId: String): Patient?
    
    /**
     * Search patients by name (nome or cognome)
     */
    fun searchPatientsByName(searchTerm: String): List<Patient>
    
    /**
     * Search patients by phone number
     */
    fun searchPatientsByPhone(phoneNumber: String): List<Patient>
    
    /**
     * Search patients by city
     */
    fun getPatientsByCity(city: String): List<Patient>
    
    /**
     * Save or update a patient
     * If patient exists (same ID), it will be updated
     * If patient is new, it will be created
     */
    suspend fun savePatient(patient: Patient)
    
    /**
     * Delete a patient by ID
     */
    suspend fun deletePatient(patientId: String)
    
    /**
     * Get patients count
     */
    fun getPatientsCount(): Long
    
    /**
     * Get patients with missing privacy consent
     * Important for GDPR compliance
     */
    fun getPatientsWithoutPrivacyConsent(): List<Patient>
    
    /**
     * Delete all patients from the database
     * Use with caution - this will permanently remove all patient data
     */
    suspend fun deleteAllPatients()
}