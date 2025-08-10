package com.narde.gestionaleosteopatabetto.data.database

import com.narde.gestionaleosteopatabetto.data.database.repository.PatientRepositoryInterface
import com.narde.gestionaleosteopatabetto.data.database.models.createPatientRepository
import com.narde.gestionaleosteopatabetto.data.database.utils.createDatabaseUtils
import kotlin.getValue

/**
 * Database initialization and setup utilities
 * Handles first-time setup and sample data creation
 * Includes platform compatibility checks
 */
object DatabaseInitializer {
    
    private val _patientRepository by lazy { 
        if (isDatabaseSupported()) createPatientRepository() else null 
    }
    
    private val _databaseUtils by lazy { createDatabaseUtils() }
    
    /**
     * Initialize the database (no longer creates sample data for faster startup)
     * Call this when the app starts up
     * Checks platform support before initializing
     */
    suspend fun initializeDatabase() {
        if (!isDatabaseSupported()) {
            return
        }
        
        _patientRepository?.let { repo ->
            // Log database status on initialization
            val patientCount = repo.getPatientsCount()
            println("🗄️ Database initialized - Found $patientCount patients")
            
            // Auto-restore sample data in development if database is empty
            // This helps during development when Android Studio clears app data
            if (patientCount == 0L) {
                println("📝 Database is empty - Adding sample patient for development")
                val samplePatient = _databaseUtils.createSamplePatient()
                
                // Add clinical history to the sample patient
                samplePatient.storiaClinica = _databaseUtils.createSampleClinicalHistory()
                
                repo.savePatient(samplePatient)
                println("✅ Added sample patient with clinical history: ${samplePatient.datiPersonali?.nome} ${samplePatient.datiPersonali?.cognome}")
            }
        }
    }
    
    /**
     * Create a new empty patient with proper initialization
     * Returns the patient ID for further editing
     * Returns null if database is not supported on current platform
     */
    suspend fun createNewPatient(): String? {
        if (!isDatabaseSupported()) {
            println("Cannot create patient - database not supported on this platform")
            return null
        }
        
        return _patientRepository?.let { repo ->
            val patientCount = repo.getPatientsCount()
            val patientId = _databaseUtils.generatePatientId(patientCount)
            val newPatient = _databaseUtils.createNewPatient(patientId)
            
            repo.savePatient(newPatient)
            
            println("Created new patient with ID: $patientId")
            patientId
        }
    }
    
    /**
     * Get the patient repository if supported on current platform
     * Returns null if database is not supported
     */
    fun getPatientRepository(): PatientRepositoryInterface? = _patientRepository
    
    /**
     * Clear all patients from the database
     * This will permanently delete all patient data
     * Returns true if successful, false if database is not supported
     */
    suspend fun clearAllPatients(): Boolean {
        if (!isDatabaseSupported()) {
            println("Cannot clear patients - database not supported on this platform")
            return false
        }
        
        return _patientRepository?.let { repo ->
            try {
                val countBefore = repo.getPatientsCount()
                repo.deleteAllPatients()
                val countAfter = repo.getPatientsCount()
                
                println("Cleared $countBefore patients from database. Current count: $countAfter")
                true
            } catch (e: Exception) {
                println("Error clearing patients: ${e.message}")
                false
            }
        } ?: false
    }
}