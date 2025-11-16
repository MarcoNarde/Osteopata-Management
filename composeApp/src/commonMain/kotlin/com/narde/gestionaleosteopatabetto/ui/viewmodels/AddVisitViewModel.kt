package com.narde.gestionaleosteopatabetto.ui.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.ui.mvi.BaseViewModel
import com.narde.gestionaleosteopatabetto.ui.mvi.AddVisitEvent
import com.narde.gestionaleosteopatabetto.ui.mvi.AddVisitState
import com.narde.gestionaleosteopatabetto.ui.mvi.AddVisitSideEffect

/**
 * ViewModel for Add Visit screen using MVI pattern
 * 
 * MVI Benefits:
 * - Unidirectional data flow
 * - Predictable state management
 * - Easy to test and debug
 * - Clear separation of concerns
 * - Business logic encapsulated in events
 */
class AddVisitViewModel(
    private val savePatientUseCase: com.narde.gestionaleosteopatabetto.domain.usecases.SavePatientUseCase,
    private val getVisitsByPatientUseCase: com.narde.gestionaleosteopatabetto.domain.usecases.GetVisitsByPatientUseCase,
    private val saveVisitUseCase: com.narde.gestionaleosteopatabetto.domain.usecases.SaveVisitUseCase
) : BaseViewModel<AddVisitEvent, AddVisitState>() {

    // MVI Implementation
    override fun initialState(): AddVisitState = AddVisitState()
    
    override suspend fun processIntent(intent: AddVisitEvent) {
        when (intent) {
            is AddVisitEvent.UpdateVisitDate -> handleUpdateVisitDate(intent.date)
            is AddVisitEvent.UpdateGeneralNotes -> handleUpdateGeneralNotes(intent.notes)
            is AddVisitEvent.UpdateWeight -> handleUpdateWeight(intent.weight)
            is AddVisitEvent.UpdateBmi -> handleUpdateBmi(intent.bmi)
            is AddVisitEvent.UpdateBloodPressure -> handleUpdateBloodPressure(intent.pressure)
            is AddVisitEvent.UpdateCranialIndices -> handleUpdateCranialIndices(intent.indices)
            is AddVisitEvent.UpdateMainReasonDesc -> handleUpdateMainReasonDesc(intent.desc)
            is AddVisitEvent.UpdateMainReasonOnset -> handleUpdateMainReasonOnset(intent.onset)
            is AddVisitEvent.UpdateMainReasonPain -> handleUpdateMainReasonPain(intent.pain)
            is AddVisitEvent.UpdateMainReasonPainLevel -> handleUpdateMainReasonPainLevel(intent.level)
            is AddVisitEvent.UpdateMainReasonFactors -> handleUpdateMainReasonFactors(intent.factors)
            is AddVisitEvent.UpdateSecondaryReasonDesc -> handleUpdateSecondaryReasonDesc(intent.desc)
            is AddVisitEvent.UpdateSecondaryReasonDuration -> handleUpdateSecondaryReasonDuration(intent.duration)
            is AddVisitEvent.UpdateSecondaryReasonPainLevel -> handleUpdateSecondaryReasonPainLevel(intent.level)
            is AddVisitEvent.SelectPatient -> handleSelectPatient(intent.patient)
            is AddVisitEvent.UpdatePatientSearchText -> handleUpdatePatientSearchText(intent.searchText)
            is AddVisitEvent.TogglePatientDropdown -> handleTogglePatientDropdown(intent.expanded)
            is AddVisitEvent.SaveVisit -> handleSaveVisit()
            is AddVisitEvent.ClearError -> handleClearError()
            is AddVisitEvent.LoadPatients -> handleLoadPatients()
            is AddVisitEvent.ValidateForm -> handleValidateForm()
            
            // Apparatus evaluation events
            is AddVisitEvent.ToggleApparatusExpanded -> handleToggleApparatusExpanded(intent.apparatusKey, intent.expanded)
            is AddVisitEvent.UpdateCranioField -> handleUpdateCranioField(intent.field, intent.value)
            is AddVisitEvent.UpdateRespiratorioField -> handleUpdateRespiratorioField(intent.field, intent.value)
            is AddVisitEvent.UpdateCardiovascolareField -> handleUpdateCardiovascolareField(intent.field, intent.value)
            is AddVisitEvent.UpdateGastrointestinaleField -> handleUpdateGastrointestinaleField(intent.field, intent.value)
            is AddVisitEvent.UpdateUrinarioField -> handleUpdateUrinarioField(intent.field, intent.value)
            is AddVisitEvent.UpdateRiproduttivoField -> handleUpdateRiproduttivoField(intent.field, intent.value)
            is AddVisitEvent.UpdatePsicoNeuroEndocrinoField -> handleUpdatePsicoNeuroEndocrinoField(intent.field, intent.value)
            is AddVisitEvent.UpdateUnghieCuteField -> handleUpdateUnghieCuteField(intent.field, intent.value)
            is AddVisitEvent.UpdateMetabolismoField -> handleUpdateMetabolismoField(intent.field, intent.value)
            is AddVisitEvent.UpdateLinfonodiField -> handleUpdateLinfonodiField(intent.field, intent.value)
            is AddVisitEvent.UpdateMuscoloScheletricoField -> handleUpdateMuscoloScheletricoField(intent.field, intent.value)
            is AddVisitEvent.UpdateNervosoField -> handleUpdateNervosoField(intent.field, intent.value)
        }
    }
    
    // MVI Event Handlers
    private fun handleUpdateVisitDate(date: String) {
        updateState { copy(visitDate = date) }
        handleValidateForm()
    }
    
    private fun handleUpdateGeneralNotes(notes: String) {
        updateState { copy(generalNotes = notes) }
    }
    
    private fun handleUpdateWeight(weight: String) {
        updateState { copy(weight = weight) }
    }
    
    private fun handleUpdateBmi(bmi: String) {
        updateState { copy(bmi = bmi) }
    }
    
    private fun handleUpdateBloodPressure(pressure: String) {
        updateState { copy(bloodPressure = pressure) }
    }
    
    private fun handleUpdateCranialIndices(indices: String) {
        updateState { copy(cranialIndices = indices) }
    }
    
    private fun handleUpdateMainReasonDesc(desc: String) {
        updateState { copy(mainReasonDesc = desc) }
    }
    
    private fun handleUpdateMainReasonOnset(onset: String) {
        updateState { copy(mainReasonOnset = onset) }
    }
    
    private fun handleUpdateMainReasonPain(pain: String) {
        updateState { copy(mainReasonPain = pain) }
    }
    
    private fun handleUpdateMainReasonPainLevel(level: String) {
        updateState { copy(mainReasonPainLevel = level) }
    }
    
    private fun handleUpdateMainReasonFactors(factors: String) {
        updateState { copy(mainReasonFactors = factors) }
    }
    
    private fun handleUpdateSecondaryReasonDesc(desc: String) {
        updateState { copy(secondaryReasonDesc = desc) }
    }
    
    private fun handleUpdateSecondaryReasonDuration(duration: String) {
        updateState { copy(secondaryReasonDuration = duration) }
    }
    
    private fun handleUpdateSecondaryReasonPainLevel(level: String) {
        updateState { copy(secondaryReasonPainLevel = level) }
    }
    
    private fun handleSelectPatient(patient: Patient) {
        updateState { 
            copy(
                selectedPatient = patient,
                patientSearchText = patient.name,
                patientDropdownExpanded = false
            ) 
        }
        handleValidateForm()
    }
    
    private fun handleUpdatePatientSearchText(searchText: String) {
        updateState { copy(patientSearchText = searchText) }
    }
    
    private fun handleTogglePatientDropdown(expanded: Boolean) {
        updateState { copy(patientDropdownExpanded = expanded) }
    }
    
    private fun handleSaveVisit() {
        val currentState = state.value
        if (!currentState.canSave) {
            emitSideEffect(AddVisitSideEffect.ValidationError("Seleziona un paziente e inserisci la data della visita"))
            return
        }
        
        updateState { copy(isSaving = true) }
        emitSideEffect(AddVisitSideEffect.SavingStarted)
        
        viewModelScope.launch {
            try {
                println("AddVisitViewModel: Starting visit save process")
                
                // Create visit from current state
                val uiVisit = com.narde.gestionaleosteopatabetto.data.model.Visit(
                    idVisita = "VIS_" + System.currentTimeMillis(),
                    idPaziente = currentState.selectedPatient?.id ?: "",
                    dataVisita = currentState.visitDate,
                    osteopata = currentState.osteopath,
                    noteGenerali = currentState.generalNotes
                )
                
                println("AddVisitViewModel: Created UI visit - ID: ${uiVisit.idVisita}, Date: ${uiVisit.dataVisita}")
                
                // For now, save directly as basic visit data
                // TODO: Implement full domain model conversion when domain models are finalized
                val basicDomainVisit = createBasicDomainVisit(uiVisit)
                
                println("AddVisitViewModel: Created domain visit - ID: ${basicDomainVisit.idVisita}")
                
                // Save the visit using the repository
                saveVisitUseCase(basicDomainVisit).collect { result ->
                    when {
                        result.isSuccess -> {
                            val visitId = result.getOrNull()?.idVisita ?: uiVisit.idVisita
                            println("AddVisitViewModel: Visit saved successfully - ID: $visitId")
                            emitSideEffect(AddVisitSideEffect.VisitSaved(visitId))
                            updateState { copy(isSaving = false) }
                            emitSideEffect(AddVisitSideEffect.SavingCompleted)
                        }
                        result.isFailure -> {
                            val errorMessage = result.exceptionOrNull()?.message ?: "Errore nel salvare la visita"
                            println("AddVisitViewModel: Save failed - $errorMessage")
                            updateState { copy(isSaving = false, errorMessage = errorMessage) }
                            emitSideEffect(AddVisitSideEffect.ValidationError(errorMessage))
                        }
                    }
                }
                
            } catch (e: Exception) {
                println("AddVisitViewModel: Exception in save process - ${e.message}")
                e.printStackTrace()
                updateState { copy(isSaving = false, errorMessage = e.message ?: "Errore sconosciuto") }
                emitSideEffect(AddVisitSideEffect.ValidationError(e.message ?: "Errore sconosciuto"))
            }
        }
    }
    
    private fun handleClearError() {
        updateState { copy(errorMessage = "") }
    }
    
    private fun handleLoadPatients() {
        updateState { copy(isLoadingPatients = true) }
        
        viewModelScope.launch {
            try {
                // TODO: Implement getPatientsUseCase
                // For now, we'll use empty list
                updateState { 
                    copy(
                        isLoadingPatients = false,
                        filteredPatients = emptyList()
                    ) 
                }
                emitSideEffect(AddVisitSideEffect.PatientsLoaded(0))
            } catch (e: Exception) {
                updateState { 
                    copy(
                        isLoadingPatients = false,
                        errorMessage = e.message ?: "Errore nel caricare i pazienti"
                    ) 
                }
                emitSideEffect(AddVisitSideEffect.PatientsLoadFailed)
            }
        }
    }
    
    private fun handleValidateForm() {
        val currentState = state.value
        val isValid = currentState.selectedPatient != null && 
                     currentState.visitDate.isNotBlank()
        
        updateState { copy(isFormValid = isValid) }
    }
    
    /**
     * Load patients into ViewModel state
     */
    fun loadPatients(patients: List<com.narde.gestionaleosteopatabetto.data.model.Patient>) {
        sendIntent(AddVisitEvent.LoadPatients)
        updateState { copy(filteredPatients = patients) }
    }
    
    // Apparatus event handlers
    private fun handleToggleApparatusExpanded(apparatusKey: String, expanded: Boolean) {
        updateState { 
            copy(
                apparatusExpandedStates = apparatusExpandedStates.toMutableMap().apply {
                    put(apparatusKey, expanded)
                }.toMap()
            )
        }
    }
    
    private fun handleUpdateCranioField(field: String, value: Any) {
        updateState { 
            copy(
                apparatoCranio = when (field) {
                    "problemiOlfatto" -> apparatoCranio.copy(problemiOlfatto = value as Boolean)
                    "problemiVista" -> apparatoCranio.copy(problemiVista = value as Boolean)
                    "problemiUdito" -> apparatoCranio.copy(problemiUdito = value as Boolean)
                    "disturbiOcclusali" -> apparatoCranio.copy(disturbiOcclusali = value as Boolean)
                    "malattieParodontali" -> apparatoCranio.copy(malattieParodontali = value as Boolean)
                    "linguaDolente" -> apparatoCranio.copy(linguaDolente = value as Boolean)
                    "cefaleaPresente" -> apparatoCranio.copy(cefaleaPresente = value as Boolean)
                    "cefaleaIntensitaVas" -> apparatoCranio.copy(cefaleaIntensitaVas = value as String)
                    "cefaleaFrequenza" -> apparatoCranio.copy(cefaleaFrequenza = value as String)
                    "cefaleaDurataOre" -> apparatoCranio.copy(cefaleaDurataOre = value as String)
                    "cefaleaTipo" -> apparatoCranio.copy(cefaleaTipo = value as String)
                    "cefaleaLocalizzazione" -> apparatoCranio.copy(cefaleaLocalizzazione = value as List<String>)
                    "cefaleaFattoriScatenanti" -> apparatoCranio.copy(cefaleaFattoriScatenanti = value as List<String>)
                    "cefaleaFattoriAllevianti" -> apparatoCranio.copy(cefaleaFattoriAllevianti = value as List<String>)
                    "emicraniaPresente" -> apparatoCranio.copy(emicraniaPresente = value as Boolean)
                    "emicraniaConAura" -> apparatoCranio.copy(emicraniaConAura = value as Boolean)
                    "emicraniaFrequenza" -> apparatoCranio.copy(emicraniaFrequenza = value as String)
                    "atmProblemiPresenti" -> apparatoCranio.copy(atmProblemiPresenti = value as Boolean)
                    "atmClickArticolare" -> apparatoCranio.copy(atmClickArticolare = value as Boolean)
                    "atmDoloreMasticazione" -> apparatoCranio.copy(atmDoloreMasticazione = value as Boolean)
                    "atmLimitazioneApertura" -> apparatoCranio.copy(atmLimitazioneApertura = value as Boolean)
                    "atmSerramentoDiurno" -> apparatoCranio.copy(atmSerramentoDiurno = value as Boolean)
                    "atmBruxismoNotturno" -> apparatoCranio.copy(atmBruxismoNotturno = value as Boolean)
                    "atmDeviazioneMandibolare" -> apparatoCranio.copy(atmDeviazioneMandibolare = value as Boolean)
                    "apparecchioOrtodonticoPortato" -> apparatoCranio.copy(apparecchioOrtodonticoPortato = value as Boolean)
                    "apparecchioOrtodonticoPeriodo" -> apparatoCranio.copy(apparecchioOrtodonticoPeriodo = value as String)
                    "apparecchioOrtodonticoEtaInizio" -> apparatoCranio.copy(apparecchioOrtodonticoEtaInizio = value as String)
                    "apparecchioOrtodonticoEtaFine" -> apparatoCranio.copy(apparecchioOrtodonticoEtaFine = value as String)
                    "apparecchioOrtodonticoTipo" -> apparatoCranio.copy(apparecchioOrtodonticoTipo = value as String)
                    else -> apparatoCranio
                }
            )
        }
    }
    
    private fun handleUpdateRespiratorioField(field: String, value: Any) {
        updateState { 
            copy(
                apparatoRespiratorio = when (field) {
                    "dispneaPresente" -> apparatoRespiratorio.copy(dispneaPresente = value as Boolean)
                    "dispneaSottoSforzo" -> apparatoRespiratorio.copy(dispneaSottoSforzo = value as Boolean)
                    "dispneaARiposo" -> apparatoRespiratorio.copy(dispneaARiposo = value as Boolean)
                    "dispneaNotturna" -> apparatoRespiratorio.copy(dispneaNotturna = value as Boolean)
                    "oppressioneToracica" -> apparatoRespiratorio.copy(oppressioneToracica = value as Boolean)
                    "tossePresente" -> apparatoRespiratorio.copy(tossePresente = value as Boolean)
                    "tosseTipo" -> apparatoRespiratorio.copy(tosseTipo = value as String)
                    "tosseNotturna" -> apparatoRespiratorio.copy(tosseNotturna = value as Boolean)
                    "tosseCronica" -> apparatoRespiratorio.copy(tosseCronica = value as Boolean)
                    "tosseConSangue" -> apparatoRespiratorio.copy(tosseConSangue = value as Boolean)
                    "allergieRespiratoriePresente" -> apparatoRespiratorio.copy(allergieRespiratoriePresente = value as Boolean)
                    "allergieRespiratorieAllergeni" -> apparatoRespiratorio.copy(allergieRespiratorieAllergeni = value as List<String>)
                    "allergieRespiratorieStagionalita" -> apparatoRespiratorio.copy(allergieRespiratorieStagionalita = value as String)
                    "raucedine" -> apparatoRespiratorio.copy(raucedine = value as Boolean)
                    "congestioneNasalePresente" -> apparatoRespiratorio.copy(congestioneNasalePresente = value as Boolean)
                    "congestioneNasaleCronica" -> apparatoRespiratorio.copy(congestioneNasaleCronica = value as Boolean)
                    "sinusitePresente" -> apparatoRespiratorio.copy(sinusitePresente = value as Boolean)
                    "sinusiteLocalizzazione" -> apparatoRespiratorio.copy(sinusiteLocalizzazione = value as String)
                    "russarePresente" -> apparatoRespiratorio.copy(russarePresente = value as Boolean)
                    "russareIntensita" -> apparatoRespiratorio.copy(russareIntensita = value as String)
                    "russareApneeNotturne" -> apparatoRespiratorio.copy(russareApneeNotturne = value as Boolean)
                    "brucioreGola" -> apparatoRespiratorio.copy(brucioreGola = value as Boolean)
                    "raffreddoriFrequentiPresente" -> apparatoRespiratorio.copy(raffreddoriFrequentiPresente = value as Boolean)
                    "raffreddoriFrequentiFrequenzaAnno" -> apparatoRespiratorio.copy(raffreddoriFrequentiFrequenzaAnno = value as String)
                    else -> apparatoRespiratorio
                }
            )
        }
    }
    
    private fun handleUpdateCardiovascolareField(field: String, value: Any) {
        // TODO: Implement field updates for cardiovascular apparatus
        // For now, state remains unchanged
        updateState { copy() }
    }
    
    private fun handleUpdateGastrointestinaleField(field: String, value: Any) {
        // TODO: Implement field updates for gastrointestinal apparatus
        // For now, state remains unchanged
        updateState { copy() }
    }
    
    private fun handleUpdateUrinarioField(field: String, value: Any) {
        // TODO: Implement field updates for urinary apparatus
        // For now, state remains unchanged
        updateState { copy() }
    }
    
    private fun handleUpdateRiproduttivoField(field: String, value: Any) {
        // TODO: Implement field updates for reproductive apparatus
        // For now, state remains unchanged
        updateState { copy() }
    }
    
    private fun handleUpdatePsicoNeuroEndocrinoField(field: String, value: Any) {
        // TODO: Implement field updates for psycho-neuro-endocrine apparatus
        // For now, state remains unchanged
        updateState { copy() }
    }
    
    private fun handleUpdateUnghieCuteField(field: String, value: Any) {
        // TODO: Implement field updates for nails and skin apparatus
        // For now, state remains unchanged
        updateState { copy() }
    }
    
    private fun handleUpdateMetabolismoField(field: String, value: Any) {
        // TODO: Implement field updates for metabolism apparatus
        // For now, state remains unchanged
        updateState { copy() }
    }
    
    private fun handleUpdateLinfonodiField(field: String, value: Any) {
        updateState { 
            copy(
                apparatoLinfonodi = when (field) {
                    "anomaliePresente" -> apparatoLinfonodi.copy(anomaliePresente = value as Boolean)
                    "anomalieLocalizzazione" -> apparatoLinfonodi.copy(anomalieLocalizzazione = value as String)
                    "anomalieDimensione" -> apparatoLinfonodi.copy(anomalieDimensione = value as String)
                    "anomalieMobilita" -> apparatoLinfonodi.copy(anomalieMobilita = value as Boolean)
                    "anomalieDolenti" -> apparatoLinfonodi.copy(anomalieDolenti = value as Boolean)
                    "anomalieConsistenza" -> apparatoLinfonodi.copy(anomalieConsistenza = value as String)
                    "note" -> apparatoLinfonodi.copy(note = value as String)
                    else -> apparatoLinfonodi
                }
            )
        }
    }
    
    private fun handleUpdateMuscoloScheletricoField(field: String, value: Any) {
        // TODO: Implement field updates for musculoskeletal apparatus
        // For now, state remains unchanged
        updateState { copy() }
    }
    
    private fun handleUpdateNervosoField(field: String, value: Any) {
        // TODO: Implement field updates for nervous apparatus
        // For now, state remains unchanged
        updateState { copy() }
    }
    
    /**
     * Create basic domain visit model with only required fields
     * Required: patient ID, visit ID, visit date, osteopath, notes
     */
    private fun createBasicDomainVisit(uiVisit: com.narde.gestionaleosteopatabetto.data.model.Visit): com.narde.gestionaleosteopatabetto.domain.models.Visit {
        println("AddVisitViewModel: Parsing date: ${uiVisit.dataVisita}")
        val parsedDate = com.narde.gestionaleosteopatabetto.utils.DateUtils.parseItalianDate(uiVisit.dataVisita)
        if (parsedDate == null) {
            println("AddVisitViewModel: Date parsing failed for: ${uiVisit.dataVisita}")
            throw IllegalArgumentException("Invalid visit date format: ${uiVisit.dataVisita}")
        }
        println("AddVisitViewModel: Date parsed successfully: $parsedDate")
        
        return com.narde.gestionaleosteopatabetto.domain.models.Visit(
            idVisita = uiVisit.idVisita,
            idPaziente = uiVisit.idPaziente,
            dataVisita = parsedDate,
            osteopata = uiVisit.osteopata,
            noteGenerali = uiVisit.noteGenerali,
            // Set optional fields to null to avoid validation issues
            datiVisitaCorrente = null,
            motivoConsulto = null
        )
    }
    
}
