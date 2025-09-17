package com.narde.gestionaleosteopatabetto.ui.mvi

import com.narde.gestionaleosteopatabetto.ui.viewmodels.PatientField
import com.narde.gestionaleosteopatabetto.ui.viewmodels.ConsentType

/**
 * MVI State for Add Patient Screen
 * Represents the complete UI state in an immutable way
 */
data class AddPatientState(
    // Form fields
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val placeOfBirth: String = "",
    val taxCode: String = "",
    val phone: String = "",
    val email: String = "",
    
    // Address fields
    val street: String = "",
    val city: String = "",
    val zipCode: String = "",
    val province: String = "",
    val country: String = "",
    
    // Anthropometric fields
    val height: String = "",
    val weight: String = "",
    val bmi: String = "",
    val dominantSide: String = "",
    
    // Parent fields
    val fatherFirstName: String = "",
    val fatherLastName: String = "",
    val motherFirstName: String = "",
    val motherLastName: String = "",
    
    // Privacy consents
    val treatmentConsent: Boolean = false,
    val marketingConsent: Boolean = false,
    val thirdPartyConsent: Boolean = false,
    
    // UI state
    val age: Int? = null,
    val isMinor: Boolean = false,
    val isParentSectionExpanded: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String = "",
    val isFormValid: Boolean = false
) {
    /**
     * Computed property for full name
     */
    val fullName: String
        get() = "$firstName $lastName".trim()
    
    /**
     * Computed property for address completeness
     */
    val hasAddress: Boolean
        get() = street.isNotBlank() || city.isNotBlank() || zipCode.isNotBlank()
    
    /**
     * Computed property for anthropometric data completeness
     */
    val hasAnthropometricData: Boolean
        get() = height.isNotBlank() || weight.isNotBlank() || bmi.isNotBlank()
}

/**
 * MVI Events/Intents for Add Patient Screen
 * Represents all possible user actions
 */
sealed class AddPatientEvent {
    // Form field updates
    data class UpdateField(val field: PatientField, val value: String) : AddPatientEvent()
    data class UpdateConsent(val consent: ConsentType, val value: Boolean) : AddPatientEvent()
    
    // UI actions
    object ToggleParentSection : AddPatientEvent()
    object SavePatient : AddPatientEvent()
    object ClearError : AddPatientEvent()
    
    // Form validation
    object ValidateForm : AddPatientEvent()
}

/**
 * Side effects specific to Add Patient Screen
 */
sealed class AddPatientSideEffect : SideEffect() {
    data class PatientSaved(val patientId: String) : AddPatientSideEffect()
    data class ValidationError(val message: String) : AddPatientSideEffect()
    object SavingStarted : AddPatientSideEffect()
    object SavingCompleted : AddPatientSideEffect()
}
