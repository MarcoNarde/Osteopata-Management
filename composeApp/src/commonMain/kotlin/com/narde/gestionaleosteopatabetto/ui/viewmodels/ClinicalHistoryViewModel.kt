package com.narde.gestionaleosteopatabetto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.data.database.models.Patient as DatabasePatient
import com.narde.gestionaleosteopatabetto.data.database.models.*
import com.narde.gestionaleosteopatabetto.data.database.utils.createDatabaseUtils
import io.realm.kotlin.ext.realmListOf

/**
 * ViewModel for Clinical History editing functionality
 * Handles business logic, validation, and state management for clinical history updates
 */
class ClinicalHistoryViewModel : ViewModel() {
    
    // Database utils instance
    private val _databaseUtils = createDatabaseUtils()
    
    private val _uiState = MutableStateFlow(ClinicalHistoryUiState())
    val uiState: StateFlow<ClinicalHistoryUiState> = _uiState.asStateFlow()
    
    /**
     * Initialize the form with existing clinical history data
     */
    fun initializeWithPatient(dbPatient: DatabasePatient) {
        val storiaClinica = dbPatient.storiaClinica
        
        val state = ClinicalHistoryUiState(
            patientId = dbPatient.idPaziente,
            
            // Chronic conditions
            hasDrugAllergies = storiaClinica?.patologieCroniche?.allergieFarmaci?.presente ?: false,
            drugAllergiesList = storiaClinica?.patologieCroniche?.allergieFarmaci?.listaAllergie?.joinToString(", ") ?: "",
            hasDiabetes = storiaClinica?.patologieCroniche?.diabete?.presente ?: false,
            diabetesType = storiaClinica?.patologieCroniche?.diabete?.tipologia ?: "",
            hasHyperthyroidism = storiaClinica?.patologieCroniche?.ipertiroidismo ?: false,
            hasHeartDisease = storiaClinica?.patologieCroniche?.cardiopatia ?: false,
            hasHypertension = storiaClinica?.patologieCroniche?.ipertensioneArteriosa ?: false,
            
            // Lifestyle factors
            smokingStatus = storiaClinica?.stileVita?.tabagismo?.stato ?: "",
            cigarettesPerDay = storiaClinica?.stileVita?.tabagismo?.sigaretteGiorno?.toString() ?: "",
            yearsSmoking = storiaClinica?.stileVita?.tabagismo?.anniFumo?.toString() ?: "",
            workType = storiaClinica?.stileVita?.lavoro ?: "",
            profession = storiaClinica?.stileVita?.professione ?: "",
            workHoursPerDay = storiaClinica?.stileVita?.oreLavoroGiorno?.toString() ?: "",
            hasPhysicalActivity = storiaClinica?.stileVita?.attivitaSportiva?.presente ?: false,
            sportsList = storiaClinica?.stileVita?.attivitaSportiva?.sport?.joinToString(", ") ?: "",
            activityFrequency = storiaClinica?.stileVita?.attivitaSportiva?.frequenza ?: "",
            activityIntensity = storiaClinica?.stileVita?.attivitaSportiva?.intensita ?: "",
            
            // Pediatric history
            pregnancyComplications = storiaClinica?.anamnesiPediatrica?.gravidanza?.complicazioni ?: false,
            pregnancyNotes = storiaClinica?.anamnesiPediatrica?.gravidanza?.note ?: "",
            birthType = storiaClinica?.anamnesiPediatrica?.parto?.tipo ?: "",
            birthComplications = storiaClinica?.anamnesiPediatrica?.parto?.complicazioni ?: false,
            birthWeight = storiaClinica?.anamnesiPediatrica?.parto?.pesoNascitaGrammi?.toString() ?: "",
            apgarScore = storiaClinica?.anamnesiPediatrica?.parto?.punteggioApgar5min?.toString() ?: "",
            birthNotes = storiaClinica?.anamnesiPediatrica?.parto?.note ?: "",
            firstStepsMonths = storiaClinica?.anamnesiPediatrica?.sviluppo?.primiPassiMesi?.toString() ?: "",
            firstWordsMonths = storiaClinica?.anamnesiPediatrica?.sviluppo?.primeParoleMesi?.toString() ?: "",
            developmentProblems = storiaClinica?.anamnesiPediatrica?.sviluppo?.problemiSviluppo ?: false,
            developmentNotes = storiaClinica?.anamnesiPediatrica?.sviluppo?.note ?: "",
            pediatricGeneralNotes = storiaClinica?.anamnesiPediatrica?.noteGenerali ?: ""
        )
        
        _uiState.value = state
    }
    
    /**
     * Update field values
     */
    fun updateField(field: ClinicalHistoryField, value: String) {
        val currentState = _uiState.value
        _uiState.value = when (field) {
            ClinicalHistoryField.DrugAllergiesList -> currentState.copy(drugAllergiesList = value)
            ClinicalHistoryField.DiabetesType -> currentState.copy(diabetesType = value)
            ClinicalHistoryField.SmokingStatus -> currentState.copy(smokingStatus = value)
            ClinicalHistoryField.CigarettesPerDay -> currentState.copy(cigarettesPerDay = value)
            ClinicalHistoryField.YearsSmoking -> currentState.copy(yearsSmoking = value)
            ClinicalHistoryField.WorkType -> currentState.copy(workType = value)
            ClinicalHistoryField.Profession -> currentState.copy(profession = value)
            ClinicalHistoryField.WorkHoursPerDay -> currentState.copy(workHoursPerDay = value)
            ClinicalHistoryField.SportsList -> currentState.copy(sportsList = value)
            ClinicalHistoryField.ActivityFrequency -> currentState.copy(activityFrequency = value)
            ClinicalHistoryField.ActivityIntensity -> currentState.copy(activityIntensity = value)
            ClinicalHistoryField.PregnancyNotes -> currentState.copy(pregnancyNotes = value)
            ClinicalHistoryField.BirthType -> currentState.copy(birthType = value)
            ClinicalHistoryField.BirthWeight -> currentState.copy(birthWeight = value)
            ClinicalHistoryField.ApgarScore -> currentState.copy(apgarScore = value)
            ClinicalHistoryField.BirthNotes -> currentState.copy(birthNotes = value)
            ClinicalHistoryField.FirstStepsMonths -> currentState.copy(firstStepsMonths = value)
            ClinicalHistoryField.FirstWordsMonths -> currentState.copy(firstWordsMonths = value)
            ClinicalHistoryField.DevelopmentNotes -> currentState.copy(developmentNotes = value)
            ClinicalHistoryField.PediatricGeneralNotes -> currentState.copy(pediatricGeneralNotes = value)
        }
    }
    
    /**
     * Update boolean field values
     */
    fun updateBooleanField(field: ClinicalHistoryBooleanField, value: Boolean) {
        val currentState = _uiState.value
        _uiState.value = when (field) {
            ClinicalHistoryBooleanField.HasDrugAllergies -> currentState.copy(hasDrugAllergies = value)
            ClinicalHistoryBooleanField.HasDiabetes -> currentState.copy(hasDiabetes = value)
            ClinicalHistoryBooleanField.HasHyperthyroidism -> currentState.copy(hasHyperthyroidism = value)
            ClinicalHistoryBooleanField.HasHeartDisease -> currentState.copy(hasHeartDisease = value)
            ClinicalHistoryBooleanField.HasHypertension -> currentState.copy(hasHypertension = value)
            ClinicalHistoryBooleanField.HasPhysicalActivity -> currentState.copy(hasPhysicalActivity = value)
            ClinicalHistoryBooleanField.PregnancyComplications -> currentState.copy(pregnancyComplications = value)
            ClinicalHistoryBooleanField.BirthComplications -> currentState.copy(birthComplications = value)
            ClinicalHistoryBooleanField.DevelopmentProblems -> currentState.copy(developmentProblems = value)
        }
    }
    
    /**
     * Validate and update clinical history
     */
    fun updateClinicalHistory(onSuccess: () -> Unit) {
        val state = _uiState.value
        
        // Update database
        viewModelScope.launch {
            _uiState.value = state.copy(isUpdating = true, errorMessage = "", isUpdateSuccessful = false)
            
            try {
                val success = updateClinicalHistoryInDatabase(state)
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
                        errorMessage = "Failed to update clinical history",
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
     * Data layer: Update clinical history in database
     */
    private suspend fun updateClinicalHistoryInDatabase(state: ClinicalHistoryUiState): Boolean {
        return try {
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getPatientRepository()
                repository?.let {
                    val patient = it.getPatientById(state.patientId)
                    if (patient != null) {
                        // Update clinical history
                        patient.storiaClinica = state.toStoriaClinica()
                        it.savePatient(patient)
                        true
                    } else {
                        false
                    }
                } ?: false
            } else {
                false
            }
        } catch (e: Exception) {
            println("Error updating clinical history: ${e.message}")
            false
        }
    }
}

/**
 * Extension to convert UI state to StoriaClinica model
 */
private fun ClinicalHistoryUiState.toStoriaClinica(): StoriaClinica {
    return StoriaClinica().apply {
        // Chronic conditions
        patologieCroniche = PatologieCroniche().apply {
            allergieFarmaci = AllergieFarmaci().apply {
                presente = hasDrugAllergies
                listaAllergie = if (drugAllergiesList.isNotEmpty()) {
                    realmListOf(*drugAllergiesList.split(",").map { it.trim() }.toTypedArray())
                } else {
                    realmListOf()
                }
            }
            diabete = Diabete().apply {
                presente = hasDiabetes
                tipologia = diabetesType
            }
            ipertiroidismo = hasHyperthyroidism
            cardiopatia = hasHeartDisease
            ipertensioneArteriosa = hasHypertension
        }
        
        // Lifestyle factors
        stileVita = StileVita().apply {
            tabagismo = Tabagismo().apply {
                stato = smokingStatus
                sigaretteGiorno = cigarettesPerDay.toIntOrNull() ?: 0
                anniFumo = yearsSmoking.toIntOrNull() ?: 0
            }
            lavoro = workType
            professione = profession
            oreLavoroGiorno = workHoursPerDay.toIntOrNull() ?: 0
            attivitaSportiva = AttivitaSportiva().apply {
                presente = hasPhysicalActivity
                sport = if (sportsList.isNotEmpty()) {
                    realmListOf(*sportsList.split(",").map { it.trim() }.toTypedArray())
                } else {
                    realmListOf()
                }
                frequenza = activityFrequency
                intensita = activityIntensity
            }
        }
        
        // Pediatric history
        anamnesiPediatrica = AnamnesiPediatrica().apply {
            gravidanza = Gravidanza().apply {
                complicazioni = pregnancyComplications
                note = pregnancyNotes
            }
            parto = Parto().apply {
                tipo = birthType
                complicazioni = birthComplications
                pesoNascitaGrammi = birthWeight.toIntOrNull() ?: 0
                punteggioApgar5min = apgarScore.toIntOrNull() ?: 0
                note = birthNotes
            }
            sviluppo = Sviluppo().apply {
                primiPassiMesi = firstStepsMonths.toIntOrNull() ?: 0
                primeParoleMesi = firstWordsMonths.toIntOrNull() ?: 0
                problemiSviluppo = developmentProblems
                note = developmentNotes
            }
            noteGenerali = pediatricGeneralNotes
        }
    }
}

