package com.narde.gestionaleosteopatabetto.domain.usecases

import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interface for deleting a visit
 * Contains business logic for visit deletion
 */
interface DeleteVisitUseCase {
    suspend operator fun invoke(visitId: String): Flow<Result<Boolean>>
}

/**
 * Implementation of DeleteVisitUseCase
 */
class DeleteVisitUseCaseImpl : DeleteVisitUseCase {
    
    /**
     * Execute the use case to delete a visit
     * @param visitId The ID of the visit to delete
     * @return Flow of Result indicating success or failure
     */
    override suspend operator fun invoke(visitId: String): Flow<Result<Boolean>> = flow {
        try {
            // Business logic: Validate visit ID
            if (visitId.isBlank()) {
                emit(Result.failure(Exception("Visit ID is required")))
                return@flow
            }
            
            // Data layer: Delete from database
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getVisitRepository()
                if (repository != null) {
                    // Check if visit exists
                    val existingVisit = repository.getVisitById(visitId)
                    if (existingVisit == null) {
                        emit(Result.failure(Exception("Visit not found")))
                        return@flow
                    }
                    
                    // Delete from database
                    repository.deleteVisit(visitId)
                    
                    // Return success
                    emit(Result.success(true))
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

