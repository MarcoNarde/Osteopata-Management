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
    val isLoadingPatients: Boolean = false,
    
    // Apparatus evaluation state
    val apparatusExpandedStates: Map<String, Boolean> = mapOf(
        "cranio" to false,
        "respiratorio" to false,
        "cardiovascolare" to false,
        "gastrointestinale" to false,
        "urinario" to false,
        "riproduttivo" to false,
        "psicoNeuroEndocrino" to false,
        "unghieCute" to false,
        "metabolismo" to false,
        "linfonodi" to false,
        "muscoloScheletrico" to false,
        "nervoso" to false
    ),
    val apparatoCranio: ApparatoCranioState = ApparatoCranioState(),
    val apparatoRespiratorio: ApparatoRespiratorioState = ApparatoRespiratorioState(),
    val apparatoCardiovascolare: ApparatoCardiovascolareState = ApparatoCardiovascolareState(),
    val apparatoGastrointestinale: ApparatoGastrointestinaleState = ApparatoGastrointestinaleState(),
    val apparatoUrinario: ApparatoUrinarioState = ApparatoUrinarioState(),
    val apparatoRiproduttivo: ApparatoRiproduttivoState = ApparatoRiproduttivoState(),
    val apparatoPsicoNeuroEndocrino: ApparatoPsicoNeuroEndocrinoState = ApparatoPsicoNeuroEndocrinoState(),
    val apparatoUnghieCute: ApparatoUnghieCuteState = ApparatoUnghieCuteState(),
    val apparatoMetabolismo: ApparatoMetabolismoState = ApparatoMetabolismoState(),
    val apparatoLinfonodi: ApparatoLinfonodiState = ApparatoLinfonodiState(),
    val apparatoMuscoloScheletrico: ApparatoMuscoloScheletricoState = ApparatoMuscoloScheletricoState(),
    val apparatoNervoso: ApparatoNervosoState = ApparatoNervosoState()
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
    
    // Apparatus evaluation events
    data class ToggleApparatusExpanded(val apparatusKey: String, val expanded: Boolean) : AddVisitEvent()
    
    // Cranial apparatus events
    data class UpdateCranioField(val field: String, val value: Any) : AddVisitEvent()
    
    // Respiratory apparatus events
    data class UpdateRespiratorioField(val field: String, val value: Any) : AddVisitEvent()
    
    // Cardiovascular apparatus events
    data class UpdateCardiovascolareField(val field: String, val value: Any) : AddVisitEvent()
    
    // Gastrointestinal apparatus events
    data class UpdateGastrointestinaleField(val field: String, val value: Any) : AddVisitEvent()
    
    // Urinary apparatus events
    data class UpdateUrinarioField(val field: String, val value: Any) : AddVisitEvent()
    
    // Reproductive apparatus events
    data class UpdateRiproduttivoField(val field: String, val value: Any) : AddVisitEvent()
    
    // Psycho-neuro-endocrine apparatus events
    data class UpdatePsicoNeuroEndocrinoField(val field: String, val value: Any) : AddVisitEvent()
    
    // Nails and skin apparatus events
    data class UpdateUnghieCuteField(val field: String, val value: Any) : AddVisitEvent()
    
    // Metabolism apparatus events
    data class UpdateMetabolismoField(val field: String, val value: Any) : AddVisitEvent()
    
    // Lymph nodes apparatus events
    data class UpdateLinfonodiField(val field: String, val value: Any) : AddVisitEvent()
    
    // Musculoskeletal apparatus events
    data class UpdateMuscoloScheletricoField(val field: String, val value: Any) : AddVisitEvent()
    
    // Nervous apparatus events
    data class UpdateNervosoField(val field: String, val value: Any) : AddVisitEvent()
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

