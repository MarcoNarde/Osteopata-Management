package com.narde.gestionaleosteopatabetto.domain.usecases

import com.narde.gestionaleosteopatabetto.domain.models.Patient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interface for saving a patient
 * Contains business logic for patient validation and persistence
 */
interface SavePatientUseCase {
    suspend operator fun invoke(patient: Patient): Flow<Result<Patient>>
}

/**
 * Implementation of SavePatientUseCase
 */
class SavePatientUseCaseImpl : SavePatientUseCase {
    /**
     * Execute the use case to save a patient
     * @param patient The patient to save
     * @return Flow of Result indicating success or failure
     */
    override suspend operator fun invoke(patient: Patient): Flow<Result<Patient>> = flow {
        try {
            // Business logic: Validate patient data
            val validationResult = validatePatient(patient)
            if (!validationResult.isValid) {
                emit(Result.failure(Exception(validationResult.errorMessage)))
                return@flow
            }
            
            // Business logic: Generate patient ID if not provided
            val patientToSave = if (patient.id.isEmpty()) {
                patient.copy(id = generatePatientId())
            } else {
                patient
            }
            
            // TODO: Implement actual repository save
            // For now, just return the patient with generated ID
            emit(Result.success(patientToSave))
            
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    /**
     * Business logic: Validate patient data
     */
    private fun validatePatient(patient: Patient): ValidationResult {
        return when {
            patient.firstName.isBlank() -> ValidationResult.invalid("First name is required")
            patient.lastName.isBlank() -> ValidationResult.invalid("Last name is required")
            patient.phone.isBlank() -> ValidationResult.invalid("Phone number is required")
            patient.privacyConsents?.treatmentConsent != true -> ValidationResult.invalid("Treatment consent is required")
            else -> ValidationResult.valid()
        }
    }
    
    /**
     * Business logic: Generate patient ID
     */
    private fun generatePatientId(): String {
        val timestamp = kotlinx.datetime.Clock.System.now().epochSeconds % 1000000
        return "P${timestamp.toString().padStart(6, '0')}"
    }
}

/**
 * Validation result data class
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String = ""
) {
    companion object {
        fun valid() = ValidationResult(true)
        fun invalid(message: String) = ValidationResult(false, message)
    }
}
