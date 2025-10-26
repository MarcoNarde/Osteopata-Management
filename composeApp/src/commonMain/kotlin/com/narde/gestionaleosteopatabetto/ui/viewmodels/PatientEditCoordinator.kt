package com.narde.gestionaleosteopatabetto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.async

/**
 * Unified state for patient edit operations
 * Manages both personal data and clinical history updates
 */
data class PatientEditState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String = "",
    val personalDataError: String? = null,
    val clinicalHistoryError: String? = null
)

/**
 * Coordinator for managing patient edit operations
 * Handles both patient details and clinical history updates together
 * Provides unified state and parallel execution
 */
class PatientEditCoordinator(
    private val editPatientViewModel: EditPatientViewModel,
    private val clinicalHistoryViewModel: ClinicalHistoryViewModel
) : ViewModel() {
    
    private val _state = MutableStateFlow(PatientEditState())
    val state: StateFlow<PatientEditState> = _state.asStateFlow()
    
    /**
     * Save both patient details and clinical history in parallel
     * Provides unified loading/success/error states
     */
    fun savePatient(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Start loading state
            _state.value = PatientEditState(isLoading = true, errorMessage = "")
            
            try {
                // Execute both updates in parallel
                val personalDataDeferred = async {
                    editPatientViewModel.updatePatient()
                }
                val clinicalHistoryDeferred = async {
                    clinicalHistoryViewModel.updateClinicalHistory()
                }
                
                // Wait for both operations to complete
                val personalDataResult = personalDataDeferred.await()
                val clinicalHistoryResult = clinicalHistoryDeferred.await()
                
                // Handle combined results
                when {
                    personalDataResult.isSuccess && clinicalHistoryResult.isSuccess -> {
                        // Both succeeded
                        _state.value = PatientEditState(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = ""
                        )
                        // Wait for UI feedback
                        delay(1500)
                        onSuccess()
                        // Reset success state
                        _state.value = _state.value.copy(isSuccess = false)
                    }
                    
                    personalDataResult.isFailure && clinicalHistoryResult.isFailure -> {
                        // Both failed
                        val pdError = personalDataResult.exceptionOrNull()?.message ?: "Unknown error"
                        val chError = clinicalHistoryResult.exceptionOrNull()?.message ?: "Unknown error"
                        _state.value = PatientEditState(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = "Failed to update patient details and clinical history",
                            personalDataError = pdError,
                            clinicalHistoryError = chError
                        )
                    }
                    
                    else -> {
                        // Partial success
                        val errors = mutableListOf<String>()
                        if (personalDataResult.isFailure) {
                            val error = personalDataResult.exceptionOrNull()?.message ?: "Unknown error"
                            errors.add("Patient details: $error")
                            _state.value = _state.value.copy(personalDataError = error)
                        }
                        if (clinicalHistoryResult.isFailure) {
                            val error = clinicalHistoryResult.exceptionOrNull()?.message ?: "Unknown error"
                            errors.add("Clinical history: $error")
                            _state.value = _state.value.copy(clinicalHistoryError = error)
                        }
                        
                        _state.value = PatientEditState(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = "Partial update succeeded: ${errors.joinToString(", ")}",
                            personalDataError = _state.value.personalDataError,
                            clinicalHistoryError = _state.value.clinicalHistoryError
                        )
                    }
                }
            } catch (e: Exception) {
                println("PatientEditCoordinator: Exception during save - ${e.message}")
                _state.value = PatientEditState(
                    isLoading = false,
                    isSuccess = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Clear error messages
     */
    fun clearErrors() {
        _state.value = _state.value.copy(
            errorMessage = "",
            personalDataError = null,
            clinicalHistoryError = null
        )
    }
}

