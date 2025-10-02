package com.narde.gestionaleosteopatabetto.domain.usecases

import com.narde.gestionaleosteopatabetto.domain.models.Visit
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interface for getting a visit by ID
 * Contains business logic for visit retrieval
 */
interface GetVisitUseCase {
    suspend operator fun invoke(visitId: String): Flow<Result<Visit?>>
}

/**
 * Implementation of GetVisitUseCase
 */
class GetVisitUseCaseImpl : GetVisitUseCase {
    
    /**
     * Execute the use case to get a visit by ID
     * @param visitId The ID of the visit to retrieve
     * @return Flow of Result containing the visit or null if not found
     */
    override suspend operator fun invoke(visitId: String): Flow<Result<Visit?>> = flow {
        try {
            // Business logic: Validate visit ID
            if (visitId.isBlank()) {
                emit(Result.failure(Exception("Visit ID is required")))
                return@flow
            }
            
            // Data layer: Get from database
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getVisitRepository()
                if (repository != null) {
                    // Get visit from database
                    val databaseVisit = repository.getVisitById(visitId)
                    
                    // Convert database model to domain model
                    val domainVisit = databaseVisit?.toDomainModel()
                    
                    // Return success with the visit
                    emit(Result.success(domainVisit))
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
}

/**
 * Extension to convert database Visit model to domain Visit model
 */
private fun com.narde.gestionaleosteopatabetto.data.database.models.Visita.toDomainModel(): Visit {
    return Visit(
        idVisita = this.idVisita,
        idPaziente = this.idPaziente,
        dataVisita = kotlinx.datetime.LocalDate.parse(this.dataVisita),
        osteopata = this.osteopata,
        noteGenerali = this.noteGenerali,
        datiVisitaCorrente = this.datiVisitaCorrente?.let { currentData ->
            com.narde.gestionaleosteopatabetto.domain.models.DatiVisitaCorrente(
                peso = currentData.peso,
                bmi = currentData.bmi,
                pressione = currentData.pressione,
                indiciCraniali = currentData.indiciCraniali
            )
        },
        motivoConsulto = this.motivoConsulto?.let { motivo ->
            com.narde.gestionaleosteopatabetto.domain.models.MotivoConsulto(
                principale = motivo.principale?.let { principale ->
                    com.narde.gestionaleosteopatabetto.domain.models.MotivoPrincipale(
                        descrizione = principale.descrizione,
                        insorgenza = principale.insorgenza,
                        dolore = principale.dolore,
                        vas = principale.vas,
                        fattori = principale.fattori
                    )
                },
                secondario = motivo.secondario?.let { secondario ->
                    com.narde.gestionaleosteopatabetto.domain.models.MotivoSecondario(
                        descrizione = secondario.descrizione,
                        durata = secondario.durata,
                        vas = secondario.vas
                    )
                }
            )
        }
    )
}

