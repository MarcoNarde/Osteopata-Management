package com.narde.gestionaleosteopatabetto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.narde.gestionaleosteopatabetto.ui.screens.AddPatientScreen
import com.narde.gestionaleosteopatabetto.ui.screens.PatientDetailsScreen
import com.narde.gestionaleosteopatabetto.data.database.utils.createDatabaseUtils
import com.narde.gestionaleosteopatabetto.ui.screens.PatientsScreen
import com.narde.gestionaleosteopatabetto.ui.screens.VisitsScreen
import com.narde.gestionaleosteopatabetto.ui.viewmodels.MainAppViewModel
import com.narde.gestionaleosteopatabetto.ui.viewmodels.AppScreen
import org.jetbrains.compose.resources.stringResource
import gestionaleosteopatabetto.composeapp.generated.resources.Res
import gestionaleosteopatabetto.composeapp.generated.resources.*

/**
 * ✅ REFACTORED: Clean architecture with ViewModels
 * - Centralized state management
 * - Business logic in ViewModels
 * - Testable and maintainable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OsteopathManagementAppRefactored(
    viewModel: MainAppViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val databaseUtils = remember { createDatabaseUtils() }
    
    // Tab titles for navigation using localized strings
    val tabs = listOf(
        stringResource(Res.string.tab_patients),
        stringResource(Res.string.tab_visits)
    )
    
    // Handle different screen states
    when (uiState.currentScreen) {
        AppScreen.AddPatient -> {
            AddPatientScreen(
                onBackClick = { viewModel.navigateBack() },
                onPatientSaved = { 
                    viewModel.navigateBack()
                    viewModel.refreshPatients()
                }
            )
        }
        AppScreen.PatientDetails -> {
            uiState.selectedDatabasePatient?.let { dbPatient ->
                val uiPatient = databaseUtils.toUIPatient(dbPatient)
                PatientDetailsScreen(
                    patient = uiPatient,
                    databasePatient = dbPatient,
                    onBackClick = { viewModel.navigateBack() },
                    onPatientUpdated = {
                        viewModel.refreshPatients()
                    }
                )
            }
        }
        AppScreen.Main -> {
            // Main application content with FAB
            Scaffold(
                floatingActionButton = {
                    // Only show FAB on Patients tab
                    if (uiState.selectedTabIndex == 0) {
                        FloatingActionButton(
                            onClick = { viewModel.showAddPatientScreen() },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(Res.string.add_patient)
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Application header
                    TopAppBar(
                        title = { Text(stringResource(Res.string.app_title)) },
                        actions = {
                            Text(
                                text = "MVVM Architecture",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )

                    // Tab row for navigation
                    TabRow(
                        selectedTabIndex = uiState.selectedTabIndex,
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = uiState.selectedTabIndex == index,
                                onClick = { viewModel.selectTab(index) },
                                text = { Text(title) }
                            )
                        }
                    }

                    // Content based on selected tab
                    if (uiState.isLoading) {
                        // Show loading indicator
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = stringResource(Res.string.loading_patients),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    } else {
                        when (uiState.selectedTabIndex) {
                            0 -> PatientsScreen(
                                patients = uiState.patients,
                                onPatientClick = { patient ->
                                    // TODO: Convert UI Patient to Database Patient or implement navigation for UI model
                                    // For now, this just logs the click
                                    println("Clicked on UI patient: ${patient.name}")
                                }
                            )
                            1 -> VisitsScreen(visits = uiState.visits)
                        }
                    }
                    
                    // Error message display
                    uiState.errorMessage?.let { error ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = { viewModel.clearError() }) {
                                    Text("OK")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}