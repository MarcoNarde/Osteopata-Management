package com.narde.gestionaleosteopatabetto.ui.viewmodels

/**
 * UI State for Clinical History editing
 */
data class ClinicalHistoryUiState(
    val patientId: String = "",
    
    // Chronic conditions
    val hasDrugAllergies: Boolean = false,
    val drugAllergiesList: String = "",
    val hasDiabetes: Boolean = false,
    val diabetesType: String = "",
    val hasHyperthyroidism: Boolean = false,
    val hasHeartDisease: Boolean = false,
    val hasHypertension: Boolean = false,
    
    // Lifestyle factors
    val smokingStatus: String = "",
    val cigarettesPerDay: String = "",
    val yearsSmoking: String = "",
    val workType: String = "",
    val profession: String = "",
    val workHoursPerDay: String = "",
    val hasPhysicalActivity: Boolean = false,
    val sportsList: String = "",
    val activityFrequency: String = "",
    val activityIntensity: String = "",
    
    // Pediatric history
    val pregnancyComplications: Boolean = false,
    val pregnancyNotes: String = "",
    val birthType: String = "",
    val birthComplications: Boolean = false,
    val birthWeight: String = "",
    val apgarScore: String = "",
    val birthNotes: String = "",
    val firstStepsMonths: String = "",
    val firstWordsMonths: String = "",
    val developmentProblems: Boolean = false,
    val developmentNotes: String = "",
    val pediatricGeneralNotes: String = "",
    
    // Interventions & Traumas
    val interventions: List<InterventionUiState> = emptyList(),
    
    // Diagnostic Tests
    val diagnosticTests: List<DiagnosticTestUiState> = emptyList(),
    
    // UI state
    val isUpdating: Boolean = false,
    val isUpdateSuccessful: Boolean = false,
    val errorMessage: String = ""
)

/**
 * Enum for clinical history text fields
 */
enum class ClinicalHistoryField {
    DrugAllergiesList,
    DiabetesType,
    SmokingStatus,
    CigarettesPerDay,
    YearsSmoking,
    WorkType,
    Profession,
    WorkHoursPerDay,
    SportsList,
    ActivityFrequency,
    ActivityIntensity,
    PregnancyNotes,
    BirthType,
    BirthWeight,
    ApgarScore,
    BirthNotes,
    FirstStepsMonths,
    FirstWordsMonths,
    DevelopmentNotes,
    PediatricGeneralNotes
}

/**
 * Enum for clinical history boolean fields
 */
enum class ClinicalHistoryBooleanField {
    HasDrugAllergies,
    HasDiabetes,
    HasHyperthyroidism,
    HasHeartDisease,
    HasHypertension,
    HasPhysicalActivity,
    PregnancyComplications,
    BirthComplications,
    DevelopmentProblems
}

/**
 * UI State for a single Intervention/Trauma
 */
data class InterventionUiState(
    val id: String = "",
    val date: String = "",
    val type: String = "",
    val description: String = "",
    val treatment: String = "",
    val outcome: String = "",
    val isExpanded: Boolean = false
)

/**
 * UI State for a single Diagnostic Test
 */
data class DiagnosticTestUiState(
    val id: String = "",
    val date: String = "",
    val type: String = "",
    val bodyArea: String = "",
    val results: String = "",
    val facility: String = "",
    val isExpanded: Boolean = false
)

