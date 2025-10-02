package com.narde.gestionaleosteopatabetto.domain.usecases

import com.narde.gestionaleosteopatabetto.domain.models.Visit
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interface for updating an existing visit
 * Contains business logic for visit updates and validation
 */
interface UpdateVisitUseCase {
    suspend operator fun invoke(visit: Visit): Flow<Result<Visit>>
}

/**
 * Implementation of UpdateVisitUseCase
 */
class UpdateVisitUseCaseImpl : UpdateVisitUseCase {
    
    /**
     * Execute the use case to update a visit
     * @param visit The visit with updated information
     * @return Flow of Result indicating success or failure
     */
    override suspend operator fun invoke(visit: Visit): Flow<Result<Visit>> = flow {
        try {
            // Business logic: Validate visit data
            val validationResult = validateVisit(visit)
            if (!validationResult.isValid) {
                emit(Result.failure(Exception(validationResult.errorMessage)))
                return@flow
            }
            
            // Business logic: Check if visit exists
            if (visit.idVisita.isBlank()) {
                emit(Result.failure(Exception("Visit ID is required for update")))
                return@flow
            }
            
            // Data layer: Update in database
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getVisitRepository()
                if (repository != null) {
                    // Check if visit exists
                    val existingVisit = repository.getVisitById(visit.idVisita)
                    if (existingVisit == null) {
                        emit(Result.failure(Exception("Visit not found")))
                        return@flow
                    }
                    
                    // Convert domain model to database model
                    val databaseVisit = visit.toDatabaseModel()
                    
                    // Update in database
                    repository.saveVisit(databaseVisit)
                    
                    // Return success with the updated visit
                    emit(Result.success(visit))
                } else {
                    emit(Result.failure(Exception("Visit repository not available")))
                }
            } else {
                emit(Result.failure(Exception("Database not supported on this platform")))
            }
            
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    /**
     * Business logic: Validate visit data for updates
     */
    private fun validateVisit(visit: Visit): ValidationResult {
        return when {
            visit.idVisita.isBlank() -> ValidationResult.invalid("Visit ID is required")
            visit.idPaziente.isBlank() -> ValidationResult.invalid("Patient ID is required")
            visit.dataVisitaString.isBlank() -> ValidationResult.invalid("Visit date is required")
            visit.osteopata.isBlank() -> ValidationResult.invalid("Osteopath name is required")
            visit.motivoConsulto?.principale?.vas !in 0..10 -> ValidationResult.invalid("VAS score must be between 0 and 10")
            visit.motivoConsulto?.secondario?.vas !in 0..10 -> ValidationResult.invalid("Secondary VAS score must be between 0 and 10")
            else -> ValidationResult.valid()
        }
    }
}

/**
 * Extension to convert domain Visit model to database Visit model
 */
private fun Visit.toDatabaseModel(): com.narde.gestionaleosteopatabetto.data.database.models.Visita {
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
    }
}

