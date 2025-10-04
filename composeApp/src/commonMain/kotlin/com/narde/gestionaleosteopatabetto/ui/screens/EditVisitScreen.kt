package com.narde.gestionaleosteopatabetto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.model.Visit
import com.narde.gestionaleosteopatabetto.ui.components.ItalianDateInput
import com.narde.gestionaleosteopatabetto.domain.usecases.UpdateVisitUseCaseImpl
import com.narde.gestionaleosteopatabetto.domain.models.Visit as DomainVisit
import com.narde.gestionaleosteopatabetto.utils.DateUtils
import kotlinx.coroutines.launch

/**
 * Screen for editing existing visits in the osteopath management system
 * Pre-populated form with existing visit data for modification
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVisitScreen(
    visit: Visit,
    patients: List<Patient>,
    onBackClick: () -> Unit,
    onVisitUpdated: (Visit) -> Unit,
    modifier: Modifier = Modifier
) {
    // Coroutine scope for async operations
    val coroutineScope = rememberCoroutineScope()
    
    // Update visit use case
    val updateVisitUseCase = remember { UpdateVisitUseCaseImpl() }
    
    // Form state - pre-populated with existing visit data
    var selectedPatient by remember { mutableStateOf<Patient?>(null) }
    var visitDate by remember { mutableStateOf(visit.dataVisita) }
    val osteopath = visit.osteopata // Use existing osteopath name
    var generalNotes by remember { mutableStateOf(visit.noteGenerali) }
    
    // Save state
    var isSaving by remember { mutableStateOf(false) }
    
    // Patient dropdown state
    var patientDropdownExpanded by remember { mutableStateOf(false) }
    var patientSearchText by remember { mutableStateOf("") }
    
    // Current visit data state
    var weight by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }
    var bloodPressure by remember { mutableStateOf("") }
    var cranialIndices by remember { mutableStateOf("") }
    
    // Consultation reason state
    var mainReason by remember { mutableStateOf("") }
    var mainReasonOnset by remember { mutableStateOf("") }
    var mainReasonPain by remember { mutableStateOf("") }
    var mainReasonVas by remember { mutableStateOf("") }
    var mainReasonFactors by remember { mutableStateOf("") }
    
    var secondaryReason by remember { mutableStateOf("") }
    var secondaryReasonDuration by remember { mutableStateOf("") }
    var secondaryReasonVas by remember { mutableStateOf("") }
    
    // Find and set the selected patient from the visit
    LaunchedEffect(visit.idPaziente, patients) {
        selectedPatient = patients.find { it.id == visit.idPaziente }
    }
    
    // Filter patients based on search text
    val filteredPatients = remember(patientSearchText, patients) {
        if (patientSearchText.isEmpty()) {
            patients
        } else {
            patients.filter { patient ->
                patient.name.contains(patientSearchText, ignoreCase = true) ||
                patient.id.contains(patientSearchText, ignoreCase = true)
            }
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Modifica Visita",
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
                        // Update visit to database using UpdateVisitUseCase
                        coroutineScope.launch {
                            try {
                                isSaving = true
                                
                                println("EditVisitScreen: Starting visit update process")
                                
                                // Create domain visit model with updated data
                                val domainVisit = DomainVisit(
                                    idVisita = visit.idVisita, // Keep existing ID
                                    idPaziente = selectedPatient?.id ?: visit.idPaziente,
                                    dataVisita = DateUtils.parseItalianDate(visitDate) ?: throw IllegalArgumentException("Invalid date format: $visitDate"),
                                    osteopata = osteopath,
                                    noteGenerali = generalNotes,
                                    datiVisitaCorrente = null,
                                    motivoConsulto = null
                                )
                                
                                println("EditVisitScreen: Created domain visit - ID: ${domainVisit.idVisita}")
                                
                                // Update using use case
                                updateVisitUseCase(domainVisit).collect { result ->
                                    when {
                                        result.isSuccess -> {
                                            val updatedVisit = result.getOrNull()
                                            println("EditVisitScreen: Visit updated successfully - ID: ${updatedVisit?.idVisita}")
                                            
                                            // Create UI visit for callback
                                            val uiVisit = Visit(
                                                idVisita = updatedVisit?.idVisita ?: domainVisit.idVisita,
                                                idPaziente = updatedVisit?.idPaziente ?: domainVisit.idPaziente,
                                                dataVisita = updatedVisit?.dataVisitaString ?: visitDate,
                                                osteopata = updatedVisit?.osteopata ?: domainVisit.osteopata,
                                                noteGenerali = updatedVisit?.noteGenerali ?: domainVisit.noteGenerali
                                            )
                                            
                                            onVisitUpdated(uiVisit)
                                            isSaving = false
                                        }
                                        result.isFailure -> {
                                            val error = result.exceptionOrNull()?.message ?: "Unknown error"
                                            println("EditVisitScreen: Update failed - $error")
                                            isSaving = false
                                        }
                                    }
                                }
                                
                            } catch (e: Exception) {
                                println("EditVisitScreen: Exception in update process - ${e.message}")
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
                    Text("Salva Modifiche")
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
            // Basic Information Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Informazioni Base",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    // Patient Selection Dropdown
                    ExposedDropdownMenuBox(
                        expanded = patientDropdownExpanded,
                        onExpandedChange = { patientDropdownExpanded = !patientDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedPatient?.let { "${it.name} (${it.id})" } ?: "",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Paziente Selezionato") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = patientDropdownExpanded
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = patientDropdownExpanded,
                            onDismissRequest = { patientDropdownExpanded = false }
                        ) {
                            // Search field
                            OutlinedTextField(
                                value = patientSearchText,
                                onValueChange = { patientSearchText = it },
                                label = { Text("Cerca paziente...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                singleLine = true
                            )
                            
                            filteredPatients.forEach { patient ->
                                DropdownMenuItem(
                                    text = { Text("${patient.name} (${patient.id})") },
                                    onClick = {
                                        selectedPatient = patient
                                        patientDropdownExpanded = false
                                        patientSearchText = ""
                                    }
                                )
                            }
                        }
                    }
                    
                    // Visit Date
                    ItalianDateInput(
                        value = visitDate,
                        onValueChange = { visitDate = it },
                        label = "Data Visita",
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Osteopath (Read-only)
                    OutlinedTextField(
                        value = osteopath,
                        onValueChange = { },
                        label = { Text("Osteopata") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )
                    
                    // General Notes
                    OutlinedTextField(
                        value = generalNotes,
                        onValueChange = { generalNotes = it },
                        label = { Text("Note Generali") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }
            
            // Current Visit Data Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Dati Visita Corrente",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Peso (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = bmi,
                            onValueChange = { bmi = it },
                            label = { Text("BMI") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = bloodPressure,
                            onValueChange = { bloodPressure = it },
                            label = { Text("Pressione") },
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = cranialIndices,
                            onValueChange = { cranialIndices = it },
                            label = { Text("Indici Craniali") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Consultation Reason Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Motivo Consulto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    // Main Reason
                    Text(
                        text = "Motivo Principale",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    
                    OutlinedTextField(
                        value = mainReason,
                        onValueChange = { mainReason = it },
                        label = { Text("Descrizione") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 3
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = mainReasonOnset,
                            onValueChange = { mainReasonOnset = it },
                            label = { Text("Insorgenza") },
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = mainReasonPain,
                            onValueChange = { mainReasonPain = it },
                            label = { Text("Dolore") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = mainReasonVas,
                            onValueChange = { mainReasonVas = it },
                            label = { Text("VAS (0-10)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = mainReasonFactors,
                            onValueChange = { mainReasonFactors = it },
                            label = { Text("Fattori") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // Secondary Reason
                    Text(
                        text = "Motivo Secondario",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    
                    OutlinedTextField(
                        value = secondaryReason,
                        onValueChange = { secondaryReason = it },
                        label = { Text("Descrizione") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 3
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = secondaryReasonDuration,
                            onValueChange = { secondaryReasonDuration = it },
                            label = { Text("Durata") },
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = secondaryReasonVas,
                            onValueChange = { secondaryReasonVas = it },
                            label = { Text("VAS (0-10)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Bottom spacing
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
