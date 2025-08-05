package com.narde.gestionaleosteopatabetto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.toLocalDateTime
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.data.database.utils.*
import com.narde.gestionaleosteopatabetto.data.database.utils.DatabaseUtilsInterface
import com.narde.gestionaleosteopatabetto.utils.DateUtils

/**
 * ViewModel for Add Patient screen
 * Handles business logic, validation, and state management
 */
class AddPatientViewModel : ViewModel() {
    
    // Database utils instance
    private val _databaseUtils = createDatabaseUtils()
    
    private val _uiState = MutableStateFlow(AddPatientUiState())
    val uiState: StateFlow<AddPatientUiState> = _uiState.asStateFlow()
    
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
            
            // Address fields
            PatientField.Street -> _uiState.value.copy(street = value)
            PatientField.City -> _uiState.value.copy(city = value)
            PatientField.ZipCode -> _uiState.value.copy(zipCode = value)
            PatientField.Province -> _uiState.value.copy(province = value)
            PatientField.Country -> _uiState.value.copy(country = value)
            
            // Anthropometric fields
            PatientField.Height -> _uiState.value.copy(height = value)
            PatientField.Weight -> _uiState.value.copy(weight = value)
            PatientField.DominantSide -> _uiState.value.copy(dominantSide = value)
            
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
     * Toggle parent section expansion
     */
    fun toggleParentSection() {
        _uiState.value = _uiState.value.copy(
            isParentSectionExpanded = !_uiState.value.isParentSectionExpanded
        )
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
     * Validate and save patient
     */
    fun savePatient(onSuccess: () -> Unit) {
        val state = _uiState.value
        
        // Validation
        val validationResult = validatePatientData(state)
        if (!validationResult.isValid) {
            _uiState.value = state.copy(errorMessage = validationResult.errorMessage)
            return
        }
        
        // Save to database
        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, errorMessage = "")
            
            try {
                val success = savePatientToDatabase(state)
                if (success) {
                    onSuccess()
                } else {
                    _uiState.value = state.copy(
                        isSaving = false,
                        errorMessage = "Failed to save patient"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isSaving = false,
                    errorMessage = "Error: ${e.message}"
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
    private fun validatePatientData(state: AddPatientUiState): ValidationResult {
        return when {
            state.firstName.isBlank() -> ValidationResult.invalid("First name is required")
            state.lastName.isBlank() -> ValidationResult.invalid("Last name is required")
            state.phone.isBlank() -> ValidationResult.invalid("Phone number is required")
            !state.treatmentConsent -> ValidationResult.invalid("Treatment consent is required")
            else -> ValidationResult.valid()
        }
    }
    
    /**
     * Data layer: Save patient to database
     */
    private suspend fun savePatientToDatabase(state: AddPatientUiState): Boolean {
        return try {
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getPatientRepository()
                repository?.let {
                    // Get patient count for ID generation
                    val patientCount = it.getPatientsCount()
                    // Convert UI state to database model
                    val patient = state.toDatabaseModel(patientCount, _databaseUtils)
                    it.savePatient(patient)
                    true
                } ?: false
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}

/**
 * Extension to convert UI state to database model
 */
private fun AddPatientUiState.toDatabaseModel(patientCount: Long, databaseUtils: DatabaseUtilsInterface): com.narde.gestionaleosteopatabetto.data.database.models.Patient {
    // Generate patient ID using actual patient count
    val patientId = databaseUtils.generatePatientId(patientCount)
    
    return databaseUtils.createNewPatient(patientId).apply {
        datiPersonali?.apply {
            nome = firstName
            cognome = lastName
            // Convert Italian format (DD/MM/AAAA) to ISO format (YYYY-MM-DD) for database storage
            dataNascita = DateUtils.convertItalianToIsoFormat(birthDate)
            sesso = gender
            luogoNascita = placeOfBirth
            codiceFiscale = taxCode
            telefonoPaziente = phone
            emailPaziente = this@toDatabaseModel.email
            
            // Anthropometric measurements
            altezza = height.toIntOrNull() ?: 0
            peso = weight.toDoubleOrNull() ?: 0.0
            latoDominante = dominantSide
        }
        
        indirizzo?.apply {
            via = street
            citta = city
            cap = zipCode
            provincia = province
            nazione = country
            tipoIndirizzo = "residenza"
        }
        
        privacy?.apply {
            consensoTrattamento = treatmentConsent
            consensoMarketing = marketingConsent
            consensoTerzeparti = thirdPartyConsent
            dataConsenso = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date.toString()
        }
        
        // Add parent information only for minors
        if (isMinor && (fatherFirstName.isNotBlank() || fatherLastName.isNotBlank() || 
                        motherFirstName.isNotBlank() || motherLastName.isNotBlank())) {
            genitori?.apply {
                if (fatherFirstName.isNotBlank() || fatherLastName.isNotBlank()) {
                    padre?.apply {
                        nome = fatherFirstName
                        cognome = fatherLastName
                    }
                }
                if (motherFirstName.isNotBlank() || motherLastName.isNotBlank()) {
                    madre?.apply {
                        nome = motherFirstName
                        cognome = motherLastName
                    }
                }
            }
        }
    }
}