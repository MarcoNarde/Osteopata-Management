package com.narde.gestionaleosteopatabetto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.datetime.toLocalDateTime
import com.narde.gestionaleosteopatabetto.data.database.models.Patient as DatabasePatient
import com.narde.gestionaleosteopatabetto.data.database.utils.*
import com.narde.gestionaleosteopatabetto.utils.DateUtils
import com.narde.gestionaleosteopatabetto.domain.usecases.UpdatePatientUseCase

/**
 * ViewModel for Edit Patient functionality
 * Handles business logic, validation, and state management for patient updates
 * Uses domain layer (UpdatePatientUseCase) following clean architecture principles
 */
class EditPatientViewModel(
    private val updatePatientUseCase: UpdatePatientUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditPatientUiState())
    val uiState: StateFlow<EditPatientUiState> = _uiState.asStateFlow()
    
    /**
     * Initialize the form with existing patient data
     */
    fun initializeWithPatient(dbPatient: DatabasePatient) {
        val state = EditPatientUiState(
            // Patient info
            patientId = dbPatient.id_paziente,
            firstName = dbPatient.dati_personali?.nome ?: "",
            lastName = dbPatient.dati_personali?.cognome ?: "",
            // Convert ISO format from database to Italian format for display
            birthDate = dbPatient.dati_personali?.data_nascita?.let { isoDate -> 
                DateUtils.convertIsoToItalianFormat(isoDate)
            } ?: "",
            gender = dbPatient.dati_personali?.sesso ?: "",
            placeOfBirth = dbPatient.dati_personali?.luogo_nascita ?: "",
            taxCode = dbPatient.dati_personali?.codice_fiscale ?: "",
            phone = dbPatient.dati_personali?.telefono_paziente ?: "",
            email = dbPatient.dati_personali?.email_paziente ?: "",
            
            // Anthropometric measurements
            height = dbPatient.dati_personali?.altezza?.toString() ?: "",
            weight = dbPatient.dati_personali?.peso?.toString() ?: "",
            bmi = dbPatient.dati_personali?.bmi?.toString() ?: "",
            dominantSide = dbPatient.dati_personali?.latoDominante ?: "",
            
            // Address info
            street = dbPatient.indirizzo?.via ?: "",
            city = dbPatient.indirizzo?.citta ?: "",
            zipCode = dbPatient.indirizzo?.cap ?: "",
            province = dbPatient.indirizzo?.provincia ?: "",
            country = dbPatient.indirizzo?.nazione ?: "IT",
            
            // Parent info
            fatherFirstName = dbPatient.genitori?.padre?.nome ?: "",
            fatherLastName = dbPatient.genitori?.padre?.cognome ?: "",
            motherFirstName = dbPatient.genitori?.madre?.nome ?: "",
            motherLastName = dbPatient.genitori?.madre?.cognome ?: "",
            
            // Privacy
            treatmentConsent = dbPatient.privacy?.consenso_trattamento ?: false,
            marketingConsent = dbPatient.privacy?.consenso_marketing ?: false,
            thirdPartyConsent = dbPatient.privacy?.consenso_terze_parti ?: false,
        )
        
        // Calculate age and minor status
        val age = calculateAge(state.birthDate)
        val isMinor = age != null && age < 18
        
        _uiState.value = state.copy(
            age = age,
            isMinor = isMinor,
            isParentSectionExpanded = isMinor && (state.fatherFirstName.isNotBlank() || 
                                                 state.fatherLastName.isNotBlank() ||
                                                 state.motherFirstName.isNotBlank() || 
                                                 state.motherLastName.isNotBlank())
        )
    }
    
    /**
     * Update form field value
     */
    fun updateField(field: PatientField, value: String) {
        _uiState.value = when (field) {
            PatientField.FirstName -> _uiState.value.copy(firstName = value)
            PatientField.LastName -> _uiState.value.copy(lastName = value)
            PatientField.BirthDate -> {
                val age = calculateAge(value)
                val isMinor = age != null && age < 18
                val newState = _uiState.value.copy(
                    birthDate = value,
                    age = age,
                    isMinor = isMinor
                )
                // Auto-collapse parent section if patient becomes adult
                if (!isMinor) {
                    newState.copy(isParentSectionExpanded = false)
                } else {
                    newState
                }
            }
            PatientField.Gender -> _uiState.value.copy(gender = value)
            PatientField.PlaceOfBirth -> _uiState.value.copy(placeOfBirth = value)
            PatientField.TaxCode -> _uiState.value.copy(taxCode = value)
            PatientField.Phone -> _uiState.value.copy(phone = value)
            PatientField.Email -> _uiState.value.copy(email = value)
            
            // Anthropometric fields
            PatientField.Height -> _uiState.value.copy(height = value)
            PatientField.Weight -> _uiState.value.copy(weight = value)
            PatientField.BMI -> _uiState.value.copy(bmi = value)
            PatientField.DominantSide -> _uiState.value.copy(dominantSide = value)
            
            // Address fields
            PatientField.Street -> _uiState.value.copy(street = value)
            PatientField.City -> _uiState.value.copy(city = value)
            PatientField.ZipCode -> _uiState.value.copy(zipCode = value)
            PatientField.Province -> _uiState.value.copy(province = value)
            PatientField.Country -> _uiState.value.copy(country = value)
            
            // Parent fields
            PatientField.FatherFirstName -> _uiState.value.copy(fatherFirstName = value)
            PatientField.FatherLastName -> _uiState.value.copy(fatherLastName = value)
            PatientField.MotherFirstName -> _uiState.value.copy(motherFirstName = value)
            PatientField.MotherLastName -> _uiState.value.copy(motherLastName = value)
        }
        
        // Clear errors when user starts typing
        if (_uiState.value.errorMessage.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "")
        }
    }
    
    /**
     * Update consent checkbox
     */
    fun updateConsent(consent: ConsentType, value: Boolean) {
        _uiState.value = when (consent) {
            ConsentType.Treatment -> _uiState.value.copy(treatmentConsent = value)
            ConsentType.Marketing -> _uiState.value.copy(marketingConsent = value)
            ConsentType.ThirdParty -> _uiState.value.copy(thirdPartyConsent = value)
        }
    }
    
    /**
     * Validate and update patient using domain layer
     */
    fun updatePatient(onSuccess: () -> Unit) {
        val state = _uiState.value
        
        // UI-level validation (for immediate feedback to user)
        val validationResult = validatePatientData(state)
        if (!validationResult.isValid) {
            _uiState.value = state.copy(errorMessage = validationResult.errorMessage)
            return
        }
        
        // Convert UI state to domain model
        val domainPatient = state.toDomainModel()
        
        // Update patient via domain layer
        viewModelScope.launch {
            _uiState.value = state.copy(isUpdating = true, errorMessage = "", isUpdateSuccessful = false)
            
            try {
                // Call use case with domain model
                updatePatientUseCase(domainPatient).collect { result ->
                    result.fold(
                        onSuccess = { _ ->
                            println("EditPatientViewModel: Patient updated successfully via domain layer")
                            // Show success state first
                            _uiState.value = state.copy(
                                isUpdating = false, 
                                isUpdateSuccessful = true,
                                errorMessage = ""
                            )
                            // Wait a moment to show success feedback, then call onSuccess
                            delay(1500) // Show success for 1.5 seconds
                            onSuccess()
                            // Reset success state after switching to view mode
                            _uiState.value = _uiState.value.copy(isUpdateSuccessful = false)
                        },
                        onFailure = { error ->
                            println("EditPatientViewModel: Patient update failed - ${error.message}")
                            _uiState.value = state.copy(
                                isUpdating = false,
                                errorMessage = error.message ?: "Failed to update patient",
                                isUpdateSuccessful = false
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                println("EditPatientViewModel: Exception during update - ${e.message}")
                _uiState.value = state.copy(
                    isUpdating = false,
                    errorMessage = "Error: ${e.message}",
                    isUpdateSuccessful = false
                )
            }
        }
    }
    
    /**
     * Business logic: Calculate age from birth date in Italian DD/MM/AAAA format
     */
    private fun calculateAge(birthDateString: String): Int? {
        return DateUtils.calculateAgeFromItalianDate(birthDateString)
    }
    
    /**
     * Business logic: Validate patient data
     */
    private fun validatePatientData(state: EditPatientUiState): ValidationResult {
        return when {
            state.firstName.isBlank() -> ValidationResult.invalid("First name is required")
            state.lastName.isBlank() -> ValidationResult.invalid("Last name is required")
            state.phone.isBlank() -> ValidationResult.invalid("Phone number is required")
            !state.treatmentConsent -> ValidationResult.invalid("Treatment consent is required")
            else -> ValidationResult.valid()
        }
    }
    
}

/**
 * Extension to convert UI state to domain model for update
 */
private fun EditPatientUiState.toDomainModel(): com.narde.gestionaleosteopatabetto.domain.models.Patient {
    // Parse birth date from Italian format (DD/MM/YYYY) to LocalDate
    val birthDateParsed = try {
        if (birthDate.isNotEmpty()) {
            kotlinx.datetime.LocalDate.parse(
                DateUtils.convertItalianToIsoFormat(birthDate)
            )
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
    
    // Convert gender string to enum
    val genderEnum = when (gender) {
        "M" -> com.narde.gestionaleosteopatabetto.domain.models.Gender.MALE
        "F" -> com.narde.gestionaleosteopatabetto.domain.models.Gender.FEMALE
        else -> com.narde.gestionaleosteopatabetto.domain.models.Gender.MALE
    }
    
    // Convert anthropometric data if available
    val anthropometricData = if (height.isNotBlank() && weight.isNotBlank()) {
        val heightInt = height.toIntOrNull() ?: 0
        val weightDouble = weight.toDoubleOrNull() ?: 0.0
        val bmiDouble = bmi.toDoubleOrNull()
        val dominantSideEnum = when (dominantSide) {
            "dx" -> com.narde.gestionaleosteopatabetto.domain.models.DominantSide.RIGHT
            "sx" -> com.narde.gestionaleosteopatabetto.domain.models.DominantSide.LEFT
            else -> com.narde.gestionaleosteopatabetto.domain.models.DominantSide.RIGHT
        }
        
        com.narde.gestionaleosteopatabetto.domain.models.AnthropometricData(
            height = heightInt,
            weight = weightDouble,
            bmi = bmiDouble,
            dominantSide = dominantSideEnum
        )
    } else {
        null
    }
    
    // Convert address if available
    val address = if (street.isNotBlank() || city.isNotBlank()) {
        com.narde.gestionaleosteopatabetto.domain.models.Address(
            street = street,
            city = city,
            zipCode = zipCode,
            province = province,
            country = country
        )
    } else {
        null
    }
    
    // Create privacy consents
    val privacyConsents = com.narde.gestionaleosteopatabetto.domain.models.PrivacyConsents(
        treatmentConsent = treatmentConsent,
        marketingConsent = marketingConsent,
        thirdPartyConsent = thirdPartyConsent,
        consentDate = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date.toString(),
        notes = null
    )
    
    // Create parent info for minors
    val parentInfo = if (isMinor) {
        val father = if (fatherFirstName.isNotBlank() || fatherLastName.isNotBlank()) {
            com.narde.gestionaleosteopatabetto.domain.models.Parent(
                firstName = fatherFirstName,
                lastName = fatherLastName
            )
        } else {
            null
        }
        
        val mother = if (motherFirstName.isNotBlank() || motherLastName.isNotBlank()) {
            com.narde.gestionaleosteopatabetto.domain.models.Parent(
                firstName = motherFirstName,
                lastName = motherLastName
            )
        } else {
            null
        }
        
        com.narde.gestionaleosteopatabetto.domain.models.ParentInfo(
            father = father,
            mother = mother
        )
    } else {
        null
    }
    
    // Create and return domain Patient model
    return com.narde.gestionaleosteopatabetto.domain.models.Patient(
        id = patientId,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDateParsed,
        gender = genderEnum,
        placeOfBirth = placeOfBirth,
        taxCode = taxCode,
        phone = phone,
        email = email,
        address = address,
        anthropometricData = anthropometricData,
        privacyConsents = privacyConsents,
        parentInfo = parentInfo
    )
}
