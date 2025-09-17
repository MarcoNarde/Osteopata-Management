package com.narde.gestionaleosteopatabetto.ui.factories

import androidx.compose.runtime.Composable
import com.narde.gestionaleosteopatabetto.ui.viewmodels.AddPatientViewModel
import com.narde.gestionaleosteopatabetto.domain.usecases.SavePatientUseCase
import com.narde.gestionaleosteopatabetto.domain.usecases.UpdatePatientUseCase

/**
 * Factory for creating ViewModels with proper dependencies
 * This provides a bridge between the existing code and the new MVI architecture
 */
object ViewModelFactory {
    
    /**
     * Create AddPatientViewModel with proper dependencies
     * For now, we'll create a mock use case until the full domain layer is implemented
     */
    fun createAddPatientViewModel(): AddPatientViewModel {
        // TODO: Replace with real implementation when domain layer is complete
        val mockUseCase = object : SavePatientUseCase {
            override suspend fun invoke(patient: com.narde.gestionaleosteopatabetto.domain.models.Patient): kotlinx.coroutines.flow.Flow<kotlin.Result<com.narde.gestionaleosteopatabetto.domain.models.Patient>> {
                return kotlinx.coroutines.flow.flow {
                    try {
                        // For now, just return success with the same patient
                        emit(kotlin.Result.success(patient))
                    } catch (e: Exception) {
                        emit(kotlin.Result.failure(e))
                    }
                }
            }
        }
        
        return AddPatientViewModel(mockUseCase)
    }
    
    /**
     * Create UpdatePatientUseCase with proper dependencies
     * For now, we'll create a mock use case until the full domain layer is implemented
     */
    fun createUpdatePatientUseCase(): UpdatePatientUseCase {
        // TODO: Replace with real implementation when domain layer is complete
        val mockUseCase = object : UpdatePatientUseCase {
            override suspend fun invoke(patient: com.narde.gestionaleosteopatabetto.domain.models.Patient): kotlinx.coroutines.flow.Flow<kotlin.Result<com.narde.gestionaleosteopatabetto.domain.models.Patient>> {
                return kotlinx.coroutines.flow.flow {
                    try {
                        // For now, just return success with the same patient
                        emit(kotlin.Result.success(patient))
                    } catch (e: Exception) {
                        emit(kotlin.Result.failure(e))
                    }
                }
            }
        }
        
        return mockUseCase
    }
}

/**
 * Composable function to get AddPatientViewModel with proper dependencies
 */
@Composable
fun rememberAddPatientViewModel(): AddPatientViewModel {
    return androidx.compose.runtime.remember { ViewModelFactory.createAddPatientViewModel() }
}

/**
 * Composable function to get UpdatePatientUseCase with proper dependencies
 */
@Composable
fun rememberUpdatePatientUseCase(): UpdatePatientUseCase {
    return androidx.compose.runtime.remember { ViewModelFactory.createUpdatePatientUseCase() }
}
