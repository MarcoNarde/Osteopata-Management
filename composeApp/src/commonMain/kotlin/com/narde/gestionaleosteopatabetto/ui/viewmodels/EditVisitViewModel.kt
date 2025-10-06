package com.narde.gestionaleosteopatabetto.ui.viewmodels

import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.model.Visit
import com.narde.gestionaleosteopatabetto.domain.usecases.UpdateVisitUseCase
import com.narde.gestionaleosteopatabetto.domain.usecases.GetVisitUseCase
import com.narde.gestionaleosteopatabetto.domain.models.Visit as DomainVisit
import com.narde.gestionaleosteopatabetto.ui.mvi.BaseViewModel
import com.narde.gestionaleosteopatabetto.ui.mvi.EditVisitEvent
import com.narde.gestionaleosteopatabetto.ui.mvi.EditVisitSideEffect
import com.narde.gestionaleosteopatabetto.ui.mvi.EditVisitState
import com.narde.gestionaleosteopatabetto.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart

/**
 * ViewModel for EditVisitScreen using MVI pattern
 * Manages state and business logic for editing existing visits
 * Loads visit by ID instead of storing it in constructor
 */
class EditVisitViewModel(
    private val patients: List<Patient>?,
    private val updateVisitUseCase: UpdateVisitUseCase,
    private val getVisitUseCase: GetVisitUseCase
) : BaseViewModel<EditVisitEvent, EditVisitState>() {

    // Safe patients list - never null - using function for maximum safety
    private fun getSafePatients(): List<Patient> {
        val result = patients ?: emptyList()
        println("EditVisitViewModel: getSafePatients() - patients: $patients")
        println("EditVisitViewModel: getSafePatients() - result: $result")
        println("EditVisitViewModel: getSafePatients() - result.size: ${result.size}")
        return result
    }

    override fun initialState(): EditVisitState {
        val safePatients = getSafePatients()
        println("EditVisitViewModel: Initializing state with ${safePatients.size} patients")
        
        return EditVisitState(
            visitId = "", // Will be set when LoadVisitById is called
            visit = null, // Will be loaded when LoadVisitById is called
            selectedPatient = null,
            visitDate = "",
            osteopath = "",
            generalNotes = "",
            filteredPatients = safePatients,
            isLoading = true // Start in loading state
        )
    }

    override suspend fun processIntent(intent: EditVisitEvent) {
        when (intent) {
            is EditVisitEvent.LoadVisitById -> handleLoadVisitById(intent.visitId)
            is EditVisitEvent.SaveVisit -> handleSaveVisit()
            is EditVisitEvent.CancelEdit -> handleCancelEdit()
            
            is EditVisitEvent.UpdatePatient -> handleUpdatePatient(intent.patient)
            is EditVisitEvent.UpdateVisitDate -> handleUpdateVisitDate(intent.date)
            is EditVisitEvent.UpdateGeneralNotes -> handleUpdateGeneralNotes(intent.notes)
            
            is EditVisitEvent.UpdateWeight -> handleUpdateWeight(intent.weight)
            is EditVisitEvent.UpdateBmi -> handleUpdateBmi(intent.bmi)
            is EditVisitEvent.UpdateBloodPressure -> handleUpdateBloodPressure(intent.pressure)
            is EditVisitEvent.UpdateCranialIndices -> handleUpdateCranialIndices(intent.indices)
            
            is EditVisitEvent.UpdateMainReason -> handleUpdateMainReason(intent.reason)
            is EditVisitEvent.UpdateMainReasonOnset -> handleUpdateMainReasonOnset(intent.onset)
            is EditVisitEvent.UpdateMainReasonPain -> handleUpdateMainReasonPain(intent.pain)
            is EditVisitEvent.UpdateMainReasonVas -> handleUpdateMainReasonVas(intent.vas)
            is EditVisitEvent.UpdateMainReasonFactors -> handleUpdateMainReasonFactors(intent.factors)
            
            is EditVisitEvent.UpdateSecondaryReason -> handleUpdateSecondaryReason(intent.reason)
            is EditVisitEvent.UpdateSecondaryReasonDuration -> handleUpdateSecondaryReasonDuration(intent.duration)
            is EditVisitEvent.UpdateSecondaryReasonVas -> handleUpdateSecondaryReasonVas(intent.vas)
            
            is EditVisitEvent.TogglePatientDropdown -> handleTogglePatientDropdown(intent.expanded)
            is EditVisitEvent.UpdatePatientSearchText -> handleUpdatePatientSearchText(intent.searchText)
            
            is EditVisitEvent.ClearError -> handleClearError()
        }
    }

    private suspend fun handleLoadVisitById(visitId: String) {
        println("EditVisitViewModel: Loading visit by ID: $visitId")
        
        updateState(
            state.value.copy(
                visitId = visitId,
                isLoading = true,
                errorMessage = null
            )
        )
        
        try {
            getVisitUseCase(visitId)
                .onStart {
                    println("EditVisitViewModel: Get visit use case started")
                }
                .catch { error ->
                    println("EditVisitViewModel: Get visit failed - ${error.message}")
                    error.printStackTrace()
                    
                    updateState(
                        state.value.copy(
                            isLoading = false,
                            errorMessage = "Errore durante il caricamento della visita: ${error.message}"
                        )
                    )
                    
                    emitSideEffect(EditVisitSideEffect.ShowError("Errore durante il caricamento della visita: ${error.message}"))
                }
                .collect { result ->
                    when {
                        result.isSuccess -> {
                            val domainVisit = result.getOrNull()
                            println("EditVisitViewModel: Visit loaded successfully - ID: ${domainVisit?.idVisita}")
                            
                            if (domainVisit != null) {
                                // Convert ISO date to Italian format for UI
                                val italianDate = DateUtils.convertIsoToItalianFormat(domainVisit.dataVisitaString)
                                println("EditVisitViewModel: Converting date from ISO '${domainVisit.dataVisitaString}' to Italian '$italianDate'")
                                
                                // Convert domain visit to UI visit
                                val uiVisit = Visit(
                                    idVisita = domainVisit.idVisita,
                                    idPaziente = domainVisit.idPaziente,
                                    dataVisita = italianDate, // Use Italian format for UI
                                    osteopata = domainVisit.osteopata,
                                    noteGenerali = domainVisit.noteGenerali
                                )
                                
                                val safePatients = getSafePatients()
                                val selectedPatient = safePatients.find { it.id == domainVisit.idPaziente }
                                
                                updateState(
                                    state.value.copy(
                                        visit = uiVisit,
                                        selectedPatient = selectedPatient,
                                        visitDate = italianDate, // Use Italian format for state
                                        osteopath = domainVisit.osteopata,
                                        generalNotes = domainVisit.noteGenerali,
                                        isLoading = false
                                    )
                                )
                                
                                validateForm()
                            } else {
                                updateState(
                                    state.value.copy(
                                        isLoading = false,
                                        errorMessage = "Visita non trovata"
                                    )
                                )
                                
                                emitSideEffect(EditVisitSideEffect.ShowError("Visita non trovata"))
                            }
                        }
                        result.isFailure -> {
                            val error = result.exceptionOrNull()?.message ?: "Unknown error"
                            println("EditVisitViewModel: Load failed - $error")
                            
                            updateState(
                                state.value.copy(
                                    isLoading = false,
                                    errorMessage = "Errore durante il caricamento della visita: $error"
                                )
                            )
                            
                            emitSideEffect(EditVisitSideEffect.ShowError("Errore durante il caricamento della visita: $error"))
                        }
                    }
                }
                
        } catch (e: Exception) {
            println("EditVisitViewModel: Exception in load process - ${e.message}")
            e.printStackTrace()
            
            updateState(
                state.value.copy(
                    isLoading = false,
                    errorMessage = "Errore durante il caricamento della visita: ${e.message}"
                )
            )
            
            emitSideEffect(EditVisitSideEffect.ShowError("Errore durante il caricamento della visita: ${e.message}"))
        }
    }

    private suspend fun handleSaveVisit() {
        println("EditVisitViewModel: Starting save visit process")
        
        updateState(
            state.value.copy(isSaving = true, errorMessage = null)
        )
        
        emitSideEffect(EditVisitSideEffect.SavingStarted)
        
        try {
            val domainVisit = createDomainVisitFromState()
            println("EditVisitViewModel: Created domain visit - ID: ${domainVisit.idVisita}")
            
            updateVisitUseCase(domainVisit)
                .onStart {
                    println("EditVisitViewModel: Update visit use case started")
                }
                .catch { error ->
                    println("EditVisitViewModel: Update visit failed - ${error.message}")
                    error.printStackTrace()
                    
                    updateState(
                        state.value.copy(
                            isSaving = false,
                            errorMessage = "Errore durante il salvataggio: ${error.message}"
                        )
                    )
                    
                    emitSideEffect(EditVisitSideEffect.ShowError("Errore durante il salvataggio: ${error.message}"))
                }
                .collect { result ->
                    when {
                        result.isSuccess -> {
                            val updatedVisit = result.getOrNull()
                            println("EditVisitViewModel: Visit updated successfully - ID: ${updatedVisit?.idVisita}")
                            
                            // Create UI visit for callback
                            val uiVisit = Visit(
                                idVisita = updatedVisit?.idVisita ?: domainVisit.idVisita,
                                idPaziente = updatedVisit?.idPaziente ?: domainVisit.idPaziente,
                                dataVisita = updatedVisit?.dataVisitaString ?: state.value.visitDate,
                                osteopata = updatedVisit?.osteopata ?: domainVisit.osteopata,
                                noteGenerali = updatedVisit?.noteGenerali ?: domainVisit.noteGenerali
                            )
                            
                            updateState(
                                state.value.copy(isSaving = false)
                            )
                            
                            emitSideEffect(EditVisitSideEffect.SavingCompleted)
                            emitSideEffect(EditVisitSideEffect.VisitUpdated(uiVisit))
                        }
                        result.isFailure -> {
                            val error = result.exceptionOrNull()?.message ?: "Unknown error"
                            println("EditVisitViewModel: Update failed - $error")
                            
                            updateState(
                                state.value.copy(
                                    isSaving = false,
                                    errorMessage = "Errore durante il salvataggio: $error"
                                )
                            )
                            
                            emitSideEffect(EditVisitSideEffect.ShowError("Errore durante il salvataggio: $error"))
                        }
                    }
                }
                
        } catch (e: Exception) {
            println("EditVisitViewModel: Exception in save process - ${e.message}")
            e.printStackTrace()
            
            updateState(
                state.value.copy(
                    isSaving = false,
                    errorMessage = "Errore durante il salvataggio: ${e.message}"
                )
            )
            
            emitSideEffect(EditVisitSideEffect.ShowError("Errore durante il salvataggio: ${e.message}"))
        }
    }

    private fun handleCancelEdit() {
        println("EditVisitViewModel: Cancel edit requested")
        emitSideEffect(EditVisitSideEffect.NavigateBack)
    }

        private fun createDomainVisitFromState(): DomainVisit {
            val currentState = state.value
            val visit = currentState.visit ?: throw IllegalStateException("Visit not loaded in state")

            println("EditVisitViewModel: Creating domain visit from state")
            println("EditVisitViewModel: - Visit ID: ${visit.idVisita}")
            println("EditVisitViewModel: - Patient ID: ${currentState.selectedPatient?.id}")
            println("EditVisitViewModel: - Visit Date: ${currentState.visitDate}")
            println("EditVisitViewModel: - Osteopath: ${currentState.osteopath}")
            println("EditVisitViewModel: - General Notes: ${currentState.generalNotes}")

            return DomainVisit(
                idVisita = visit.idVisita, // Keep existing ID
                idPaziente = currentState.selectedPatient?.id ?: visit.idPaziente,
                dataVisita = DateUtils.parseItalianDate(currentState.visitDate)
                    ?: throw IllegalArgumentException("Invalid date format: ${currentState.visitDate}"),
                osteopata = currentState.osteopath,
                noteGenerali = currentState.generalNotes,
                datiVisitaCorrente = null, // TODO: Implement when needed
                motivoConsulto = null // TODO: Implement when needed
            )
        }

    private fun validateForm() {
        val currentState = state.value
        
        val isFormValid = currentState.selectedPatient != null && 
                         currentState.visitDate.isNotBlank() &&
                         currentState.osteopath.isNotBlank()
        
        updateState(
            state.value.copy(isFormValid = isFormValid)
        )
        
        println("EditVisitViewModel: Form validation - isValid: $isFormValid")
    }

    // Form field update handlers
    private fun handleUpdatePatient(patient: Patient?) {
        updateState(
            state.value.copy(selectedPatient = patient)
        )
        validateForm()
    }

    private fun handleUpdateVisitDate(date: String) {
        updateState(
            state.value.copy(visitDate = date)
        )
        validateForm()
    }

    private fun handleUpdateGeneralNotes(notes: String) {
        updateState(
            state.value.copy(generalNotes = notes)
        )
    }

    private fun handleUpdateWeight(weight: String) {
        updateState(
            state.value.copy(weight = weight)
        )
    }

    private fun handleUpdateBmi(bmi: String) {
        updateState(
            state.value.copy(bmi = bmi)
        )
    }

    private fun handleUpdateBloodPressure(pressure: String) {
        updateState(
            state.value.copy(bloodPressure = pressure)
        )
    }

    private fun handleUpdateCranialIndices(indices: String) {
        updateState(
            state.value.copy(cranialIndices = indices)
        )
    }

    private fun handleUpdateMainReason(reason: String) {
        updateState(
            state.value.copy(mainReason = reason)
        )
    }

    private fun handleUpdateMainReasonOnset(onset: String) {
        updateState(
            state.value.copy(mainReasonOnset = onset)
        )
    }

    private fun handleUpdateMainReasonPain(pain: String) {
        updateState(
            state.value.copy(mainReasonPain = pain)
        )
    }

    private fun handleUpdateMainReasonVas(vas: String) {
        updateState(
            state.value.copy(mainReasonVas = vas)
        )
    }

    private fun handleUpdateMainReasonFactors(factors: String) {
        updateState(
            state.value.copy(mainReasonFactors = factors)
        )
    }

    private fun handleUpdateSecondaryReason(reason: String) {
        updateState(
            state.value.copy(secondaryReason = reason)
        )
    }

    private fun handleUpdateSecondaryReasonDuration(duration: String) {
        updateState(
            state.value.copy(secondaryReasonDuration = duration)
        )
    }

    private fun handleUpdateSecondaryReasonVas(vas: String) {
        updateState(
            state.value.copy(secondaryReasonVas = vas)
        )
    }

    private fun handleTogglePatientDropdown(expanded: Boolean) {
        updateState(
            state.value.copy(patientDropdownExpanded = expanded)
        )
    }

    private fun handleUpdatePatientSearchText(searchText: String) {
        println("EditVisitViewModel: Updating patient search text: '$searchText'")
        
        val safePatients = getSafePatients()
        println("EditVisitViewModel: Current patients list size: ${safePatients.size}")
        
        val filteredPatients = if (searchText.isEmpty()) {
            safePatients
        } else {
            safePatients.filter { patient ->
                patient.name.contains(searchText, ignoreCase = true) ||
                patient.id.contains(searchText, ignoreCase = true)
            }
        }
        
        println("EditVisitViewModel: Filtered patients count: ${filteredPatients.size}")

        updateState(
            state.value.copy(
                patientSearchText = searchText,
                filteredPatients = filteredPatients
            )
        )
    }

    private fun handleClearError() {
        updateState(
            state.value.copy(errorMessage = null)
        )
    }
}
