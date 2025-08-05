package com.narde.gestionaleosteopatabetto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.ui.components.PatientCard
import com.narde.gestionaleosteopatabetto.ui.components.DeleteConfirmationDialog
import org.jetbrains.compose.resources.stringResource
import gestionaleosteopatabetto.composeapp.generated.resources.Res
import gestionaleosteopatabetto.composeapp.generated.resources.*

/**
 * Patients management screen
 * Displays a list of patients with basic information in a scrollable list
 * @param patients List of patients to display
 * @param onPatientClick Callback function called when a patient card is clicked
 */
@Composable
fun PatientsScreen(
    patients: List<Patient>,
    onPatientClick: (Patient) -> Unit = {},
    onDeletePatient: (String, String) -> Unit = { _, _ -> }
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var patientToDelete by remember { mutableStateOf<Patient?>(null) }
    
    // Main content in a Box to handle layout properly
    Box(modifier = Modifier.fillMaxSize()) {
        // Background and content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Header for patients section with enhanced styling
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.patients_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }
                
                // Patients list with enhanced spacing
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
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