package com.narde.gestionaleosteopatabetto.domain.usecases

import com.narde.gestionaleosteopatabetto.domain.models.Visit
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interface for getting visits by patient ID
 * Contains business logic for visit retrieval by patient
 */
interface GetVisitsByPatientUseCase {
    suspend operator fun invoke(patientId: String): Flow<Result<List<Visit>>>
}

/**
 * Implementation of GetVisitsByPatientUseCase
 */
class GetVisitsByPatientUseCaseImpl : GetVisitsByPatientUseCase {
    
    /**
     * Execute the use case to get visits by patient ID
     * @param patientId The ID of the patient
     * @return Flow of Result containing the list of visits
     */
    override suspend operator fun invoke(patientId: String): Flow<Result<List<Visit>>> = flow {
        try {
            // Business logic: Validate patient ID
            if (patientId.isBlank()) {
                emit(Result.failure(Exception("Patient ID is required")))
                return@flow
            }
            
            // Data layer: Get from database
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getVisitRepository()
                if (repository != null) {
                    // Get visits from database
                    val databaseVisits = repository.getVisitsByPatientId(patientId)
                    
                    // Convert database models to domain models
                    val domainVisits = databaseVisits.map { it.toDomainModel() }
                    
                    // Return success with the visits
                    emit(Result.success(domainVisits))
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

