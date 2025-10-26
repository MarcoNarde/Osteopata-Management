package com.narde.gestionaleosteopatabetto.domain.usecases

import com.narde.gestionaleosteopatabetto.domain.models.Patient
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.data.database.utils.*
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
 * Follows the same pattern as SavePatientUseCase and UpdateVisitUseCase
 */
class UpdatePatientUseCaseImpl : UpdatePatientUseCase {
    
    private val databaseUtils = createDatabaseUtils()
    
    /**
     * Execute the use case to update a patient
     * @param patient The patient with updated information
     * @return Flow of Result indicating success or failure
     */
    override suspend operator fun invoke(patient: Patient): Flow<Result<Patient>> = flow {
        try {
            println("UpdatePatientUseCase: Starting update process for patient ID: ${patient.id}")
            
            // Business logic: Validate patient data
            val validationResult = validatePatient(patient)
            if (!validationResult.isValid) {
                println("UpdatePatientUseCase: Validation failed - ${validationResult.errorMessage}")
                emit(Result.failure(Exception(validationResult.errorMessage)))
                return@flow
            }
            
            println("UpdatePatientUseCase: Validation passed")
            
            // Business logic: Check if patient exists
            if (patient.id.isBlank()) {
                println("UpdatePatientUseCase: Patient ID is blank")
                emit(Result.failure(Exception("Patient ID is required for update")))
                return@flow
            }
            
            // Data layer: Update in database
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getPatientRepository()
                if (repository != null) {
                    println("UpdatePatientUseCase: Repository available, checking if patient exists")
                    
                    // Check if patient exists
                    val existingPatient = repository.getPatientById(patient.id)
                    if (existingPatient == null) {
                        println("UpdatePatientUseCase: Patient not found in database")
                        emit(Result.failure(Exception("Patient not found")))
                        return@flow
                    }
                    
                    println("UpdatePatientUseCase: Patient found, converting to database model")
                    
                    // Convert domain model to database model
                    val databasePatient = patient.toDatabaseModel(databaseUtils)
                    
                    println("UpdatePatientUseCase: Saving updated patient to database")
                    
                    // Update in database (savePatient handles both create and update)
                    repository.savePatient(databasePatient)
                    
                    println("UpdatePatientUseCase: Patient updated successfully in database")
                    
                    // Return success with the updated patient
                    emit(Result.success(patient))
                } else {
                    println("UpdatePatientUseCase: Repository is null")
                    emit(Result.failure(Exception("Patient repository not available")))
                }
            } else {
                println("UpdatePatientUseCase: Database not supported on this platform")
                emit(Result.failure(Exception("Database not supported on this platform")))
            }
            
        } catch (e: Exception) {
            println("UpdatePatientUseCase: Exception occurred - ${e.message}")
            e.printStackTrace()
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

/**
 * Extension to convert domain Patient model to database Patient model
 * Reuses the same conversion logic from SavePatientUseCase
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
