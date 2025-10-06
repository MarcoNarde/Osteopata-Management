package com.narde.gestionaleosteopatabetto.ui.factories

import androidx.compose.runtime.Composable
import com.narde.gestionaleosteopatabetto.ui.viewmodels.AddPatientViewModel
import com.narde.gestionaleosteopatabetto.ui.viewmodels.AddVisitViewModel
import com.narde.gestionaleosteopatabetto.ui.viewmodels.EditVisitViewModel
import com.narde.gestionaleosteopatabetto.domain.usecases.SavePatientUseCaseImpl
import com.narde.gestionaleosteopatabetto.domain.usecases.UpdatePatientUseCase
import com.narde.gestionaleosteopatabetto.domain.usecases.UpdateVisitUseCaseImpl
import com.narde.gestionaleosteopatabetto.domain.usecases.GetVisitsByPatientUseCaseImpl
import com.narde.gestionaleosteopatabetto.domain.usecases.SaveVisitUseCaseImpl
import com.narde.gestionaleosteopatabetto.domain.usecases.GetVisitUseCaseImpl
import com.narde.gestionaleosteopatabetto.data.model.Patient

/**
 * Factory for creating ViewModels with proper dependencies
 * This provides a bridge between the existing code and the new MVI architecture
 */
object ViewModelFactory {
    
    /**
     * Create AddPatientViewModel with proper dependencies
     * Now uses real implementation that saves to database
     */
    fun createAddPatientViewModel(): AddPatientViewModel {
        val savePatientUseCase = SavePatientUseCaseImpl()
        return AddPatientViewModel(savePatientUseCase)
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
    
    /**
     * Create AddVisitViewModel with proper dependencies
     */
    fun createAddVisitViewModel(): AddVisitViewModel {
        val savePatientUseCase = SavePatientUseCaseImpl()
        val getVisitsByPatientUseCase = GetVisitsByPatientUseCaseImpl()
        val saveVisitUseCase = SaveVisitUseCaseImpl()
        return AddVisitViewModel(savePatientUseCase, getVisitsByPatientUseCase, saveVisitUseCase)
    }
    
    /**
     * Create EditVisitViewModel with proper dependencies
     */
    fun createEditVisitViewModel(patients: List<Patient>?): EditVisitViewModel {
        val updateVisitUseCase = UpdateVisitUseCaseImpl()
        val getVisitUseCase = GetVisitUseCaseImpl()
        return EditVisitViewModel(patients, updateVisitUseCase, getVisitUseCase)
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

/**
 * Composable function to get AddVisitViewModel with proper dependencies
 */
@Composable
fun rememberAddVisitViewModel(): AddVisitViewModel {
    return androidx.compose.runtime.remember { ViewModelFactory.createAddVisitViewModel() }
}

/**
 * Composable function to get EditVisitViewModel with proper dependencies
 */
@Composable
fun rememberEditVisitViewModel(patients: List<Patient>?): EditVisitViewModel {
    return androidx.compose.runtime.remember(patients) { 
        ViewModelFactory.createEditVisitViewModel(patients) 
    }
}
