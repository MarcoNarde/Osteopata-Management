package com.narde.gestionaleosteopatabetto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.data.database.models.Patient as DatabasePatient
import com.narde.gestionaleosteopatabetto.data.database.utils.*
import com.narde.gestionaleosteopatabetto.utils.DateUtils

/**
 * ViewModel for Edit Patient functionality
 * Handles business logic, validation, and state management for patient updates
 */
class EditPatientViewModel : ViewModel() {
    
    // Database utils instance
    private val _databaseUtils = createDatabaseUtils()
    
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
     * Validate and update patient
     */
    fun updatePatient(onSuccess: () -> Unit) {
        val state = _uiState.value
        
        // Validation
        val validationResult = validatePatientData(state)
        if (!validationResult.isValid) {
            _uiState.value = state.copy(errorMessage = validationResult.errorMessage)
            return
        }
        
        // Update database
        viewModelScope.launch {
            _uiState.value = state.copy(isUpdating = true, errorMessage = "", isUpdateSuccessful = false)
            
            try {
                val success = updatePatientInDatabase(state)
                if (success) {
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
                } else {
                    _uiState.value = state.copy(
                        isUpdating = false,
                        errorMessage = "Failed to update patient",
                        isUpdateSuccessful = false
                    )
                }
            } catch (e: Exception) {
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
    
    /**
     * Data layer: Update patient in database
     */
    private suspend fun updatePatientInDatabase(state: EditPatientUiState): Boolean {
        return try {
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getPatientRepository()
                repository?.let {
                    // Convert UI state to database model for update
                    val updatedPatient = state.toDatabaseModel(_databaseUtils)
                    it.savePatient(updatedPatient) // savePatient handles both create and update
                    true
                } ?: false
            } else {
                false
            }
        } catch (e: Exception) {
            println("Error updating patient: ${e.message}")
            false
        }
    }
}

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
 * Extension to convert UI state to database model for update
 */
private fun EditPatientUiState.toDatabaseModel(databaseUtils: DatabaseUtilsInterface): DatabasePatient {
    // Create updated patient using existing ID
    return databaseUtils.createNewPatient(patientId).apply {
        // Override ID to maintain the existing patient ID
        idPaziente = this@toDatabaseModel.patientId
        
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
            dataConsenso = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
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