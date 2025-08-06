package com.narde.gestionaleosteopatabetto.ui.viewmodels

/**
 * UI State for Add Patient screen
 */
data class AddPatientUiState(
    // Personal Information
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val placeOfBirth: String = "",
    val taxCode: String = "",
    val phone: String = "",
    val email: String = "",
    
    // Anthropometric measurements
    val height: String = "", // Height in centimeters
    val weight: String = "", // Weight in kilograms
    val bmi: String = "", // Body Mass Index
    val dominantSide: String = "", // Dominant side: "sx" for left, "dx" for right
    
    // Address Information
    val street: String = "",
    val city: String = "",
    val zipCode: String = "",
    val province: String = "",
    val country: String = "IT",
    
    // Parent Information (for minors)
    val fatherFirstName: String = "",
    val fatherLastName: String = "",
    val motherFirstName: String = "",
    val motherLastName: String = "",
    val isParentSectionExpanded: Boolean = false,
    
    // Calculated fields
    val age: Int? = null,
    val isMinor: Boolean = false,
    
    // Privacy Consents
    val treatmentConsent: Boolean = false,
    val marketingConsent: Boolean = false,
    val thirdPartyConsent: Boolean = false,
    
    // UI State
    val isSaving: Boolean = false,
    val errorMessage: String = ""
)

/**
 * UI State for Edit Patient screen
 */
data class EditPatientUiState(
    // Patient ID (immutable)
    val patientId: String = "",
    
    // Personal Information
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val placeOfBirth: String = "",
    val taxCode: String = "",
    val phone: String = "",
    val email: String = "",
    
    // Anthropometric measurements
    val height: String = "", // Height in centimeters
    val weight: String = "", // Weight in kilograms
    val bmi: String = "", // Body Mass Index
    val dominantSide: String = "", // Dominant side: "sx" for left, "dx" for right
    
    // Address Information
    val street: String = "",
    val city: String = "",
    val zipCode: String = "",
    val province: String = "",
    val country: String = "IT",
    
    // Parent Information (for minors)
    val fatherFirstName: String = "",
    val fatherLastName: String = "",
    val motherFirstName: String = "",
    val motherLastName: String = "",
    val isParentSectionExpanded: Boolean = false,
    
    // Calculated fields
    val age: Int? = null,
    val isMinor: Boolean = false,
    
    // Privacy Consents
    val treatmentConsent: Boolean = false,
    val marketingConsent: Boolean = false,
    val thirdPartyConsent: Boolean = false,
    
    // UI State
    val isUpdating: Boolean = false,
    val errorMessage: String = "",
    val isUpdateSuccessful: Boolean = false
)

/**
 * Form field types
 */
enum class PatientField {
    FirstName, LastName, BirthDate, Gender, PlaceOfBirth, TaxCode, Phone, Email,
    Height, Weight, BMI, DominantSide, // Anthropometric fields
    Street, City, ZipCode, Province, Country,
    FatherFirstName, FatherLastName, MotherFirstName, MotherLastName
}

/**
 * Consent types
 */
enum class ConsentType {
    Treatment, Marketing, ThirdParty
}

/**
 * Validation result
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String = ""
) {
    companion object {
        fun valid() = ValidationResult(true)
        fun invalid(message: String) = ValidationResult(false, message)
    }
} 