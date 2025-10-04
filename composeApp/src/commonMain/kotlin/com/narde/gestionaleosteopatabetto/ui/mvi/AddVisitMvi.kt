package com.narde.gestionaleosteopatabetto.ui.mvi

import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.model.Visit

/**
 * MVI State for Add Visit Screen
 * Represents the complete UI state in an immutable way
 */
data class AddVisitState(
    // Form fields
    val selectedPatient: Patient? = null,
    val visitDate: String = "",
    val osteopath: String = "Roberto Caeran", // Fixed osteopath name
    val generalNotes: String = "",
    
    // Patient dropdown state
    val patientDropdownExpanded: Boolean = false,
    val patientSearchText: String = "",
    val filteredPatients: List<Patient> = emptyList(),
    
    // Current visit data state
    val weight: String = "",
    val bmi: String = "",
    val bloodPressure: String = "",
    val cranialIndices: String = "",
    
    // Consultation reason state
    val mainReasonDesc: String = "",
    val mainReasonOnset: String = "",
    val mainReasonPain: String = "",
    val mainReasonPainLevel: String = "",
    val mainReasonFactors: String = "",
    
    val secondaryReasonDesc: String = "",
    val secondaryReasonDuration: String = "",
    val secondaryReasonPainLevel: String = "",
    
    // UI state
    val isSaving: Boolean = false,
    val errorMessage: String = "",
    val isFormValid: Boolean = false,
    val isLoadingPatients: Boolean = false
) {
    /**
     * Computed property for form validity
     * Basic validation: patient selected and visit date provided
     */
    val canSave: Boolean
        get() = selectedPatient != null && visitDate.isNotBlank() && !isSaving
    
    /**
     * Computed property for patient search results
     */
    val patientSearchResults: List<Patient>
        get() = if (patientSearchText.isBlank()) {
            filteredPatients
        } else {
            filteredPatients.filter { patient ->
                patient.name.contains(patientSearchText, ignoreCase = true)
            }
        }
}

/**
 * MVI Events/Intents for Add Visit Screen
 * Represents all possible user actions
 */
sealed class AddVisitEvent {
    // Form field updates
    data class UpdateVisitDate(val date: String) : AddVisitEvent()
    data class UpdateGeneralNotes(val notes: String) : AddVisitEvent()
    data class UpdateWeight(val weight: String) : AddVisitEvent()
    data class UpdateBmi(val bmi: String) : AddVisitEvent()
    data class UpdateBloodPressure(val pressure: String) : AddVisitEvent()
    data class UpdateCranialIndices(val indices: String) : AddVisitEvent()
    
    // Main reason updates
    data class UpdateMainReasonDesc(val desc: String) : AddVisitEvent()
    data class UpdateMainReasonOnset(val onset: String) : AddVisitEvent()
    data class UpdateMainReasonPain(val pain: String) : AddVisitEvent()
    data class UpdateMainReasonPainLevel(val level: String) : AddVisitEvent()
    data class UpdateMainReasonFactors(val factors: String) : AddVisitEvent()
    
    // Secondary reason updates
    data class UpdateSecondaryReasonDesc(val desc: String) : AddVisitEvent()
    data class UpdateSecondaryReasonDuration(val duration: String) : AddVisitEvent()
    data class UpdateSecondaryReasonPainLevel(val level: String) : AddVisitEvent()
    
    // Patient selection
    data class SelectPatient(val patient: Patient) : AddVisitEvent()
    data class UpdatePatientSearchText(val searchText: String) : AddVisitEvent()
    data class TogglePatientDropdown(val expanded: Boolean) : AddVisitEvent()
    
    // Actions
    object SaveVisit : AddVisitEvent()
    object ClearError : AddVisitEvent()
    object LoadPatients : AddVisitEvent()
    
    // Form validation
    object ValidateForm : AddVisitEvent()
}

/**
 * Side effects specific to Add Visit Screen
 */
sealed class AddVisitSideEffect : SideEffect() {
    data class VisitSaved(val visitId: String) : AddVisitSideEffect()
    data class ValidationError(val message: String) : AddVisitSideEffect()
    object SavingStarted : AddVisitSideEffect()
    object SavingCompleted : AddVisitSideEffect()
    data class PatientsLoaded(val count: Int) : AddVisitSideEffect()
    object PatientsLoadFailed : AddVisitSideEffect()
}

