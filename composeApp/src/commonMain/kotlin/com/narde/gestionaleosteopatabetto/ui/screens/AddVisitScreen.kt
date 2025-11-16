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
    
    // Save visit use case
    val saveVisitUseCase = remember { SaveVisitUseCaseImpl() }
    
    // Form state
    var selectedPatient by remember { mutableStateOf<Patient?>(null) }
    var visitDate by remember { mutableStateOf("") }
    val osteopath = "Roberto Caeran" // Fixed osteopath name - not editable
    var generalNotes by remember { mutableStateOf("") }
    
    // Save state
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
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
                        // Save visit to database using SaveVisitUseCase
                        coroutineScope.launch {
                            try {
                                isSaving = true

                                println("AddVisitScreen: Starting visit save process")
                                
                                // Create domain visit model
                                val domainVisit = DomainVisit(
                                    idVisita = "VIS_" + System.currentTimeMillis(),
                                    idPaziente = selectedPatient?.id ?: "",
                                    dataVisita = DateUtils.parseItalianDate(visitDate) ?: throw IllegalArgumentException("Invalid date format: $visitDate"),
                                    osteopata = osteopath,
                                    noteGenerali = generalNotes,
                                    datiVisitaCorrente = null,
                                    motivoConsulto = null
                                )
                                
                                println("AddVisitScreen: Created domain visit - ID: ${domainVisit.idVisita}")
                                
                                // Save using use case
                                saveVisitUseCase(domainVisit).collect { result ->
                                    when {
                                        result.isSuccess -> {
                                            val savedVisit = result.getOrNull()
                                            println("AddVisitScreen: Visit saved successfully - ID: ${savedVisit?.idVisita}")
                                            
                                            // Create UI visit for callback
                                            val uiVisit = Visit(
                                                idVisita = savedVisit?.idVisita ?: domainVisit.idVisita,
                                                idPaziente = savedVisit?.idPaziente ?: domainVisit.idPaziente,
                                                dataVisita = savedVisit?.dataVisitaString ?: visitDate,
                                                osteopata = savedVisit?.osteopata ?: domainVisit.osteopata,
                                                noteGenerali = savedVisit?.noteGenerali ?: domainVisit.noteGenerali
                                            )
                                            
                                            onVisitSaved(uiVisit)
                                            isSaving = false
                                        }
                                        result.isFailure -> {
                                            val error = result.exceptionOrNull()?.message ?: "Unknown error"
                                            println("AddVisitScreen: Save failed - $error")
                                            isSaving = false
                                        }
                                    }
                                }
                                
                            } catch (e: Exception) {
                                println("AddVisitScreen: Exception in save process - ${e.message}")
                                e.printStackTrace()
                                isSaving = false
                            }
                        }
                    },
                    enabled = selectedPatient != null && visitDate.isNotBlank() && !isSaving
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Salva")
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
            
            // Note: This section uses ViewModel pattern for apparatus state management
            // Create ViewModel instance using factory for proper dependency injection
            val viewModel = remember { ViewModelFactory.createAddVisitViewModel() }
            
            val apparatusState by viewModel.state.collectAsState()
            
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