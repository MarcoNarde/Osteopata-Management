package com.narde.gestionaleosteopatabetto.ui.mvi

import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.model.Visit

/**
 * MVI classes for EditVisitScreen
 * Manages state, events, and side effects for editing existing visits
 */

/**
 * State for EditVisitScreen
 * Contains all UI state and form data for editing a visit
 */
data class EditVisitState(
    val isLoading: Boolean = true, // Start with loading true
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    
    // Visit identification and data
    val visitId: String = "",
    val visit: Visit? = null, // Loaded visit data
    val selectedPatient: Patient? = null,
    val visitDate: String = "",
    val osteopath: String = "",
    val generalNotes: String = "",
    
    // Current visit data
    val weight: String = "",
    val bmi: String = "",
    val bloodPressure: String = "",
    val cranialIndices: String = "",
    
    // Main reason data
    val mainReason: String = "",
    val mainReasonOnset: String = "",
    val mainReasonPain: String = "",
    val mainReasonVas: String = "",
    val mainReasonFactors: String = "",
    
    // Secondary reason data
    val secondaryReason: String = "",
    val secondaryReasonDuration: String = "",
    val secondaryReasonVas: String = "",
    
    // Patient dropdown state
    val patientDropdownExpanded: Boolean = false,
    val patientSearchText: String = "",
    val filteredPatients: List<Patient> = emptyList(),
    
    // Validation state
    val isFormValid: Boolean = false
)

/**
 * Events for EditVisitScreen
 * User actions that can trigger state changes
 */
sealed class EditVisitEvent {
    data class LoadVisitById(val visitId: String) : EditVisitEvent()
    object SaveVisit : EditVisitEvent()
    object CancelEdit : EditVisitEvent()
    
    data class UpdatePatient(val patient: Patient?) : EditVisitEvent()
    data class UpdateVisitDate(val date: String) : EditVisitEvent()
    data class UpdateGeneralNotes(val notes: String) : EditVisitEvent()
    
    data class UpdateWeight(val weight: String) : EditVisitEvent()
    data class UpdateBmi(val bmi: String) : EditVisitEvent()
    data class UpdateBloodPressure(val pressure: String) : EditVisitEvent()
    data class UpdateCranialIndices(val indices: String) : EditVisitEvent()
    
    data class UpdateMainReason(val reason: String) : EditVisitEvent()
    data class UpdateMainReasonOnset(val onset: String) : EditVisitEvent()
    data class UpdateMainReasonPain(val pain: String) : EditVisitEvent()
    data class UpdateMainReasonVas(val vas: String) : EditVisitEvent()
    data class UpdateMainReasonFactors(val factors: String) : EditVisitEvent()
    
    data class UpdateSecondaryReason(val reason: String) : EditVisitEvent()
    data class UpdateSecondaryReasonDuration(val duration: String) : EditVisitEvent()
    data class UpdateSecondaryReasonVas(val vas: String) : EditVisitEvent()
    
    data class TogglePatientDropdown(val expanded: Boolean) : EditVisitEvent()
    data class UpdatePatientSearchText(val searchText: String) : EditVisitEvent()
    
    object ClearError : EditVisitEvent()
}

/**
 * Side effects for EditVisitScreen
 * One-time events that should trigger actions outside the ViewModel
 */
sealed class EditVisitSideEffect : SideEffect() {
    data class VisitUpdated(val visit: Visit) : EditVisitSideEffect()
    data class ShowError(val message: String) : EditVisitSideEffect()
    object NavigateBack : EditVisitSideEffect()
    object SavingStarted : EditVisitSideEffect()
    object SavingCompleted : EditVisitSideEffect()
}
