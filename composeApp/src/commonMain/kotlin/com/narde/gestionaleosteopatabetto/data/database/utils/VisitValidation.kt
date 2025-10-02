package com.narde.gestionaleosteopatabetto.data.database.utils

import com.narde.gestionaleosteopatabetto.data.database.models.Visita
import com.narde.gestionaleosteopatabetto.data.database.RealmConfig

/**
 * Visit validation utilities
 * Provides validation functions for visit data integrity
 */
object VisitValidation {
    
    /**
     * Validates visit data according to schema requirements
     * @param visita The visit to validate
     * @return ValidationResult with success status and error message
     */
    fun validateVisit(visita: Visita): ValidationResult {
        // Check required fields
        if (visita.idVisita.isBlank()) {
            return ValidationResult(false, "ID visita è obbligatorio")
        }
        
        if (visita.idPaziente.isBlank()) {
            return ValidationResult(false, "ID paziente è obbligatorio")
        }
        
        if (visita.dataVisita.isBlank()) {
            return ValidationResult(false, "Data visita è obbligatoria")
        }
        
        if (visita.osteopata.isBlank()) {
            return ValidationResult(false, "Nome osteopata è obbligatorio")
        }
        
        // Validate visit ID format (should be unique)
        if (!isValidVisitIdFormat(visita.idVisita)) {
            return ValidationResult(false, "Formato ID visita non valido. Usa formato: VIS_XXX_YYYY_MM_DD")
        }
        
        // Validate date format (YYYY-MM-DD)
        if (!isValidDateFormat(visita.dataVisita)) {
            return ValidationResult(false, "Formato data non valido. Usa formato: YYYY-MM-DD")
        }
        
        // Validate VAS scores (0-10)
        val vasValidation = validateVasScores(visita)
        if (!vasValidation.isValid) {
            return vasValidation
        }
        
        return ValidationResult(true, "")
    }
    
    /**
     * Validates if visit ID format is correct
     * Expected format: VIS_XXX_YYYY_MM_DD
     */
    private fun isValidVisitIdFormat(visitId: String): Boolean {
        val pattern = Regex("^VIS_[A-Z0-9]{3}_\\d{4}_\\d{2}_\\d{2}$")
        return pattern.matches(visitId)
    }
    
    /**
     * Validates if date format is correct
     * Expected format: YYYY-MM-DD
     */
    private fun isValidDateFormat(date: String): Boolean {
        val pattern = Regex("^\\d{4}-\\d{2}-\\d{2}$")
        return pattern.matches(date)
    }
    
    /**
     * Validates VAS scores in consultation reasons
     */
    private fun validateVasScores(visita: Visita): ValidationResult {
        // Check main consultation reason VAS
        visita.motivoConsulto?.principale?.let { principale ->
            if (principale.vas < 0 || principale.vas > 10) {
                return ValidationResult(false, "VAS del motivo principale deve essere tra 0 e 10")
            }
        }
        
        // Check secondary consultation reason VAS
        visita.motivoConsulto?.secondario?.let { secondario ->
            if (secondario.vas < 0 || secondario.vas > 10) {
                return ValidationResult(false, "VAS del motivo secondario deve essere tra 0 e 10")
            }
        }
        
        return ValidationResult(true, "")
    }
    
    /**
     * Checks if patient exists in database
     * @param patientId The patient ID to check
     * @return true if patient exists, false otherwise
     */
    fun patientExists(patientId: String): Boolean {
        return try {
            val patient = RealmConfig.realm.query(
                com.narde.gestionaleosteopatabetto.data.database.models.Patient::class,
                "idPaziente == $0",
                patientId
            ).first().find()
            patient != null
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Checks if visit ID is unique
     * @param visitId The visit ID to check
     * @return true if visit ID is unique, false otherwise
     */
    fun isVisitIdUnique(visitId: String): Boolean {
        return try {
            val existingVisit = RealmConfig.realm.query(
                Visita::class,
                "idVisita == $0",
                visitId
            ).first().find()
            existingVisit == null
        } catch (e: Exception) {
            true // If query fails, assume unique
        }
    }
    
    /**
     * Generates a unique visit ID
     * Format: VIS_XXX_YYYY_MM_DD
     * @param patientId Patient ID to include in the unique identifier
     * @param visitDate Visit date in YYYY-MM-DD format
     * @return Generated unique visit ID
     */
    fun generateUniqueVisitId(patientId: String, visitDate: String): String {
        val dateParts = visitDate.split("-")
        if (dateParts.size != 3) {
            throw IllegalArgumentException("Formato data non valido. Usa YYYY-MM-DD")
        }
        
        val year = dateParts[0]
        val month = dateParts[1]
        val day = dateParts[2]
        
        // Use first 3 characters of patient ID or generate random
        val patientPrefix = if (patientId.length >= 3) {
            patientId.take(3).uppercase()
        } else {
            patientId.uppercase().padEnd(3, 'X')
        }
        
        val baseId = "VIS_${patientPrefix}_${year}_${month}_${day}"
        
        // Check if ID is unique, if not add suffix
        var uniqueId = baseId
        var suffix = 1
        
        while (!isVisitIdUnique(uniqueId)) {
            uniqueId = "${baseId}_${suffix.toString().padStart(2, '0')}"
            suffix++
        }
        
        return uniqueId
    }
}

/**
 * Validation result data class
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String
)
