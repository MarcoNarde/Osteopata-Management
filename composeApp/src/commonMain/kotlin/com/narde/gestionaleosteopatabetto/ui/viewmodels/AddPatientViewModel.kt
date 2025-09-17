package com.narde.gestionaleosteopatabetto.ui.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.narde.gestionaleosteopatabetto.domain.models.*
import com.narde.gestionaleosteopatabetto.domain.usecases.SavePatientUseCase
import com.narde.gestionaleosteopatabetto.ui.mvi.BaseViewModel
import com.narde.gestionaleosteopatabetto.ui.mvi.AddPatientEvent
import com.narde.gestionaleosteopatabetto.ui.mvi.AddPatientState
import com.narde.gestionaleosteopatabetto.ui.mvi.AddPatientSideEffect
import com.narde.gestionaleosteopatabetto.utils.DateUtils

/**
 * ViewModel for Add Patient screen using MVI pattern
 * 
 * MVI Benefits:
 * - Unidirectional data flow
 * - Predictable state management
 * - Easy to test and debug
 * - Clear separation of concerns
 * - Business logic encapsulated in events
 */
class AddPatientViewModel(
    private val savePatientUseCase: SavePatientUseCase
) : BaseViewModel<AddPatientEvent, AddPatientState>() {
    
    // Legacy support - keep existing uiState for backward compatibility
    private val _legacyUiState = MutableStateFlow(AddPatientUiState())
    val uiState: StateFlow<AddPatientUiState> = _legacyUiState.asStateFlow()
    
    // MVI Implementation
    override fun initialState(): AddPatientState = AddPatientState()
    
    override suspend fun processIntent(intent: AddPatientEvent) {
        when (intent) {
            is AddPatientEvent.UpdateField -> handleUpdateField(intent.field, intent.value)
            is AddPatientEvent.UpdateConsent -> handleUpdateConsent(intent.consent, intent.value)
            is AddPatientEvent.ToggleParentSection -> handleToggleParentSection()
            is AddPatientEvent.SavePatient -> handleSavePatient()
            is AddPatientEvent.ClearError -> handleClearError()
            is AddPatientEvent.ValidateForm -> handleValidateForm()
        }
    }
    
    // MVI Event Handlers
    private fun handleUpdateField(field: PatientField, value: String) {
        updateState {
            val newState = when (field) {
                PatientField.FirstName -> copy(firstName = value)
                PatientField.LastName -> copy(lastName = value)
                PatientField.BirthDate -> {
                    val age = calculateAge(value)
                    val isMinor = age != null && age < 18
                    copy(
                        birthDate = value,
                        age = age,
                        isMinor = isMinor,
                        isParentSectionExpanded = if (!isMinor) false else isParentSectionExpanded
                    )
                }
                PatientField.Gender -> copy(gender = value)
                PatientField.PlaceOfBirth -> copy(placeOfBirth = value)
                PatientField.TaxCode -> copy(taxCode = value)
                PatientField.Phone -> copy(phone = value)
                PatientField.Email -> copy(email = value)
                PatientField.Street -> copy(street = value)
                PatientField.City -> copy(city = value)
                PatientField.ZipCode -> copy(zipCode = value)
                PatientField.Province -> copy(province = value)
                PatientField.Country -> copy(country = value)
                PatientField.Height -> copy(height = value)
                PatientField.Weight -> copy(weight = value)
                PatientField.BMI -> copy(bmi = value)
                PatientField.DominantSide -> copy(dominantSide = value)
                PatientField.FatherFirstName -> copy(fatherFirstName = value)
                PatientField.FatherLastName -> copy(fatherLastName = value)
                PatientField.MotherFirstName -> copy(motherFirstName = value)
                PatientField.MotherLastName -> copy(motherLastName = value)
            }
            
            // Clear error when user starts typing
            if (errorMessage.isNotEmpty()) {
                newState.copy(errorMessage = "")
            } else {
                newState
            }
        }
        
        // Update legacy state for backward compatibility
        updateLegacyState()
    }
    
    private fun handleUpdateConsent(consent: ConsentType, value: Boolean) {
        updateState {
            when (consent) {
                ConsentType.Treatment -> copy(treatmentConsent = value)
                ConsentType.Marketing -> copy(marketingConsent = value)
                ConsentType.ThirdParty -> copy(thirdPartyConsent = value)
            }
        }
        updateLegacyState()
    }
    
    private fun handleToggleParentSection() {
        updateState { copy(isParentSectionExpanded = !isParentSectionExpanded) }
        updateLegacyState()
    }
    
    private fun handleSavePatient() {
        val currentState = state.value
        
        // Validate form
        val validationResult = validatePatientData(currentState)
        if (!validationResult.isValid) {
            emitSideEffect(AddPatientSideEffect.ValidationError(validationResult.errorMessage))
            return
        }
        
        // Convert to domain model and save
        val patient = currentState.toDomainModel()
        
        updateState { copy(isSaving = true, errorMessage = "") }
        emitSideEffect(AddPatientSideEffect.SavingStarted)
        
        viewModelScope.launch {
            savePatientUseCase(patient).collect { result ->
                result.fold(
                    onSuccess = { savedPatient ->
                        updateState { copy(isSaving = false, errorMessage = "") }
                        emitSideEffect(AddPatientSideEffect.SavingCompleted)
                        emitSideEffect(AddPatientSideEffect.PatientSaved(savedPatient.id))
                    },
                    onFailure = { error ->
                        updateState { 
                            copy(
                                isSaving = false,
                                errorMessage = error.message ?: "Failed to save patient"
                            ) 
                        }
                        emitSideEffect(AddPatientSideEffect.SavingCompleted)
                        emitSideEffect(AddPatientSideEffect.ValidationError(error.message ?: "Failed to save patient"))
                    }
                )
            }
        }
        updateLegacyState()
    }
    
    private fun handleClearError() {
        updateState { copy(errorMessage = "") }
        updateLegacyState()
    }
    
    private fun handleValidateForm() {
        val currentState = state.value
        val validationResult = validatePatientData(currentState)
        updateState { copy(isFormValid = validationResult.isValid) }
        updateLegacyState()
    }
    
    // Legacy Methods (for backward compatibility)
    /**
     * Update form field value (Legacy method)
     */
    fun updateField(field: PatientField, value: String) {
        sendIntent(AddPatientEvent.UpdateField(field, value))
    }
    
    /**
     * Toggle parent section expansion (Legacy method)
     */
    fun toggleParentSection() {
        sendIntent(AddPatientEvent.ToggleParentSection)
    }
    
    /**
     * Update consent checkbox (Legacy method)
     */
    fun updateConsent(consent: ConsentType, value: Boolean) {
        sendIntent(AddPatientEvent.UpdateConsent(consent, value))
    }
    
    /**
     * Save patient using the use case (Legacy method)
     * Delegates business logic to the domain layer
     */
    fun savePatient(onSuccess: () -> Unit) {
        sendIntent(AddPatientEvent.SavePatient)
        // Note: onSuccess callback will be handled via side effects
    }
    
    /**
     * Calculate age from birth date in Italian DD/MM/AAAA format
     * This is UI-specific logic for form validation
     */
    private fun calculateAge(birthDateString: String): Int? {
        return DateUtils.calculateAgeFromItalianDate(birthDateString)
    }
    
    /**
     * Validate patient data using domain validation rules
     */
    private fun validatePatientData(state: AddPatientState): ValidationResult {
        return when {
            state.firstName.isBlank() -> ValidationResult.invalid("First name is required")
            state.lastName.isBlank() -> ValidationResult.invalid("Last name is required")
            state.phone.isBlank() -> ValidationResult.invalid("Phone number is required")
            !state.treatmentConsent -> ValidationResult.invalid("Treatment consent is required")
            else -> ValidationResult.valid()
        }
    }
    
    /**
     * Update legacy state for backward compatibility
     */
    private fun updateLegacyState() {
        val mviState = state.value
        _legacyUiState.value = AddPatientUiState(
            firstName = mviState.firstName,
            lastName = mviState.lastName,
            birthDate = mviState.birthDate,
            gender = mviState.gender,
            placeOfBirth = mviState.placeOfBirth,
            taxCode = mviState.taxCode,
            phone = mviState.phone,
            email = mviState.email,
            street = mviState.street,
            city = mviState.city,
            zipCode = mviState.zipCode,
            province = mviState.province,
            country = mviState.country,
            height = mviState.height,
            weight = mviState.weight,
            bmi = mviState.bmi,
            dominantSide = mviState.dominantSide,
            fatherFirstName = mviState.fatherFirstName,
            fatherLastName = mviState.fatherLastName,
            motherFirstName = mviState.motherFirstName,
            motherLastName = mviState.motherLastName,
            treatmentConsent = mviState.treatmentConsent,
            marketingConsent = mviState.marketingConsent,
            thirdPartyConsent = mviState.thirdPartyConsent,
            age = mviState.age,
            isMinor = mviState.isMinor,
            isParentSectionExpanded = mviState.isParentSectionExpanded,
            isSaving = mviState.isSaving,
            errorMessage = mviState.errorMessage
        )
    }
}

/**
 * Extension to convert MVI state to domain model
 * Follows Clean Architecture by converting UI data to domain entities
 */
private fun AddPatientState.toDomainModel(): Patient {
    val birthDate = DateUtils.parseItalianDate(birthDate)
    val gender = when (this.gender) {
        "M" -> Gender.MALE
        "F" -> Gender.FEMALE
        else -> Gender.MALE // Default fallback
    }
    
    val dominantSide = when (this.dominantSide) {
        "dx" -> DominantSide.RIGHT
        "sx" -> DominantSide.LEFT
        else -> DominantSide.RIGHT // Default fallback
    }
    
    val address = if (street.isNotBlank() || city.isNotBlank() || zipCode.isNotBlank()) {
        Address(
            street = street,
            city = city,
            zipCode = zipCode,
            province = province,
            country = country
        )
    } else null
    
    val anthropometricData = if (height.isNotBlank() || weight.isNotBlank()) {
        AnthropometricData(
            height = height.toIntOrNull() ?: 0,
            weight = weight.toDoubleOrNull() ?: 0.0,
            bmi = bmi.toDoubleOrNull(),
            dominantSide = dominantSide
        )
    } else null
    
    val privacyConsents = PrivacyConsents(
        treatmentConsent = treatmentConsent,
        marketingConsent = marketingConsent,
        thirdPartyConsent = thirdPartyConsent,
        consentDate = "2024-01-01", // TODO: Use proper date when kotlinx.datetime issues are resolved
        notes = null
    )
    
    val parentInfo = if (isMinor && (fatherFirstName.isNotBlank() || fatherLastName.isNotBlank() || 
                                    motherFirstName.isNotBlank() || motherLastName.isNotBlank())) {
        val father = if (fatherFirstName.isNotBlank() || fatherLastName.isNotBlank()) {
            Parent(fatherFirstName, fatherLastName)
        } else null
        
        val mother = if (motherFirstName.isNotBlank() || motherLastName.isNotBlank()) {
            Parent(motherFirstName, motherLastName)
        } else null
        
        ParentInfo(father, mother)
    } else null
    
    return Patient(
        id = "", // Will be generated by the use case
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        gender = gender,
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