package com.narde.gestionaleosteopatabetto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.model.Visit
import com.narde.gestionaleosteopatabetto.ui.components.ItalianDateInput
import com.narde.gestionaleosteopatabetto.domain.usecases.SaveVisitUseCaseImpl
import com.narde.gestionaleosteopatabetto.domain.models.Visit as DomainVisit
import com.narde.gestionaleosteopatabetto.utils.DateUtils
import com.narde.gestionaleosteopatabetto.ui.components.apparati.ExpandableApparatusCard
import com.narde.gestionaleosteopatabetto.ui.components.apparati.ApparatusMetadata
import com.narde.gestionaleosteopatabetto.ui.components.apparati.forms.*
import com.narde.gestionaleosteopatabetto.ui.viewmodels.AddVisitViewModel
import com.narde.gestionaleosteopatabetto.ui.mvi.AddVisitEvent
import com.narde.gestionaleosteopatabetto.ui.factories.ViewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.launch

/**
 * Screen for adding new visits to the osteopath management system
 * Contains comprehensive form for visit data entry
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVisitScreen(
    patients: List<Patient>,
    onBackClick: () -> Unit,
    onVisitSaved: (Visit) -> Unit,
    modifier: Modifier = Modifier
) {
    // Coroutine scope for async operations
    val coroutineScope = rememberCoroutineScope()
    
    // ViewModel for state management - handles all form data including consultation reasons and apparatus
    val viewModel = remember { ViewModelFactory.createAddVisitViewModel() }
    val state by viewModel.state.collectAsState()
    val sideEffects = viewModel.sideEffects
    
    // Load patients into ViewModel when patients list is available
    LaunchedEffect(patients) {
        viewModel.loadPatients(patients)
    }
    
    // Handle side effects from ViewModel
    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is com.narde.gestionaleosteopatabetto.ui.mvi.AddVisitSideEffect.VisitSaved -> {
                    println("AddVisitScreen: Visit saved via ViewModel - ID: ${sideEffect.visitId}")
                    // Reload visit from database to get complete data with all fields
                    coroutineScope.launch {
                        try {
                            // Use repository directly to get database model and convert using DatabaseUtils
                            val repository = com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer.getVisitRepository()
                            if (repository != null) {
                                val databaseVisit = repository.getVisitById(sideEffect.visitId)
                                if (databaseVisit != null) {
                                    // Convert database model to UI model using DatabaseUtils
                                    val databaseUtils = com.narde.gestionaleosteopatabetto.data.database.utils.DatabaseUtils()
                                    val uiVisit = databaseUtils.toUIVisit(databaseVisit)
                                    println("AddVisitScreen: Reloaded visit - Has motivoConsulto: ${uiVisit.motivoConsulto != null}")
                                    println("AddVisitScreen: Reloaded visit - Has valutazioneApparati: ${uiVisit.valutazioneApparati != null}")
                                    onVisitSaved(uiVisit)
                                } else {
                                    println("AddVisitScreen: Visit not found in database after save")
                                    // Fallback: create basic UI visit from state
                                    val uiVisit = Visit(
                                        idVisita = sideEffect.visitId,
                                        idPaziente = state.selectedPatient?.id ?: "",
                                        dataVisita = state.visitDate,
                                        osteopata = state.osteopath,
                                        noteGenerali = state.generalNotes
                                    )
                                    onVisitSaved(uiVisit)
                                }
                            } else {
                                println("AddVisitScreen: Repository not available")
                                // Fallback: create basic UI visit from state
                                val uiVisit = Visit(
                                    idVisita = sideEffect.visitId,
                                    idPaziente = state.selectedPatient?.id ?: "",
                                    dataVisita = state.visitDate,
                                    osteopata = state.osteopath,
                                    noteGenerali = state.generalNotes
                                )
                                onVisitSaved(uiVisit)
                            }
                        } catch (e: Exception) {
                            println("AddVisitScreen: Error loading saved visit - ${e.message}")
                            e.printStackTrace()
                            // Fallback: create basic UI visit from state
                            val uiVisit = Visit(
                                idVisita = sideEffect.visitId,
                                idPaziente = state.selectedPatient?.id ?: "",
                                dataVisita = state.visitDate,
                                osteopata = state.osteopath,
                                noteGenerali = state.generalNotes
                            )
                            onVisitSaved(uiVisit)
                        }
                    }
                }
                is com.narde.gestionaleosteopatabetto.ui.mvi.AddVisitSideEffect.ValidationError -> {
                    println("AddVisitScreen: Validation error - ${sideEffect.message}")
                }
                else -> {}
            }
        }
    }
    
    // Local state for UI (will sync to ViewModel)
    var selectedPatient by remember { mutableStateOf<Patient?>(null) }
    var visitDate by remember { mutableStateOf("") }
    val osteopath = "Roberto Caeran" // Fixed osteopath name - not editable
    var generalNotes by remember { mutableStateOf("") }
    
    // Patient dropdown state
    var patientDropdownExpanded by remember { mutableStateOf(false) }
    var patientSearchText by remember { mutableStateOf("") }
    
    // Current visit data state
    var weight by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }
    var bloodPressure by remember { mutableStateOf("") }
    var cranialIndices by remember { mutableStateOf("") }
    
    // Consultation reason state
    var mainReasonDesc by remember { mutableStateOf("") }
    var mainReasonOnset by remember { mutableStateOf("") }
    var mainReasonPain by remember { mutableStateOf("") }
    var mainReasonPainLevel by remember { mutableStateOf("") }
    var mainReasonFactors by remember { mutableStateOf("") }
    
    var secondaryReasonDesc by remember { mutableStateOf("") }
    var secondaryReasonDuration by remember { mutableStateOf("") }
    var secondaryReasonPainLevel by remember { mutableStateOf("") }
    
    // Note: Local state is synced to ViewModel only when save button is clicked
    // This avoids infinite loops and unnecessary state updates

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Nuova Visita",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Indietro"
                    )
                }
            },
            actions = {
                Button(
                    onClick = {
                        // Sync all local state to ViewModel before saving
                        // This ensures ViewModel has the latest values from the form
                        println("AddVisitScreen: Syncing state to ViewModel before save")
                        
                        // Sync basic fields
                        selectedPatient?.let { viewModel.sendIntent(AddVisitEvent.SelectPatient(it)) }
                        if (visitDate != state.visitDate) {
                            viewModel.sendIntent(AddVisitEvent.UpdateVisitDate(visitDate))
                        }
                        if (generalNotes != state.generalNotes) {
                            viewModel.sendIntent(AddVisitEvent.UpdateGeneralNotes(generalNotes))
                        }
                        
                        // Sync current visit data
                        if (weight != state.weight) {
                            viewModel.sendIntent(AddVisitEvent.UpdateWeight(weight))
                        }
                        if (bmi != state.bmi) {
                            viewModel.sendIntent(AddVisitEvent.UpdateBmi(bmi))
                        }
                        if (bloodPressure != state.bloodPressure) {
                            viewModel.sendIntent(AddVisitEvent.UpdateBloodPressure(bloodPressure))
                        }
                        if (cranialIndices != state.cranialIndices) {
                            viewModel.sendIntent(AddVisitEvent.UpdateCranialIndices(cranialIndices))
                        }
                        
                        // Sync consultation reasons
                        if (mainReasonDesc != state.mainReasonDesc) {
                            viewModel.sendIntent(AddVisitEvent.UpdateMainReasonDesc(mainReasonDesc))
                        }
                        if (mainReasonOnset != state.mainReasonOnset) {
                            viewModel.sendIntent(AddVisitEvent.UpdateMainReasonOnset(mainReasonOnset))
                        }
                        if (mainReasonPain != state.mainReasonPain) {
                            viewModel.sendIntent(AddVisitEvent.UpdateMainReasonPain(mainReasonPain))
                        }
                        if (mainReasonPainLevel != state.mainReasonPainLevel) {
                            viewModel.sendIntent(AddVisitEvent.UpdateMainReasonPainLevel(mainReasonPainLevel))
                        }
                        if (mainReasonFactors != state.mainReasonFactors) {
                            viewModel.sendIntent(AddVisitEvent.UpdateMainReasonFactors(mainReasonFactors))
                        }
                        if (secondaryReasonDesc != state.secondaryReasonDesc) {
                            viewModel.sendIntent(AddVisitEvent.UpdateSecondaryReasonDesc(secondaryReasonDesc))
                        }
                        if (secondaryReasonDuration != state.secondaryReasonDuration) {
                            viewModel.sendIntent(AddVisitEvent.UpdateSecondaryReasonDuration(secondaryReasonDuration))
                        }
                        if (secondaryReasonPainLevel != state.secondaryReasonPainLevel) {
                            viewModel.sendIntent(AddVisitEvent.UpdateSecondaryReasonPainLevel(secondaryReasonPainLevel))
                        }
                        
                        // Use ViewModel's save logic which handles all data including consultation reasons and apparatus
                        println("AddVisitScreen: Triggering ViewModel save")
                        viewModel.sendIntent(AddVisitEvent.SaveVisit)
                    },
                    enabled = selectedPatient != null && visitDate.isNotBlank() && !state.isSaving
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        when {
                            state.isSaving -> "Salvando..."
                            else -> "Salva"
                        }
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        
        // Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Patient Selection Card
            PatientSelectionCard(
                patients = patients,
                selectedPatient = selectedPatient,
                dropdownExpanded = patientDropdownExpanded,
                onDropdownExpandedChange = { patientDropdownExpanded = it },
                searchText = patientSearchText,
                onSearchTextChange = { patientSearchText = it },
                onPatientSelected = { patient ->
                    selectedPatient = patient
                    patientSearchText = patient.name
                    patientDropdownExpanded = false
                }
            )
            
            // Visit Information Card
            VisitInformationCard(
                visitDate = visitDate,
                onVisitDateChange = { visitDate = it },
                osteopath = osteopath,
                generalNotes = generalNotes,
                onGeneralNotesChange = { generalNotes = it }
            )
            
            // Current Visit Data Card
            CurrentVisitDataFormCard(
                weight = weight,
                onWeightChange = { weight = it },
                bmi = bmi,
                onBmiChange = { bmi = it },
                bloodPressure = bloodPressure,
                onBloodPressureChange = { bloodPressure = it },
                cranialIndices = cranialIndices,
                onCranialIndicesChange = { cranialIndices = it }
            )
            
            // Consultation Reason Card
            ConsultationReasonFormCard(
                mainReasonDesc = mainReasonDesc,
                onMainReasonDescChange = { mainReasonDesc = it },
                mainReasonOnset = mainReasonOnset,
                onMainReasonOnsetChange = { mainReasonOnset = it },
                mainReasonPain = mainReasonPain,
                onMainReasonPainChange = { mainReasonPain = it },
                mainReasonPainLevel = mainReasonPainLevel,
                onMainReasonPainLevelChange = { mainReasonPainLevel = it },
                mainReasonFactors = mainReasonFactors,
                onMainReasonFactorsChange = { mainReasonFactors = it },
                secondaryReasonDesc = secondaryReasonDesc,
                onSecondaryReasonDescChange = { secondaryReasonDesc = it },
                secondaryReasonDuration = secondaryReasonDuration,
                onSecondaryReasonDurationChange = { secondaryReasonDuration = it },
                secondaryReasonPainLevel = secondaryReasonPainLevel,
                onSecondaryReasonPainLevelChange = { secondaryReasonPainLevel = it }
            )
            
            // Apparatus Evaluation Section
            Text(
                text = "Valutazione Apparati",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            
            // Note: ViewModel is already created at the top level
            // Use state from ViewModel for apparatus state management
            val apparatusState = state
            
            // Render all 12 apparatus cards
            ApparatusMetadata.allApparatus.forEach { apparatusInfo ->
                val isExpanded = apparatusState.apparatusExpandedStates[apparatusInfo.key] ?: false
                val hasData = when (apparatusInfo.key) {
                    "cranio" -> apparatusState.apparatoCranio.hasData
                    "respiratorio" -> apparatusState.apparatoRespiratorio.hasData
                    "cardiovascolare" -> apparatusState.apparatoCardiovascolare.hasData
                    "gastrointestinale" -> apparatusState.apparatoGastrointestinale.hasData
                    "urinario" -> apparatusState.apparatoUrinario.hasData
                    "riproduttivo" -> apparatusState.apparatoRiproduttivo.hasData
                    "psicoNeuroEndocrino" -> apparatusState.apparatoPsicoNeuroEndocrino.hasData
                    "unghieCute" -> apparatusState.apparatoUnghieCute.hasData
                    "metabolismo" -> apparatusState.apparatoMetabolismo.hasData
                    "linfonodi" -> apparatusState.apparatoLinfonodi.hasData
                    "muscoloScheletrico" -> apparatusState.apparatoMuscoloScheletrico.hasData
                    "nervoso" -> apparatusState.apparatoNervoso.hasData
                    else -> false
                }
                
                ExpandableApparatusCard(
                    apparatusKey = apparatusInfo.key,
                    apparatusName = apparatusInfo.italianName,
                    icon = apparatusInfo.icon,
                    isExpanded = isExpanded,
                    hasData = hasData,
                    onToggleExpanded = {
                        viewModel.sendIntent(AddVisitEvent.ToggleApparatusExpanded(apparatusInfo.key, !isExpanded))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Render appropriate form based on apparatus key
                    when (apparatusInfo.key) {
                        "cranio" -> ApparatoCranioForm(
                            state = apparatusState.apparatoCranio,
                            onEvent = { event -> viewModel.sendIntent(event) }
                        )
                        "respiratorio" -> ApparatoRespiratorioForm(
                            state = apparatusState.apparatoRespiratorio,
                            onEvent = { event -> viewModel.sendIntent(event) }
                        )
                        "linfonodi" -> ApparatoLinfonodiForm(
                            state = apparatusState.apparatoLinfonodi,
                            onEvent = { event -> viewModel.sendIntent(event) }
                        )
                        else -> ApparatusPlaceholderForm(apparatusName = apparatusInfo.italianName)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Patient selection card component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PatientSelectionCard(
    patients: List<Patient>,
    selectedPatient: Patient?,
    dropdownExpanded: Boolean,
    onDropdownExpandedChange: (Boolean) -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onPatientSelected: (Patient) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Informazioni Paziente",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
            // Patient dropdown with search
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = onDropdownExpandedChange
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { onSearchTextChange(it) },
                    readOnly = false,
                    label = { Text("Seleziona Paziente") },
                    placeholder = { Text("Cerca per nome o cognome...") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = dropdownExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { onDropdownExpandedChange(false) }
                ) {
                    val filteredPatients = if (searchText.isBlank()) {
                        patients
                    } else {
                        patients.filter { patient ->
                            patient.name.contains(searchText, ignoreCase = true)
                        }
                    }
                    
                    filteredPatients.forEach { patient ->
                        DropdownMenuItem(
                            text = { 
                                Text(patient.name) 
                            },
                            onClick = {
                                onPatientSelected(patient)
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                    
                    if (filteredPatients.isEmpty() && patients.isNotEmpty()) {
                        DropdownMenuItem(
                            text = { 
                                Text("Nessun paziente trovato") 
                            },
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Visit information card component
 */
@Composable
private fun VisitInformationCard(
    visitDate: String,
    onVisitDateChange: (String) -> Unit,
    osteopath: String,
    generalNotes: String,
    onGeneralNotesChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Informazioni Visita",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
            // Visit date input
            ItalianDateInput(
                value = visitDate,
                onValueChange = onVisitDateChange,
                label = "Data Visita"
            )
            
            // Osteopath name (read-only)
            OutlinedTextField(
                value = osteopath,
                onValueChange = { },
                readOnly = true,
                label = { Text("Osteopata") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // General notes
            OutlinedTextField(
                value = generalNotes,
                onValueChange = onGeneralNotesChange,
                label = { Text("Note Generali") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
        }
    }
}

/**
 * Current visit data form card component
 */
@Composable
private fun CurrentVisitDataFormCard(
    weight: String,
    onWeightChange: (String) -> Unit,
    bmi: String,
    onBmiChange: (String) -> Unit,
    bloodPressure: String,
    onBloodPressureChange: (String) -> Unit,
    cranialIndices: String,
    onCranialIndicesChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Dati Visita Corrente",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Weight
                OutlinedTextField(
                    value = weight,
                    onValueChange = onWeightChange,
                    label = { Text("Peso (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                
                // BMI
                OutlinedTextField(
                    value = bmi,
                    onValueChange = onBmiChange,
                    label = { Text("BMI") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Blood pressure
                OutlinedTextField(
                    value = bloodPressure,
                    onValueChange = onBloodPressureChange,
                    label = { Text("Pressione") },
                    placeholder = { Text("120/80") },
                    modifier = Modifier.weight(1f)
                )
                
                // Cranial indices
                OutlinedTextField(
                    value = cranialIndices,
                    onValueChange = onCranialIndicesChange,
                    label = { Text("Indici Craniali") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Consultation reason form card component
 */
@Composable
private fun ConsultationReasonFormCard(
    mainReasonDesc: String,
    onMainReasonDescChange: (String) -> Unit,
    mainReasonOnset: String,
    onMainReasonOnsetChange: (String) -> Unit,
    mainReasonPain: String,
    onMainReasonPainChange: (String) -> Unit,
    mainReasonPainLevel: String,
    onMainReasonPainLevelChange: (String) -> Unit,
    mainReasonFactors: String,
    onMainReasonFactorsChange: (String) -> Unit,
    secondaryReasonDesc: String,
    onSecondaryReasonDescChange: (String) -> Unit,
    secondaryReasonDuration: String,
    onSecondaryReasonDurationChange: (String) -> Unit,
    secondaryReasonPainLevel: String,
    onSecondaryReasonPainLevelChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Motivo della Consultazione",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
            // Main reason section
            Text(
                text = "Motivo Principale",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            OutlinedTextField(
                value = mainReasonDesc,
                onValueChange = onMainReasonDescChange,
                label = { Text("Descrizione") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = mainReasonOnset,
                onValueChange = onMainReasonOnsetChange,
                label = { Text("Insorgenza") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = mainReasonPain,
                    onValueChange = onMainReasonPainChange,
                    label = { Text("Dolore") },
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = mainReasonPainLevel,
                    onValueChange = onMainReasonPainLevelChange,
                    label = { Text("Livello VAS") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            
            OutlinedTextField(
                value = mainReasonFactors,
                onValueChange = onMainReasonFactorsChange,
                label = { Text("Fattori") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Secondary reason section
            Text(
                text = "Motivo Secondario",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            OutlinedTextField(
                value = secondaryReasonDesc,
                onValueChange = onSecondaryReasonDescChange,
                label = { Text("Descrizione") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = secondaryReasonDuration,
                    onValueChange = onSecondaryReasonDurationChange,
                    label = { Text("Durata") },
                    modifier = Modifier.weight(1f)
                )
                
                OutlinedTextField(
                    value = secondaryReasonPainLevel,
                    onValueChange = onSecondaryReasonPainLevelChange,
                    label = { Text("Livello VAS") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}