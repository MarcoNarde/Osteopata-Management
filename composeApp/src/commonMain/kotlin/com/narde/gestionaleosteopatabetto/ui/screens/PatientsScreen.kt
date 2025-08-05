package com.narde.gestionaleosteopatabetto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.ui.components.PatientCard
import com.narde.gestionaleosteopatabetto.ui.components.DeleteConfirmationDialog
import org.jetbrains.compose.resources.stringResource
import gestionaleosteopatabetto.composeapp.generated.resources.Res
import gestionaleosteopatabetto.composeapp.generated.resources.*

/**
 * Enhanced Patients management screen with improved UX
 * Features: Better visual hierarchy, empty state, improved spacing and accessibility
 * @param patients List of patients to display
 * @param onPatientClick Callback function called when a patient card is clicked
 * @param onDeletePatient Callback function for patient deletion
 */
@Composable
fun PatientsScreen(
    patients: List<Patient>,
    onPatientClick: (Patient) -> Unit = {},
    onDeletePatient: (String, String) -> Unit = { _, _ -> }
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var patientToDelete by remember { mutableStateOf<Patient?>(null) }
    
    // Main content with improved accessibility
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Enhanced header with better visual hierarchy
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.large
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                    Column {
                        Text(
                            text = stringResource(Res.string.patients_title),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "${patients.size} pazienti registrati",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            
            // Content area with better organization
            if (patients.isEmpty()) {
                // Enhanced empty state with better UX
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "Nessun paziente trovato",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Inizia aggiungendo il tuo primo paziente usando il pulsante '+' in alto",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                // Enhanced patients list with better spacing and organization
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(patients) { patient ->
                        PatientCard(
                            patient = patient,
                            onClick = { onPatientClick(patient) },
                            onDeleteClick = {
                                patientToDelete = patient
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
        
        // Delete confirmation dialog - positioned at the root level of this composable
        // This ensures it's not constrained by the content layout
        if (showDeleteDialog) {
            DeleteConfirmationDialog(
                isVisible = showDeleteDialog,
                patientName = patientToDelete?.name ?: "",
                onConfirm = {
                    patientToDelete?.let { patient ->
                        onDeletePatient(patient.id, patient.name)
                    }
                    showDeleteDialog = false
                    patientToDelete = null
                },
                onDismiss = {
                    showDeleteDialog = false
                    patientToDelete = null
                }
            )
        }
    }
} 