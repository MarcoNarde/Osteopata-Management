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
import com.narde.gestionaleosteopatabetto.ui.factories.rememberEditVisitViewModel
import com.narde.gestionaleosteopatabetto.ui.mvi.EditVisitEvent
import com.narde.gestionaleosteopatabetto.ui.mvi.EditVisitSideEffect

/**
 * Screen for editing existing visits in the osteopath management system
 * Uses MVI pattern with EditVisitViewModel for state management
 * Loads visit by ID instead of passing visit object
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVisitScreen(
    visitId: String,
    patients: List<Patient>?,
    onBackClick: () -> Unit,
    onVisitUpdated: (Visit) -> Unit,
    modifier: Modifier = Modifier
) {
    // ViewModel with MVI pattern - no null handling needed
    val viewModel = rememberEditVisitViewModel(patients)
    val state by viewModel.state.collectAsState()
    val sideEffects = viewModel.sideEffects
    
    // Handle side effects
    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is EditVisitSideEffect.VisitUpdated -> {
                    onVisitUpdated(sideEffect.visit)
                }
                is EditVisitSideEffect.NavigateBack -> {
                    onBackClick()
                }
                is EditVisitSideEffect.ShowError -> {
                    // TODO: Show error message to user
                    println("EditVisitScreen: Error - ${sideEffect.message}")
                }
                is EditVisitSideEffect.SavingStarted -> {
                    println("EditVisitScreen: Saving started")
                }
                is EditVisitSideEffect.SavingCompleted -> {
                    println("EditVisitScreen: Saving completed")
                }
                else -> {
                    // Handle any other SideEffect types if needed
                    println("EditVisitScreen: Unhandled side effect - $sideEffect")
                }
            }
        }
    }
    
    // Load visit data when screen is first displayed
    LaunchedEffect(visitId) {
        viewModel.sendIntent(EditVisitEvent.LoadVisitById(visitId))
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
                        viewModel.sendIntent(EditVisitEvent.SaveVisit)
                    },
                    enabled = state.hasChanges && !state.isSaving
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
                            state.hasChanges -> "Salva Modifiche"
                            else -> "Nessuna Modifica"
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
                        expanded = state.patientDropdownExpanded,
                        onExpandedChange = { expanded ->
                            viewModel.sendIntent(EditVisitEvent.TogglePatientDropdown(expanded))
                        }
                    ) {
                        OutlinedTextField(
                            value = state.selectedPatient?.let { "${it.name} (${it.id})" } ?: "",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Paziente Selezionato") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = state.patientDropdownExpanded
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = state.patientDropdownExpanded,
                            onDismissRequest = { 
                                viewModel.sendIntent(EditVisitEvent.TogglePatientDropdown(false))
                            }
                        ) {
                            // Search field
                            OutlinedTextField(
                                value = state.patientSearchText,
                                onValueChange = { searchText ->
                                    viewModel.sendIntent(EditVisitEvent.UpdatePatientSearchText(searchText))
                                },
                                label = { Text("Cerca paziente...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                singleLine = true
                            )
                            
                            state.filteredPatients.forEach { patient ->
                                DropdownMenuItem(
                                    text = { Text("${patient.name} (${patient.id})") },
                                    onClick = {
                                        viewModel.sendIntent(EditVisitEvent.UpdatePatient(patient))
                                        viewModel.sendIntent(EditVisitEvent.TogglePatientDropdown(false))
                                        viewModel.sendIntent(EditVisitEvent.UpdatePatientSearchText(""))
                                    }
                                )
                            }
                        }
                    }
                    
                    // Visit Date
                    ItalianDateInput(
                        value = state.visitDate,
                        onValueChange = { date ->
                            viewModel.sendIntent(EditVisitEvent.UpdateVisitDate(date))
                        },
                        label = "Data Visita",
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Osteopath (Read-only)
                    OutlinedTextField(
                        value = state.osteopath,
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
                        value = state.generalNotes,
                        onValueChange = { notes ->
                            viewModel.sendIntent(EditVisitEvent.UpdateGeneralNotes(notes))
                        },
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
                            value = state.weight,
                            onValueChange = { weight ->
                                viewModel.sendIntent(EditVisitEvent.UpdateWeight(weight))
                            },
                            label = { Text("Peso (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = state.bmi,
                            onValueChange = { bmi ->
                                viewModel.sendIntent(EditVisitEvent.UpdateBmi(bmi))
                            },
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
                            value = state.bloodPressure,
                            onValueChange = { pressure ->
                                viewModel.sendIntent(EditVisitEvent.UpdateBloodPressure(pressure))
                            },
                            label = { Text("Pressione") },
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = state.cranialIndices,
                            onValueChange = { indices ->
                                viewModel.sendIntent(EditVisitEvent.UpdateCranialIndices(indices))
                            },
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
                        value = state.mainReason,
                        onValueChange = { reason ->
                            viewModel.sendIntent(EditVisitEvent.UpdateMainReason(reason))
                        },
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
                            value = state.mainReasonOnset,
                            onValueChange = { onset ->
                                viewModel.sendIntent(EditVisitEvent.UpdateMainReasonOnset(onset))
                            },
                            label = { Text("Insorgenza") },
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = state.mainReasonPain,
                            onValueChange = { pain ->
                                viewModel.sendIntent(EditVisitEvent.UpdateMainReasonPain(pain))
                            },
                            label = { Text("Dolore") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = state.mainReasonVas,
                            onValueChange = { vas ->
                                viewModel.sendIntent(EditVisitEvent.UpdateMainReasonVas(vas))
                            },
                            label = { Text("VAS (0-10)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = state.mainReasonFactors,
                            onValueChange = { factors ->
                                viewModel.sendIntent(EditVisitEvent.UpdateMainReasonFactors(factors))
                            },
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
                        value = state.secondaryReason,
                        onValueChange = { reason ->
                            viewModel.sendIntent(EditVisitEvent.UpdateSecondaryReason(reason))
                        },
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
                            value = state.secondaryReasonDuration,
                            onValueChange = { duration ->
                                viewModel.sendIntent(EditVisitEvent.UpdateSecondaryReasonDuration(duration))
                            },
                            label = { Text("Durata") },
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = state.secondaryReasonVas,
                            onValueChange = { vas ->
                                viewModel.sendIntent(EditVisitEvent.UpdateSecondaryReasonVas(vas))
                            },
                            label = { Text("VAS (0-10)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Error message display
            state.errorMessage?.let { errorMessage ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            // Bottom spacing
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}