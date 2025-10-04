package com.narde.gestionaleosteopatabetto.data.database.utils

import com.narde.gestionaleosteopatabetto.data.database.models.Patient
import com.narde.gestionaleosteopatabetto.data.database.models.Visita
import com.narde.gestionaleosteopatabetto.data.database.models.StoriaClinica
import com.narde.gestionaleosteopatabetto.data.model.Patient as UIPatient
import com.narde.gestionaleosteopatabetto.data.model.Visit as UIVisit

/**
 * Interface for Database utility operations
 */
interface DatabaseUtilsInterface {
    
    /**
     * Create a sample patient for testing/development
     */
    fun createSamplePatient(): Patient
    
    /**
     * Generate a patient ID based on patient count
     */
    fun generatePatientId(patientCount: Long): String
    
    /**
     * Create a new empty patient with given ID
     */
    fun createNewPatient(patientId: String): Patient
    
    /**
     * Convert database patient to UI patient
     */
    fun toUIPatient(databasePatient: Patient): UIPatient
    
    /**
     * Convert database visit to UI visit
     */
    fun toUIVisit(databaseVisit: Visita): UIVisit
    
    /**
     * Create clinical history data from JSON structure
     */
    fun createSampleClinicalHistory(): StoriaClinica
}