package com.narde.gestionaleosteopatabetto.domain.usecases

import com.narde.gestionaleosteopatabetto.domain.models.Patient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interface for updating an existing patient
 * Contains business logic for patient updates and validation
 */
interface UpdatePatientUseCase {
    suspend operator fun invoke(patient: Patient): Flow<Result<Patient>>
}

/**
 * Implementation of UpdatePatientUseCase
 */
class UpdatePatientUseCaseImpl : UpdatePatientUseCase {
    /**
     * Execute the use case to update a patient
     * @param patient The patient with updated information
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
            
            // TODO: Implement actual repository update
            // For now, just return the patient
            emit(Result.success(patient))
            
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    /**
     * Business logic: Validate patient data for updates
     */
    private fun validatePatient(patient: Patient): ValidationResult {
        return when {
            patient.id.isBlank() -> ValidationResult.invalid("Patient ID is required")
            patient.firstName.isBlank() -> ValidationResult.invalid("First name is required")
            patient.lastName.isBlank() -> ValidationResult.invalid("Last name is required")
            patient.phone.isBlank() -> ValidationResult.invalid("Phone number is required")
            patient.privacyConsents?.treatmentConsent != true -> ValidationResult.invalid("Treatment consent is required")
            else -> ValidationResult.valid()
        }
    }
}
