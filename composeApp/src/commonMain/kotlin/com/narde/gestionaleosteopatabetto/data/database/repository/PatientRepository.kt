package com.narde.gestionaleosteopatabetto.data.database.repository

import com.narde.gestionaleosteopatabetto.data.database.RealmConfig
import com.narde.gestionaleosteopatabetto.data.database.models.Patient
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow

/**
 * Repository for Patient database operations
 * Provides a clean API for patient data access and management
 */
class PatientRepository : PatientRepositoryInterface {
    
    private val realm = RealmConfig.realm
    
    /**
     * Get all patients as a Flow for reactive UI updates
     * Ordered by surname, then name
     */
    override fun getAllPatientsFlow(): Flow<ResultsChange<Patient>> {
        return realm.query<Patient>()
            .sort(
                Pair("datiPersonali.cognome", Sort.ASCENDING), 
                Pair("datiPersonali.nome", Sort.ASCENDING)
            )
            .asFlow()
    }
    
    /**
     * Get all patients as a list (one-time query)
     */
    override fun getAllPatients(): List<Patient> {
        return realm.query<Patient>()
            .sort(
                Pair("datiPersonali.cognome", Sort.ASCENDING), 
                Pair("datiPersonali.nome", Sort.ASCENDING)
            )
            .find()
    }
    
    /**
     * Find patient by ID
     */
    override fun getPatientById(patientId: String): Patient? {
        return realm.query<Patient>("idPaziente == $0", patientId).first().find()
    }
    
    /**
     * Search patients by name (nome or cognome)
     */
    override fun searchPatientsByName(searchTerm: String): List<Patient> {
        return realm.query<Patient>(
            "datiPersonali.nome CONTAINS[c] $0 OR datiPersonali.cognome CONTAINS[c] $0", 
            searchTerm
        ).find()
    }
    
    /**
     * Search patients by phone number
     */
    override fun searchPatientsByPhone(phoneNumber: String): List<Patient> {
        return realm.query<Patient>(
            "datiPersonali.telefonoPaziente CONTAINS $0", 
            phoneNumber
        ).find()
    }
    
    /**
     * Search patients by city
     */
    override fun getPatientsByCity(city: String): List<Patient> {
        return realm.query<Patient>("indirizzo.citta == $0", city).find()
    }
    
    /**
     * Save or update a patient
     * If patient exists (same ID), it will be updated
     * If patient is new, it will be created
     */
    override suspend fun savePatient(patient: Patient) {
        realm.write {
            copyToRealm(patient, updatePolicy = io.realm.kotlin.UpdatePolicy.ALL)
        }
    }
    
    /**
     * Delete a patient by ID
     */
    override suspend fun deletePatient(patientId: String) {
        println("Attempting to delete patient with ID: $patientId")
        
        realm.write {
            try {
                // First, let's check if the patient exists
                val patientToDelete = query<Patient>("idPaziente == $0", patientId).find().firstOrNull()
                
                if (patientToDelete != null) {
                    println("Found patient to delete: ${patientToDelete.datiPersonali?.nome} ${patientToDelete.datiPersonali?.cognome}")
                    delete(patientToDelete)
                    println("Successfully deleted patient with ID: $patientId")
                } else {
                    println("ERROR: Patient with ID $patientId not found in database")
                    // Let's also check what patients actually exist
                    val allPatients = query<Patient>().find()
                    println("Available patient IDs in database: ${allPatients.map { it.idPaziente }}")
                }
            } catch (e: Exception) {
                println("ERROR during patient deletion: ${e.message}")
                throw e
            }
        }
    }
    
    /**
     * Get patients count
     */
    override fun getPatientsCount(): Long {
        return realm.query<Patient>().count().find()
    }
    
    /**
     * Get patients with missing privacy consent
     * Important for GDPR compliance
     */
    override fun getPatientsWithoutPrivacyConsent(): List<Patient> {
        return realm.query<Patient>("privacy.consensoTrattamento == false").find()
    }
    
    /**
     * Delete all patients from the database
     * Use with caution - this will permanently remove all patient data
     */
    override suspend fun deleteAllPatients() {
        realm.write {
            val allPatients = query<Patient>().find()
            delete(allPatients)
        }
    }
}