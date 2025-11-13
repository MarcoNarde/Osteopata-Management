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
import kotlinx.coroutines.flow.catch
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

    // Store original state snapshot for change detection
    private var originalStateSnapshot: EditVisitState? = null

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
                                
                                // Extract extended visit data fields
                                val weight = domainVisit.datiVisitaCorrente?.peso?.toString() ?: ""
                                val bmi = domainVisit.datiVisitaCorrente?.bmi?.toString() ?: ""
                                val bloodPressure = domainVisit.datiVisitaCorrente?.pressione ?: ""
                                val cranialIndices = domainVisit.datiVisitaCorrente?.indiciCraniali?.toString() ?: ""
                                
                                // Extract main reason fields
                                val mainReason = domainVisit.motivoConsulto?.principale?.descrizione ?: ""
                                val mainReasonOnset = domainVisit.motivoConsulto?.principale?.insorgenza ?: ""
                                val mainReasonPain = domainVisit.motivoConsulto?.principale?.dolore ?: ""
                                val mainReasonVas = domainVisit.motivoConsulto?.principale?.vas?.toString() ?: ""
                                val mainReasonFactors = domainVisit.motivoConsulto?.principale?.fattori ?: ""
                                
                                // Extract secondary reason fields
                                val secondaryReason = domainVisit.motivoConsulto?.secondario?.descrizione ?: ""
                                val secondaryReasonDuration = domainVisit.motivoConsulto?.secondario?.durata ?: ""
                                val secondaryReasonVas = domainVisit.motivoConsulto?.secondario?.vas?.toString() ?: ""
                                
                                // Create updated state with all loaded fields
                                val loadedState = state.value.copy(
                                    visit = uiVisit,
                                    selectedPatient = selectedPatient,
                                    visitDate = italianDate, // Use Italian format for state
                                    osteopath = domainVisit.osteopata,
                                    generalNotes = domainVisit.noteGenerali,
                                    weight = weight,
                                    bmi = bmi,
                                    bloodPressure = bloodPressure,
                                    cranialIndices = cranialIndices,
                                    mainReason = mainReason,
                                    mainReasonOnset = mainReasonOnset,
                                    mainReasonPain = mainReasonPain,
                                    mainReasonVas = mainReasonVas,
                                    mainReasonFactors = mainReasonFactors,
                                    secondaryReason = secondaryReason,
                                    secondaryReasonDuration = secondaryReasonDuration,
                                    secondaryReasonVas = secondaryReasonVas,
                                    isLoading = false,
                                    hasChanges = false // No changes yet when first loaded
                                )
                                
                                // Store original state snapshot for change detection
                                originalStateSnapshot = loadedState.copy()
                                
                                updateState(loadedState)
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
                            
                            // Use updated visit if available, otherwise use domain visit
                            val visitToUse = updatedVisit ?: domainVisit
                            
                            // Convert datiVisitaCorrente from domain model to UI model
                            val datiVisitaCorrente = visitToUse.datiVisitaCorrente?.let { currentData ->
                                com.narde.gestionaleosteopatabetto.data.model.DatiVisitaCorrente(
                                    peso = currentData.peso,
                                    bmi = currentData.bmi,
                                    pressione = currentData.pressione,
                                    indiciCraniali = currentData.indiciCraniali
                                )
                            }
                            
                            // Convert motivoConsulto from domain model to UI model
                            val motivoConsulto = visitToUse.motivoConsulto?.let { motivo ->
                                // Convert motivoPrincipale if present
                                val principale = motivo.principale?.let { principale ->
                                    com.narde.gestionaleosteopatabetto.data.model.MotivoPrincipale(
                                        descrizione = principale.descrizione,
                                        insorgenza = principale.insorgenza,
                                        dolore = principale.dolore,
                                        vas = principale.vas,
                                        fattori = principale.fattori
                                    )
                                }
                                
                                // Convert motivoSecondario if present
                                val secondario = motivo.secondario?.let { secondario ->
                                    com.narde.gestionaleosteopatabetto.data.model.MotivoSecondario(
                                        descrizione = secondario.descrizione,
                                        durata = secondario.durata,
                                        vas = secondario.vas
                                    )
                                }
                                
                                // Create MotivoConsulto only if at least one reason exists
                                if (principale != null || secondario != null) {
                                    com.narde.gestionaleosteopatabetto.data.model.MotivoConsulto(
                                        principale = principale,
                                        secondario = secondario
                                    )
                                } else {
                                    null
                                }
                            }
                            
                            // Convert date from ISO format to Italian format for UI display
                            val italianDate = if (visitToUse.dataVisitaString.isNotEmpty()) {
                                DateUtils.convertIsoToItalianFormat(visitToUse.dataVisitaString)
                                    .takeIf { it.isNotEmpty() } ?: state.value.visitDate
                            } else {
                                state.value.visitDate
                            }
                            
                            // Create complete UI visit with all fields for callback
                            val uiVisit = Visit(
                                idVisita = visitToUse.idVisita,
                                idPaziente = visitToUse.idPaziente,
                                dataVisita = italianDate,
                                osteopata = visitToUse.osteopata,
                                datiVisitaCorrente = datiVisitaCorrente,
                                motivoConsulto = motivoConsulto,
                                noteGenerali = visitToUse.noteGenerali
                            )
                            
                            // Update state and reset change tracking after successful save
                            val currentState = state.value
                            val updatedState = currentState.copy(
                                isSaving = false,
                                hasChanges = false // Reset changes flag after successful save
                            )
                            
                            // Update original snapshot to current state since save was successful
                            originalStateSnapshot = updatedState.copy()
                            
                            updateState(updatedState)
                            
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

            // Create datiVisitaCorrente from state if any field is provided
            val datiVisitaCorrente = if (
                currentState.weight.isNotBlank() ||
                currentState.bmi.isNotBlank() ||
                currentState.bloodPressure.isNotBlank() ||
                currentState.cranialIndices.isNotBlank()
            ) {
                com.narde.gestionaleosteopatabetto.domain.models.DatiVisitaCorrente(
                    peso = currentState.weight.toDoubleOrNull() ?: 0.0,
                    bmi = currentState.bmi.toDoubleOrNull() ?: 0.0,
                    pressione = currentState.bloodPressure,
                    indiciCraniali = currentState.cranialIndices.toDoubleOrNull() ?: 0.0
                )
            } else {
                null
            }

            // Create motivoPrincipale from state if any field is provided
            val motivoPrincipale = if (
                currentState.mainReason.isNotBlank() ||
                currentState.mainReasonOnset.isNotBlank() ||
                currentState.mainReasonPain.isNotBlank() ||
                currentState.mainReasonVas.isNotBlank() ||
                currentState.mainReasonFactors.isNotBlank()
            ) {
                com.narde.gestionaleosteopatabetto.domain.models.MotivoPrincipale(
                    descrizione = currentState.mainReason,
                    insorgenza = currentState.mainReasonOnset,
                    dolore = currentState.mainReasonPain,
                    vas = currentState.mainReasonVas.toIntOrNull() ?: 0,
                    fattori = currentState.mainReasonFactors
                )
            } else {
                null
            }

            // Create motivoSecondario from state if any field is provided
            val motivoSecondario = if (
                currentState.secondaryReason.isNotBlank() ||
                currentState.secondaryReasonDuration.isNotBlank() ||
                currentState.secondaryReasonVas.isNotBlank()
            ) {
                com.narde.gestionaleosteopatabetto.domain.models.MotivoSecondario(
                    descrizione = currentState.secondaryReason,
                    durata = currentState.secondaryReasonDuration,
                    vas = currentState.secondaryReasonVas.toIntOrNull() ?: 0
                )
            } else {
                null
            }

            // Create motivoConsulto if either main or secondary reason exists
            val motivoConsulto = if (motivoPrincipale != null || motivoSecondario != null) {
                com.narde.gestionaleosteopatabetto.domain.models.MotivoConsulto(
                    principale = motivoPrincipale,
                    secondario = motivoSecondario
                )
            } else {
                null
            }

            return DomainVisit(
                idVisita = visit.idVisita, // Keep existing ID
                idPaziente = currentState.selectedPatient?.id ?: visit.idPaziente,
                dataVisita = DateUtils.parseItalianDate(currentState.visitDate)
                    ?: throw IllegalArgumentException("Invalid date format: ${currentState.visitDate}"),
                osteopata = currentState.osteopath,
                noteGenerali = currentState.generalNotes,
                datiVisitaCorrente = datiVisitaCorrente,
                motivoConsulto = motivoConsulto
            )
        }

    // Validation removed for now - no form validation needed
    // private fun validateForm() { ... }

    /**
     * Check if current state has any changes compared to original loaded state
     * Compares all editable fields to detect modifications
     */
    private fun checkForChanges() {
        val currentState = state.value
        val original = originalStateSnapshot
        
        // If no original snapshot exists, no changes can be detected
        if (original == null) {
            updateState(currentState.copy(hasChanges = false))
            return
        }
        
        // Compare all editable fields
        val hasChanges = (
            // Basic fields
            (currentState.selectedPatient?.id ?: "") != (original.selectedPatient?.id ?: "") ||
            currentState.visitDate != original.visitDate ||
            currentState.osteopath != original.osteopath ||
            currentState.generalNotes != original.generalNotes ||
            // Visit data fields
            currentState.weight != original.weight ||
            currentState.bmi != original.bmi ||
            currentState.bloodPressure != original.bloodPressure ||
            currentState.cranialIndices != original.cranialIndices ||
            // Main reason fields
            currentState.mainReason != original.mainReason ||
            currentState.mainReasonOnset != original.mainReasonOnset ||
            currentState.mainReasonPain != original.mainReasonPain ||
            currentState.mainReasonVas != original.mainReasonVas ||
            currentState.mainReasonFactors != original.mainReasonFactors ||
            // Secondary reason fields
            currentState.secondaryReason != original.secondaryReason ||
            currentState.secondaryReasonDuration != original.secondaryReasonDuration ||
            currentState.secondaryReasonVas != original.secondaryReasonVas
        )
        
        updateState(currentState.copy(hasChanges = hasChanges))
        
        println("EditVisitViewModel: Change detection - hasChanges: $hasChanges")
    }

    // Form field update handlers
    // Each handler updates state and then checks for changes
    private fun handleUpdatePatient(patient: Patient?) {
        updateState(
            state.value.copy(selectedPatient = patient)
        )
        checkForChanges()
    }

    private fun handleUpdateVisitDate(date: String) {
        updateState(
            state.value.copy(visitDate = date)
        )
        checkForChanges()
    }

    private fun handleUpdateGeneralNotes(notes: String) {
        updateState(
            state.value.copy(generalNotes = notes)
        )
        checkForChanges()
    }

    private fun handleUpdateWeight(weight: String) {
        updateState(
            state.value.copy(weight = weight)
        )
        checkForChanges()
    }

    private fun handleUpdateBmi(bmi: String) {
        updateState(
            state.value.copy(bmi = bmi)
        )
        checkForChanges()
    }

    private fun handleUpdateBloodPressure(pressure: String) {
        updateState(
            state.value.copy(bloodPressure = pressure)
        )
        checkForChanges()
    }

    private fun handleUpdateCranialIndices(indices: String) {
        updateState(
            state.value.copy(cranialIndices = indices)
        )
        checkForChanges()
    }

    private fun handleUpdateMainReason(reason: String) {
        updateState(
            state.value.copy(mainReason = reason)
        )
        checkForChanges()
    }

    private fun handleUpdateMainReasonOnset(onset: String) {
        updateState(
            state.value.copy(mainReasonOnset = onset)
        )
        checkForChanges()
    }

    private fun handleUpdateMainReasonPain(pain: String) {
        updateState(
            state.value.copy(mainReasonPain = pain)
        )
        checkForChanges()
    }

    private fun handleUpdateMainReasonVas(vas: String) {
        updateState(
            state.value.copy(mainReasonVas = vas)
        )
        checkForChanges()
    }

    private fun handleUpdateMainReasonFactors(factors: String) {
        updateState(
            state.value.copy(mainReasonFactors = factors)
        )
        checkForChanges()
    }

    private fun handleUpdateSecondaryReason(reason: String) {
        updateState(
            state.value.copy(secondaryReason = reason)
        )
        checkForChanges()
    }

    private fun handleUpdateSecondaryReasonDuration(duration: String) {
        updateState(
            state.value.copy(secondaryReasonDuration = duration)
        )
        checkForChanges()
    }

    private fun handleUpdateSecondaryReasonVas(vas: String) {
        updateState(
            state.value.copy(secondaryReasonVas = vas)
        )
        checkForChanges()
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
