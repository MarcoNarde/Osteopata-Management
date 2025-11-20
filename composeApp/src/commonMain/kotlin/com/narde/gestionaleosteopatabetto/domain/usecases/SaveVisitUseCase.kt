package com.narde.gestionaleosteopatabetto.domain.usecases

import com.narde.gestionaleosteopatabetto.domain.models.Visit
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.RealmConfig
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.data.database.utils.VisitValidation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interface for saving a visit
 * Contains business logic for visit validation and persistence
 */
interface SaveVisitUseCase {
    suspend operator fun invoke(visit: Visit): Flow<Result<Visit>>
}

/**
 * Implementation of SaveVisitUseCase
 */
class SaveVisitUseCaseImpl : SaveVisitUseCase {
    
    /**
     * Execute the use case to save a visit
     * @param visit The visit to save
     * @return Flow of Result indicating success or failure
     */
    override suspend operator fun invoke(visit: Visit): Flow<Result<Visit>> = flow {
        try {
            println("SaveVisitUseCase: Starting save for visit: ${visit.idVisita}")
            
            // Business logic: Validate visit data
            val validationResult = validateVisit(visit)
            if (!validationResult.isValid) {
                println("SaveVisitUseCase: Validation failed: ${validationResult.errorMessage}")
                emit(Result.failure(Exception(validationResult.errorMessage)))
                return@flow
            }
            
            println("SaveVisitUseCase: Validation passed")
            
            // Business logic: Generate visit ID if not provided
            val visitToSave = if (visit.idVisita.isEmpty()) {
                val generatedId = generateVisitId(visit.idPaziente, visit.dataVisitaString)
                println("SaveVisitUseCase: Generated visit ID: $generatedId")
                visit.copy(idVisita = generatedId)
            } else {
                println("SaveVisitUseCase: Using existing visit ID: ${visit.idVisita}")
                visit
            }
            
            // Data layer: Save to database
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getVisitRepository()
                if (repository != null) {
                    println("SaveVisitUseCase: Converting domain model to database model")
                    // Convert domain model to database model
                    val databaseVisit = visitToSave.toDatabaseModel()
                    
                    println("SaveVisitUseCase: Database model created - ID: ${databaseVisit.idVisita}, Patient: ${databaseVisit.idPaziente}, Date: ${databaseVisit.dataVisita}")
                    
                    // Save to database
                    repository.saveVisit(databaseVisit)
                    
                    println("SaveVisitUseCase: Visit saved to database successfully")
                    
                    // Return success with the saved visit
                    emit(Result.success(visitToSave))
                } else {
                    println("SaveVisitUseCase: Repository is null")
                    emit(Result.failure(Exception("Visit repository not available")))
                }
            } else {
                println("SaveVisitUseCase: Database not supported")
                emit(Result.failure(Exception("Database not supported on this platform")))
            }
            
        } catch (e: Exception) {
            println("SaveVisitUseCase: Exception occurred: ${e.message}")
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }
    
    /**
     * Business logic: Validate visit data
     */
    private fun validateVisit(visit: Visit): ValidationResult {
        return when {
            visit.idPaziente.isBlank() -> ValidationResult.invalid("Patient ID is required")
            visit.dataVisitaString.isBlank() -> ValidationResult.invalid("Visit date is required")
            visit.osteopata.isBlank() -> ValidationResult.invalid("Osteopath name is required")
            // Only validate VAS scores when consultation reasons exist
            visit.motivoConsulto?.principale != null && visit.motivoConsulto.principale.vas !in 0..10 -> ValidationResult.invalid("VAS score must be between 0 and 10")
            visit.motivoConsulto?.secondario != null && visit.motivoConsulto.secondario.vas !in 0..10 -> ValidationResult.invalid("Secondary VAS score must be between 0 and 10")
            else -> ValidationResult.valid()
        }
    }
    
    /**
     * Business logic: Generate visit ID
     */
    private fun generateVisitId(patientId: String, visitDate: String): String {
        return VisitValidation.generateUniqueVisitId(patientId, visitDate)
    }
}

/**
 * Extension to convert domain Visit model to database Visit model
 * Creates Realm objects within a write transaction
 */
private fun Visit.toDatabaseModel(): com.narde.gestionaleosteopatabetto.data.database.models.Visita {
    // Create visit object - Realm objects will be created within the repository's write transaction
    return com.narde.gestionaleosteopatabetto.data.database.models.Visita().apply {
        idVisita = this@toDatabaseModel.idVisita
        idPaziente = this@toDatabaseModel.idPaziente
        dataVisita = this@toDatabaseModel.dataVisitaString
        osteopata = this@toDatabaseModel.osteopata
        noteGenerali = this@toDatabaseModel.noteGenerali
        
        // Current visit data
        this@toDatabaseModel.datiVisitaCorrente?.let { currentData ->
            datiVisitaCorrente = com.narde.gestionaleosteopatabetto.data.database.models.DatiVisitaCorrente().apply {
                peso = currentData.peso
                bmi = currentData.bmi
                pressione = currentData.pressione
                indiciCraniali = currentData.indiciCraniali
            }
        }
        
        // Consultation reason
        this@toDatabaseModel.motivoConsulto?.let { motivo ->
            motivoConsulto = com.narde.gestionaleosteopatabetto.data.database.models.MotivoConsulto().apply {
                // Main reason
                motivo.principale?.let { principale ->
                    this.principale = com.narde.gestionaleosteopatabetto.data.database.models.MotivoPrincipale().apply {
                        descrizione = principale.descrizione
                        insorgenza = principale.insorgenza
                        dolore = principale.dolore
                        vas = principale.vas
                        fattori = principale.fattori
                    }
                }
                
                // Secondary reason
                motivo.secondario?.let { secondario ->
                    this.secondario = com.narde.gestionaleosteopatabetto.data.database.models.MotivoSecondario().apply {
                        descrizione = secondario.descrizione
                        durata = secondario.durata
                        vas = secondario.vas
                    }
                }
            }
        }
        
        // Apparatus evaluation
        // Note: valutazioneApparati is already a Realm object created in AddVisitViewModel
        // When the visit is saved via copyToRealm in the repository, it will link the existing Realm object
        // If valutazioneApparati was created in a different transaction, we need to handle it differently
        // For now, assign it directly - copyToRealm should handle nested Realm objects
        valutazioneApparati = this@toDatabaseModel.valutazioneApparati
    }
}

