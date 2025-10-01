package com.narde.gestionaleosteopatabetto.domain.usecases

import com.narde.gestionaleosteopatabetto.domain.models.Patient
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.data.database.utils.*
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
    
    private val databaseUtils = createDatabaseUtils()
    
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
            
            // Data layer: Save to database
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getPatientRepository()
                if (repository != null) {
                    // Convert domain model to database model
                    val databasePatient = patientToSave.toDatabaseModel(databaseUtils)
                    
                    // Save to database
                    repository.savePatient(databasePatient)
                    
                    // Return success with the saved patient
                    emit(Result.success(patientToSave))
                } else {
                    emit(Result.failure(Exception("Database repository not available")))
                }
            } else {
                emit(Result.failure(Exception("Database not supported on this platform")))
            }
            
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
 * Extension to convert domain Patient model to database Patient model
 */
private fun Patient.toDatabaseModel(databaseUtils: DatabaseUtilsInterface): com.narde.gestionaleosteopatabetto.data.database.models.Patient {
    return databaseUtils.createNewPatient(this.id).apply {
        // Personal data
        datiPersonali?.apply {
            nome = this@toDatabaseModel.firstName
            cognome = this@toDatabaseModel.lastName
            dataNascita = this@toDatabaseModel.birthDate?.toString() ?: ""
            sesso = when (this@toDatabaseModel.gender) {
                com.narde.gestionaleosteopatabetto.domain.models.Gender.MALE -> "M"
                com.narde.gestionaleosteopatabetto.domain.models.Gender.FEMALE -> "F"
            }
            luogoNascita = this@toDatabaseModel.placeOfBirth
            codiceFiscale = this@toDatabaseModel.taxCode
            telefonoPaziente = this@toDatabaseModel.phone
            emailPaziente = this@toDatabaseModel.email
            
            // Anthropometric data
            this@toDatabaseModel.anthropometricData?.let { anthro ->
                altezza = anthro.height
                peso = anthro.weight
                bmi = anthro.bmi
                latoDominante = when (anthro.dominantSide) {
                    com.narde.gestionaleosteopatabetto.domain.models.DominantSide.RIGHT -> "dx"
                    com.narde.gestionaleosteopatabetto.domain.models.DominantSide.LEFT -> "sx"
                }
            }
        }
        
        // Address data
        this@toDatabaseModel.address?.let { addr ->
            indirizzo?.apply {
                via = addr.street
                citta = addr.city
                cap = addr.zipCode
                provincia = addr.province
                nazione = addr.country
                tipoIndirizzo = addr.type
            }
        }
        
        // Privacy consents
        this@toDatabaseModel.privacyConsents?.let { consents ->
            privacy?.apply {
                consensoTrattamento = consents.treatmentConsent
                consensoMarketing = consents.marketingConsent
                consensoTerzeparti = consents.thirdPartyConsent
                dataConsenso = consents.consentDate
                notePrivacy = consents.notes ?: ""
            }
        }
        
        // Parent information for minors
        if (this@toDatabaseModel.isMinor) {
            this@toDatabaseModel.parentInfo?.let { parentInfo ->
                if (genitori == null) {
                    genitori = com.narde.gestionaleosteopatabetto.data.database.models.Genitori().apply {
                        padre = com.narde.gestionaleosteopatabetto.data.database.models.Padre()
                        madre = com.narde.gestionaleosteopatabetto.data.database.models.Madre()
                    }
                }
                
                parentInfo.father?.let { father ->
                    genitori?.padre?.apply {
                        nome = father.firstName
                        cognome = father.lastName
                    }
                }
                
                parentInfo.mother?.let { mother ->
                    genitori?.madre?.apply {
                        nome = mother.firstName
                        cognome = mother.lastName
                    }
                }
            }
        }
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
