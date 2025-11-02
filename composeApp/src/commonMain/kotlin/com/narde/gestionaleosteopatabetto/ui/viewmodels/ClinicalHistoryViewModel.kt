package com.narde.gestionaleosteopatabetto.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.RealmConfig
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.data.database.models.Patient as DatabasePatient
import com.narde.gestionaleosteopatabetto.data.database.models.*
import com.narde.gestionaleosteopatabetto.data.database.utils.createDatabaseUtils
import io.realm.kotlin.ext.query
import kotlin.random.Random

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
            pediatricGeneralNotes = storiaClinica?.anamnesiPediatrica?.noteGenerali ?: "",
            
            // Interventions & Traumas - Convert RealmList to List
            interventions = storiaClinica?.interventiTrauma?.map { intervento ->
                InterventionUiState(
                    id = intervento.id,
                    date = intervento.data,
                    type = intervento.tipo,
                    description = intervento.descrizione,
                    treatment = intervento.trattamento,
                    outcome = intervento.esito,
                    isExpanded = false
                )
            } ?: emptyList(),
            
            // Diagnostic Tests - Convert RealmList to List
            diagnosticTests = storiaClinica?.esamiStrumentali?.map { esame ->
                DiagnosticTestUiState(
                    id = esame.id,
                    date = esame.data,
                    type = esame.tipo,
                    bodyArea = esame.distretto,
                    results = esame.risultato,
                    facility = esame.struttura,
                    isExpanded = false
                )
            } ?: emptyList(),
            
            // Pharmacological Therapies - Convert RealmList to List
            pharmacologicalTherapies = storiaClinica?.terapieFarmacologiche?.map { terapia ->
                PharmacologicalTherapyUiState(
                    medication = terapia.farmaco,
                    dosage = terapia.dosaggio,
                    frequency = terapia.frequenza,
                    startDate = terapia.dataInizio,
                    endDate = terapia.dataFine ?: "",
                    indication = terapia.indicazione,
                    isOngoing = terapia.dataFine == null,
                    isExpanded = false
                )
            } ?: emptyList()
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
     * Add a new intervention/trauma
     */
    fun addIntervention() {
        val currentState = _uiState.value
        val newIntervention = InterventionUiState(
            id = "", // Empty ID for new interventions
            isExpanded = true // Auto-expand new items for editing
        )
        _uiState.value = currentState.copy(
            interventions = currentState.interventions + newIntervention
        )
    }
    
    /**
     * Update a specific field of an intervention
     */
    fun updateInterventionField(index: Int, field: String, value: String) {
        val currentState = _uiState.value
        if (index in currentState.interventions.indices) {
            val intervention = currentState.interventions[index]
            val updatedIntervention = when (field) {
                "date" -> intervention.copy(date = value)
                "type" -> intervention.copy(type = value)
                "description" -> intervention.copy(description = value)
                "treatment" -> intervention.copy(treatment = value)
                "outcome" -> intervention.copy(outcome = value)
                else -> intervention
            }
            _uiState.value = currentState.copy(
                interventions = currentState.interventions.toMutableList().apply {
                    set(index, updatedIntervention)
                }
            )
        }
    }
    
    /**
     * Delete an intervention
     */
    fun deleteIntervention(index: Int) {
        val currentState = _uiState.value
        if (index in currentState.interventions.indices) {
            _uiState.value = currentState.copy(
                interventions = currentState.interventions.toMutableList().apply {
                    removeAt(index)
                }
            )
        }
    }
    
    /**
     * Toggle expand/collapse state of an intervention
     */
    fun toggleInterventionExpanded(index: Int) {
        val currentState = _uiState.value
        if (index in currentState.interventions.indices) {
            val intervention = currentState.interventions[index]
            val updatedIntervention = intervention.copy(isExpanded = !intervention.isExpanded)
            _uiState.value = currentState.copy(
                interventions = currentState.interventions.toMutableList().apply {
                    set(index, updatedIntervention)
                }
            )
        }
    }
    
    /**
     * Add a new diagnostic test
     */
    fun addDiagnosticTest() {
        val currentState = _uiState.value
        val newTest = DiagnosticTestUiState(
            id = "", // Empty ID for new tests
            isExpanded = true // Auto-expand new items for editing
        )
        _uiState.value = currentState.copy(
            diagnosticTests = currentState.diagnosticTests + newTest
        )
    }
    
    /**
     * Update a specific field of a diagnostic test
     */
    fun updateDiagnosticTestField(index: Int, field: String, value: String) {
        val currentState = _uiState.value
        if (index in currentState.diagnosticTests.indices) {
            val test = currentState.diagnosticTests[index]
            val updatedTest = when (field) {
                "date" -> test.copy(date = value)
                "type" -> test.copy(type = value)
                "bodyArea" -> test.copy(bodyArea = value)
                "results" -> test.copy(results = value)
                "facility" -> test.copy(facility = value)
                else -> test
            }
            _uiState.value = currentState.copy(
                diagnosticTests = currentState.diagnosticTests.toMutableList().apply {
                    set(index, updatedTest)
                }
            )
        }
    }
    
    /**
     * Delete a diagnostic test
     */
    fun deleteDiagnosticTest(index: Int) {
        val currentState = _uiState.value
        if (index in currentState.diagnosticTests.indices) {
            _uiState.value = currentState.copy(
                diagnosticTests = currentState.diagnosticTests.toMutableList().apply {
                    removeAt(index)
                }
            )
        }
    }
    
    /**
     * Toggle expand/collapse state of a diagnostic test
     */
    fun toggleDiagnosticTestExpanded(index: Int) {
        val currentState = _uiState.value
        if (index in currentState.diagnosticTests.indices) {
            val test = currentState.diagnosticTests[index]
            val updatedTest = test.copy(isExpanded = !test.isExpanded)
            _uiState.value = currentState.copy(
                diagnosticTests = currentState.diagnosticTests.toMutableList().apply {
                    set(index, updatedTest)
                }
            )
        }
    }
    
    /**
     * Add a new pharmacological therapy
     */
    fun addPharmacologicalTherapy() {
        val currentState = _uiState.value
        val newTherapy = PharmacologicalTherapyUiState(
            isExpanded = true // Auto-expand new items for editing
        )
        _uiState.value = currentState.copy(
            pharmacologicalTherapies = currentState.pharmacologicalTherapies + newTherapy
        )
    }
    
    /**
     * Update a specific field of a pharmacological therapy
     */
    fun updatePharmacologicalTherapyField(index: Int, field: String, value: String) {
        val currentState = _uiState.value
        if (index in currentState.pharmacologicalTherapies.indices) {
            val therapy = currentState.pharmacologicalTherapies[index]
            val updatedTherapy = when (field) {
                "medication" -> therapy.copy(medication = value)
                "dosage" -> therapy.copy(dosage = value)
                "frequency" -> therapy.copy(frequency = value)
                "startDate" -> therapy.copy(startDate = value)
                "endDate" -> therapy.copy(endDate = value)
                "indication" -> therapy.copy(indication = value)
                else -> therapy
            }
            _uiState.value = currentState.copy(
                pharmacologicalTherapies = currentState.pharmacologicalTherapies.toMutableList().apply {
                    set(index, updatedTherapy)
                }
            )
        }
    }
    
    /**
     * Delete a pharmacological therapy
     */
    fun deletePharmacologicalTherapy(index: Int) {
        val currentState = _uiState.value
        if (index in currentState.pharmacologicalTherapies.indices) {
            _uiState.value = currentState.copy(
                pharmacologicalTherapies = currentState.pharmacologicalTherapies.toMutableList().apply {
                    removeAt(index)
                }
            )
        }
    }
    
    /**
     * Toggle expand/collapse state of a pharmacological therapy
     */
    fun togglePharmacologicalTherapyExpanded(index: Int) {
        val currentState = _uiState.value
        if (index in currentState.pharmacologicalTherapies.indices) {
            val therapy = currentState.pharmacologicalTherapies[index]
            val updatedTherapy = therapy.copy(isExpanded = !therapy.isExpanded)
            _uiState.value = currentState.copy(
                pharmacologicalTherapies = currentState.pharmacologicalTherapies.toMutableList().apply {
                    set(index, updatedTherapy)
                }
            )
        }
    }
    
    /**
     * Toggle ongoing status of a pharmacological therapy
     */
    fun togglePharmacologicalTherapyOngoing(index: Int) {
        val currentState = _uiState.value
        if (index in currentState.pharmacologicalTherapies.indices) {
            val therapy = currentState.pharmacologicalTherapies[index]
            val updatedTherapy = therapy.copy(
                isOngoing = !therapy.isOngoing,
                endDate = if (!therapy.isOngoing) "" else therapy.endDate // Clear endDate when setting to ongoing
            )
            _uiState.value = currentState.copy(
                pharmacologicalTherapies = currentState.pharmacologicalTherapies.toMutableList().apply {
                    set(index, updatedTherapy)
                }
            )
        }
    }
    
    /**
     * Validate and update clinical history
     * Returns Result<Unit> for use by coordinator
     */
    suspend fun updateClinicalHistory(): Result<Unit> {
        val state = _uiState.value
        
        return try {
            val success = updateClinicalHistoryInDatabase(state)
            if (success) {
                println("ClinicalHistoryViewModel: Clinical history updated successfully")
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update clinical history"))
            }
        } catch (e: Exception) {
            println("ClinicalHistoryViewModel: Exception during update - ${e.message}")
            Result.failure(e)
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
                    // Perform the entire operation within a single write transaction
                    RealmConfig.realm.write {
                        val patient = query<DatabasePatient>("idPaziente == $0", state.patientId).find().firstOrNull()
                        if (patient != null) {
                            // Initialize StoriaClinica if it doesn't exist
                            if (patient.storiaClinica == null) {
                                patient.storiaClinica = StoriaClinica()
                            }
                            // Update clinical history within the same transaction
                            updateExistingClinicalHistoryInTransaction(patient.storiaClinica!!, state)
                            true
                        } else {
                            false
                        }
                    }
                } ?: false
            } else {
                false
            }
        } catch (e: Exception) {
            println("Error updating clinical history: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}

/**
 * Update existing clinical history with new values while preserving existing data
 * This function must be called within a Realm write transaction
 */
private fun updateExistingClinicalHistoryInTransaction(existingStoriaClinica: StoriaClinica, state: ClinicalHistoryUiState) {
    // Initialize patologieCroniche if null
    if (existingStoriaClinica.patologieCroniche == null) {
        existingStoriaClinica.patologieCroniche = PatologieCroniche()
    }
    
    // Update chronic conditions
    existingStoriaClinica.patologieCroniche?.let { patologie ->
        // Initialize nested allergieFarmaci if null
        if (patologie.allergieFarmaci == null) {
            patologie.allergieFarmaci = AllergieFarmaci()
        }
        patologie.allergieFarmaci?.let { allergie ->
            allergie.presente = state.hasDrugAllergies
            allergie.listaAllergie.clear()
            if (state.drugAllergiesList.isNotEmpty()) {
                allergie.listaAllergie.addAll(state.drugAllergiesList.split(",").map { it.trim() })
            }
        }
        
        // Initialize nested diabete if null
        if (patologie.diabete == null) {
            patologie.diabete = Diabete()
        }
        patologie.diabete?.let { diabete ->
            diabete.presente = state.hasDiabetes
            diabete.tipologia = state.diabetesType
        }
        
        patologie.ipertiroidismo = state.hasHyperthyroidism
        patologie.cardiopatia = state.hasHeartDisease
        patologie.ipertensioneArteriosa = state.hasHypertension
    }
    
    // Initialize stileVita if null
    if (existingStoriaClinica.stileVita == null) {
        existingStoriaClinica.stileVita = StileVita()
    }
    
    // Update lifestyle factors
    existingStoriaClinica.stileVita?.let { stileVita ->
        // Initialize nested tabagismo if null
        if (stileVita.tabagismo == null) {
            stileVita.tabagismo = Tabagismo()
        }
        stileVita.tabagismo?.let { tabagismo ->
            tabagismo.stato = state.smokingStatus
            tabagismo.sigaretteGiorno = state.cigarettesPerDay.toIntOrNull() ?: 0
            tabagismo.anniFumo = state.yearsSmoking.toIntOrNull() ?: 0
        }
        
        stileVita.lavoro = state.workType
        stileVita.professione = state.profession
        stileVita.oreLavoroGiorno = state.workHoursPerDay.toIntOrNull() ?: 0
        
        // Initialize nested attivitaSportiva if null
        if (stileVita.attivitaSportiva == null) {
            stileVita.attivitaSportiva = AttivitaSportiva()
        }
        stileVita.attivitaSportiva?.let { attivita ->
            attivita.presente = state.hasPhysicalActivity
            attivita.sport.clear()
            if (state.sportsList.isNotEmpty()) {
                attivita.sport.addAll(state.sportsList.split(",").map { it.trim() })
            }
            attivita.frequenza = state.activityFrequency
            attivita.intensita = state.activityIntensity
        }
    }
    
    // Initialize anamnesiPediatrica if null
    if (existingStoriaClinica.anamnesiPediatrica == null) {
        existingStoriaClinica.anamnesiPediatrica = AnamnesiPediatrica()
    }
    
    // Update pediatric history
    existingStoriaClinica.anamnesiPediatrica?.let { anamnesi ->
        // Initialize nested gravidanza if null
        if (anamnesi.gravidanza == null) {
            anamnesi.gravidanza = Gravidanza()
        }
        anamnesi.gravidanza?.let { gravidanza ->
            gravidanza.complicazioni = state.pregnancyComplications
            gravidanza.note = state.pregnancyNotes
        }
        
        // Initialize nested parto if null
        if (anamnesi.parto == null) {
            anamnesi.parto = Parto()
        }
        anamnesi.parto?.let { parto ->
            parto.tipo = state.birthType
            parto.complicazioni = state.birthComplications
            parto.pesoNascitaGrammi = state.birthWeight.toIntOrNull() ?: 0
            parto.punteggioApgar5min = state.apgarScore.toIntOrNull() ?: 0
            parto.note = state.birthNotes
        }
        
        // Initialize nested sviluppo if null
        if (anamnesi.sviluppo == null) {
            anamnesi.sviluppo = Sviluppo()
        }
        anamnesi.sviluppo?.let { sviluppo ->
            sviluppo.primiPassiMesi = state.firstStepsMonths.toIntOrNull() ?: 0
            sviluppo.primeParoleMesi = state.firstWordsMonths.toIntOrNull() ?: 0
            sviluppo.problemiSviluppo = state.developmentProblems
            sviluppo.note = state.developmentNotes
        }
        
        anamnesi.noteGenerali = state.pediatricGeneralNotes
    }
    
    // Update Interventions & Traumas
    existingStoriaClinica.interventiTrauma.clear()
    state.interventions.forEach { interventionUi ->
        val intervention = InterventoTrauma().apply {
            // Generate UUID for new interventions (those with empty id)
            id = if (interventionUi.id.isNotEmpty()) {
                interventionUi.id
            } else {
                "INT${System.currentTimeMillis()}${Random.nextInt(1000)}"
            }
            data = interventionUi.date
            tipo = interventionUi.type
            descrizione = interventionUi.description
            trattamento = interventionUi.treatment
            esito = interventionUi.outcome
        }
        existingStoriaClinica.interventiTrauma.add(intervention)
    }
    
    // Update Diagnostic Tests
    existingStoriaClinica.esamiStrumentali.clear()
    state.diagnosticTests.forEach { testUi ->
        val test = EsameStrumentale().apply {
            // Generate UUID for new tests (those with empty id)
            id = if (testUi.id.isNotEmpty()) {
                testUi.id
            } else {
                "TEST${System.currentTimeMillis()}${Random.nextInt(1000)}"
            }
            data = testUi.date
            tipo = testUi.type
            distretto = testUi.bodyArea
            risultato = testUi.results
            struttura = testUi.facility
        }
        existingStoriaClinica.esamiStrumentali.add(test)
    }
    
    // Update Pharmacological Therapies
    existingStoriaClinica.terapieFarmacologiche.clear()
    state.pharmacologicalTherapies.forEach { therapyUi ->
        val therapy = TerapiaFarmacologica().apply {
            farmaco = therapyUi.medication
            dosaggio = therapyUi.dosage
            frequenza = therapyUi.frequency
            dataInizio = therapyUi.startDate
            // Set dataFine to null if ongoing, otherwise use endDate value
            dataFine = if (therapyUi.isOngoing) {
                null
            } else {
                if (therapyUi.endDate.isNotEmpty()) therapyUi.endDate else null
            }
            indicazione = therapyUi.indication
        }
        existingStoriaClinica.terapieFarmacologiche.add(therapy)
    }
}

