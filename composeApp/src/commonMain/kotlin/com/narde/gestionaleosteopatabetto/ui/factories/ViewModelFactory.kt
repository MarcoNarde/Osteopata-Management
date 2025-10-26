package com.narde.gestionaleosteopatabetto.ui.factories

import androidx.compose.runtime.Composable
import com.narde.gestionaleosteopatabetto.ui.viewmodels.AddPatientViewModel
import com.narde.gestionaleosteopatabetto.ui.viewmodels.EditPatientViewModel
import com.narde.gestionaleosteopatabetto.ui.viewmodels.AddVisitViewModel
import com.narde.gestionaleosteopatabetto.ui.viewmodels.EditVisitViewModel
import com.narde.gestionaleosteopatabetto.domain.usecases.SavePatientUseCaseImpl
import com.narde.gestionaleosteopatabetto.domain.usecases.UpdatePatientUseCaseImpl
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
     * Create EditPatientViewModel with proper dependencies
     * Uses domain layer with UpdatePatientUseCase
     */
    fun createEditPatientViewModel(): EditPatientViewModel {
        val updatePatientUseCase = UpdatePatientUseCaseImpl()
        return EditPatientViewModel(updatePatientUseCase)
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
 * Composable function to get EditVisitViewModel with proper dependencies
 */
@Composable
fun rememberEditVisitViewModel(patients: List<Patient>?): EditVisitViewModel {
    return androidx.compose.runtime.remember(patients) { 
        ViewModelFactory.createEditVisitViewModel(patients) 
    }
}
