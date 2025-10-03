package com.narde.gestionaleosteopatabetto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.model.Visit
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.data.database.utils.createDatabaseUtils
import com.narde.gestionaleosteopatabetto.data.sample.SampleData

/**
 * Main ViewModel for the application
 * Handles navigation state and data loading
 */
class MainAppViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainAppUiState())
    val uiState: StateFlow<MainAppUiState> = _uiState.asStateFlow()
    
    // DatabaseUtils instance
    private val databaseUtils = createDatabaseUtils()
    
    init {
        initializeApp()
    }
    
    /**
     * Initialize application - load data from database
     */
    private fun initializeApp() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val (patients, visits) = loadData()
                _uiState.value = _uiState.value.copy(
                    patients = patients,
                    visits = visits,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    patients = SampleData.patients,
                    visits = SampleData.visits,
                    isLoading = false,
                    errorMessage = "Failed to load data: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Navigation: Change selected tab
     */
    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = index)
    }
    
    /**
     * Navigation: Show add patient screen
     */
    fun showAddPatientScreen() {
        _uiState.value = _uiState.value.copy(
            currentScreen = AppScreen.AddPatient
        )
    }
    
    /**
     * Navigation: Show patient details
     */
    fun showPatientDetails(patient: com.narde.gestionaleosteopatabetto.data.database.models.Patient) {
        _uiState.value = _uiState.value.copy(
            currentScreen = AppScreen.PatientDetails,
            selectedDatabasePatient = patient,
            selectedVisit = null
        )
    }
    
    /**
     * Navigation: Show visit details
     */
    fun showVisitDetails(visit: Visit) {
        _uiState.value = _uiState.value.copy(
            currentScreen = AppScreen.VisitDetails,
            selectedVisit = visit,
            selectedDatabasePatient = null
        )
    }
    
    /**
     * Navigation: Go back to main screen
     */
    fun navigateBack() {
        _uiState.value = _uiState.value.copy(
            currentScreen = AppScreen.Main,
            selectedDatabasePatient = null,
            selectedVisit = null
        )
    }
    
    /**
     * Data operation: Refresh patients list
     */
    fun refreshPatients() {
        viewModelScope.launch {
            try {
                val patients = loadPatients()
                _uiState.value = _uiState.value.copy(patients = patients)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to refresh patients: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    /**
     * Data operation: Delete a patient
     */
    fun deletePatient(patientId: String, patientName: String) {
        viewModelScope.launch {
            try {
                println("ViewModel: Starting deletion for patient ID: $patientId, Name: $patientName")
                
                if (isDatabaseSupported()) {
                    val repository = DatabaseInitializer.getPatientRepository()
                    if (repository != null) {
                        // First check if patient exists before deletion
                        val existingPatient = repository.getPatientById(patientId)
                        if (existingPatient != null) {
                            println("ViewModel: Patient found, proceeding with deletion")
                            repository.deletePatient(patientId)
                            
                            // Refresh the patients list after deletion
                            refreshPatients()
                            
                            _uiState.value = _uiState.value.copy(
                                errorMessage = "Patient $patientName deleted successfully"
                            )
                            println("ViewModel: Patient deletion completed successfully")
                        } else {
                            println("ViewModel: ERROR - Patient not found for deletion")
                            _uiState.value = _uiState.value.copy(
                                errorMessage = "Patient not found in database"
                            )
                        }
                    } else {
                        println("ViewModel: ERROR - Repository is null")
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Database repository not available"
                        )
                    }
                } else {
                    println("ViewModel: ERROR - Database not supported")
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Database not supported for deletion"
                    )
                }
            } catch (e: Exception) {
                println("ViewModel: ERROR during patient deletion: ${e.message}")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to delete patient: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Data operation: Update patient and refresh list
     */
    fun onPatientUpdated() {
        refreshPatients()
        _uiState.value = _uiState.value.copy(
            errorMessage = "Patient updated successfully"
        )
    }
    
    /**
     * Data layer: Load all data
     */
    private suspend fun loadData(): Pair<List<Patient>, List<Visit>> {
        val patients = loadPatients()
        val visits = SampleData.visits // TODO: Implement visits loading
        return Pair(patients, visits)
    }
    
    /**
     * Data layer: Load patients from database
     */
    private suspend fun loadPatients(): List<Patient> {
        return if (isDatabaseSupported()) {
            DatabaseInitializer.initializeDatabase()
            val repository = DatabaseInitializer.getPatientRepository()
            repository?.let { repo ->
                val dbPatients = repo.getAllPatients()
                dbPatients.map { databaseUtils.toUIPatient(it) }
            } ?: SampleData.patients
        } else {
            SampleData.patients
        }
    }
}

/**
 * UI State for main application
 */
data class MainAppUiState(
    val selectedTabIndex: Int = 0,
    val currentScreen: AppScreen = AppScreen.Main,
    val patients: List<Patient> = emptyList(),
    val visits: List<Visit> = emptyList(),
    val selectedDatabasePatient: com.narde.gestionaleosteopatabetto.data.database.models.Patient? = null,
    val selectedVisit: Visit? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Screen navigation states
 */
enum class AppScreen {
    Main,
    AddPatient,
    PatientDetails,
    VisitDetails
}