package com.narde.gestionaleosteopatabetto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.model.Visit
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.data.database.utils.createDatabaseUtils
import com.narde.gestionaleosteopatabetto.data.sample.SampleData
import com.narde.gestionaleosteopatabetto.ui.viewmodels.toDisplayItems
import com.narde.gestionaleosteopatabetto.ui.screens.AddPatientScreen
import com.narde.gestionaleosteopatabetto.ui.screens.PatientDetailsScreenNew
import com.narde.gestionaleosteopatabetto.ui.screens.EditPatientScreen
import com.narde.gestionaleosteopatabetto.ui.screens.PatientsScreen
import com.narde.gestionaleosteopatabetto.ui.screens.VisitsScreen
import com.narde.gestionaleosteopatabetto.ui.screens.VisitDetailsScreen
import com.narde.gestionaleosteopatabetto.ui.screens.AddVisitScreen
import com.narde.gestionaleosteopatabetto.ui.screens.EditVisitScreen
import org.jetbrains.compose.resources.stringResource
import gestionaleosteopatabetto.composeapp.generated.resources.Res
import gestionaleosteopatabetto.composeapp.generated.resources.*
import kotlinx.coroutines.launch

/**
 * Main application component for the Osteopath Management System
 * Provides a tabbed interface with Patients and Visits management
 * Automatically loads patients from database or falls back to sample data
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OsteopathManagementApp() {
    val coroutineScope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableStateOf(0) }
    var showAddPatientScreen by remember { mutableStateOf(false) }

    // Patient screens - use nullable IDs (null = not shown)
    var patientDetailsId by remember { mutableStateOf<String?>(null) }
    var editPatientId by remember { mutableStateOf<String?>(null) }
    
    // Visit screens - use nullable objects (null = not shown)
    var selectedVisit by remember { mutableStateOf<Visit?>(null) }
    var editVisitId by remember { mutableStateOf<String?>(null) }
    var showAddVisitScreen by remember { mutableStateOf(false) }
    
    // Delete confirmation state
    var showDeleteVisitDialog by remember { mutableStateOf(false) }
    var visitToDelete by remember { mutableStateOf<Visit?>(null) }
    
    // Create DatabaseUtils instance
    val databaseUtils = remember { createDatabaseUtils() }

    // State for patients and visits data
    var patients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    var visits by remember { mutableStateOf<List<Visit>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Function to refresh patients list
    val refreshPatients: suspend () -> Unit = {
        try {
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getPatientRepository()
                if (repository != null) {
                    val dbPatients = repository.getAllPatients()
                    patients = dbPatients.map { databaseUtils.toUIPatient(it) }
                }
            }
        } catch (e: Exception) {
            println("Error refreshing patients: ${e.message}")
        }
    }
    
    // Function to refresh visits list
    val refreshVisits: suspend () -> Unit = {
        try {
            println("OsteopathManagementApp: Starting visits refresh")
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getVisitRepository()
                if (repository != null) {
                    val dbVisits = repository.getAllVisits()
                    println("OsteopathManagementApp: Loaded ${dbVisits.size} visits from database")
                    visits = dbVisits.map { databaseUtils.toUIVisit(it) }
                    println("OsteopathManagementApp: Converted to ${visits.size} UI visits")
                } else {
                    println("OsteopathManagementApp: Visit repository is null")
                }
            } else {
                println("OsteopathManagementApp: Database not supported")
            }
        } catch (e: Exception) {
            println("Error refreshing visits: ${e.message}")
            e.printStackTrace()
        }
    }
    
    // Function to delete a patient
    val deletePatient: (String, String) -> Unit = { patientId, patientName ->
        coroutineScope.launch {
            try {
                println("OsteopathApp: Starting deletion for patient ID: $patientId, Name: $patientName")
                
                if (isDatabaseSupported()) {
                    val repository = DatabaseInitializer.getPatientRepository()
                    if (repository != null) {
                        // First check if patient exists before deletion
                        val existingPatient = repository.getPatientById(patientId)
                        if (existingPatient != null) {
                            println("OsteopathApp: Patient found, proceeding with deletion")
                            repository.deletePatient(patientId)
                            refreshPatients() // Refresh the list after deletion
                            println("OsteopathApp: Patient $patientName deleted successfully")
                        } else {
                            println("OsteopathApp: ERROR - Patient not found for deletion")
                        }
                    } else {
                        println("OsteopathApp: ERROR - Repository is null")
                    }
                } else {
                    println("OsteopathApp: ERROR - Database not supported for deletion")
                }
            } catch (e: Exception) {
                println("OsteopathApp: ERROR deleting patient: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    // Function to delete a visit
    val deleteVisit: (Visit) -> Unit = { visit ->
        coroutineScope.launch {
            try {
                println("OsteopathApp: Starting deletion for visit ID: ${visit.idVisita}")
                
                if (isDatabaseSupported()) {
                    val repository = DatabaseInitializer.getVisitRepository()
                    if (repository != null) {
                        // First check if visit exists before deletion
                        val existingVisit = repository.getVisitById(visit.idVisita)
                        if (existingVisit != null) {
                            println("OsteopathApp: Visit found, proceeding with deletion")
                            repository.deleteVisit(visit.idVisita)
                            refreshVisits() // Refresh the list after deletion
                            println("OsteopathApp: Visit ${visit.idVisita} deleted successfully")
                        } else {
                            println("OsteopathApp: ERROR - Visit not found for deletion")
                        }
                    } else {
                        println("OsteopathApp: ERROR - Visit repository is null")
                    }
                } else {
                    println("OsteopathApp: ERROR - Database not supported for visit deletion")
                }
            } catch (e: Exception) {
                println("OsteopathApp: ERROR deleting visit: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Initialize database and load patients
    LaunchedEffect(Unit) {
        try {
            // Initialize database if supported on current platform
            if (isDatabaseSupported()) {
                DatabaseInitializer.initializeDatabase()

                // Load patients from database
                val repository = DatabaseInitializer.getPatientRepository()
                if (repository != null) {
                    val dbPatients = repository.getAllPatients()
                    // Convert database patients to UI patients
                    patients = dbPatients.map { databaseUtils.toUIPatient(it) }
                } else {
                    // Fallback to sample data if repository is null
                    patients = SampleData.patients
                }
            } else {
                // Database not supported on this platform, use sample data
                patients = SampleData.patients
            }

            // Load visits from database
            if (isDatabaseSupported()) {
                val visitRepository = DatabaseInitializer.getVisitRepository()
                if (visitRepository != null) {
                    val dbVisits = visitRepository.getAllVisits()
                    visits = dbVisits.map { databaseUtils.toUIVisit(it) }
                } else {
                    visits = SampleData.visits
                }
            } else {
                visits = SampleData.visits
            }

        } catch (e: Exception) {
            // If there's any error, fall back to sample data
            println("Error loading from database: ${e.message}")
            patients = SampleData.patients
            visits = SampleData.visits
        } finally {
            isLoading = false
        }
    }

    // Tab titles for navigation using localized strings
    val tabs = listOf(
        stringResource(Res.string.tab_patients),
                stringResource(Res.string.tab_visits)
    )

    // Handle different screen states
    when {
        selectedVisit != null -> {
            // Find the corresponding patient
            val patient = patients.find { it.id == selectedVisit!!.idPaziente }
            if (patient != null) {
                VisitDetailsScreen(
                    visit = selectedVisit!!,
                    patient = patient,
                    onBackClick = {
                        selectedVisit = null
                    },
                    onEditClick = {
                        editVisitId = selectedVisit?.idVisita
                        selectedVisit = null
                    }
                )
            } else {
                // Fallback if patient not found
                VisitDetailsScreen(
                    visit = selectedVisit!!,
                    patient = Patient(
                        id = selectedVisit!!.idPaziente,
                        name = "Paziente non trovato",
                        phone = "",
                        email = "",
                        age = 0
                    ),
                    onBackClick = {
                        selectedVisit = null
                    },
                    onEditClick = {
                        editVisitId = selectedVisit?.idVisita
                        selectedVisit = null
                    }
                )
            }
        }
        showAddVisitScreen -> {
            AddVisitScreen(
                patients = patients,
                onBackClick = {
                    showAddVisitScreen = false
                },
                onVisitSaved = { visit ->
                    showAddVisitScreen = false
                    // Navigate to visits tab to show the updated list
                    selectedTabIndex = 1
                    // Refresh visits list
                    coroutineScope.launch {
                        refreshVisits()
                    }
                    println("Visit saved: $visit - Navigated to visits tab")
                }
            )
        }
        editVisitId != null -> {
            EditVisitScreen(
                visitId = editVisitId!!,
                patients = patients,
                onBackClick = {
                },
                onVisitUpdated = { _ ->
                    val visitIdToReload = editVisitId
                    
                    // Refresh visits list and reload the specific visit from database
                    coroutineScope.launch {
                        try {
                            // Refresh visits list first
                            refreshVisits()
                            
                            // Reload the specific visit from database to ensure we have latest data
                            if (visitIdToReload != null && isDatabaseSupported()) {
                                val repository = DatabaseInitializer.getVisitRepository()
                                if (repository != null) {
                                    val dbVisit = repository.getVisitById(visitIdToReload)
                                    if (dbVisit != null) {
                                        // Convert database visit to UI visit with complete data
                                        val reloadedVisit = databaseUtils.toUIVisit(dbVisit)
                                        
                                        // Update selectedVisit if it matches the updated visit
                                        // This ensures VisitDetailsScreen shows the latest data
                                        if (selectedVisit?.idVisita == visitIdToReload) {
                                            selectedVisit = reloadedVisit
                                        }
                                        
                                        // Also update the visits list entry if it exists
                                        visits = visits.map { visit ->
                                            if (visit.idVisita == visitIdToReload) {
                                                reloadedVisit
                                            } else {
                                                visit
                                            }
                                        }
                                        
                                        println("OsteopathManagementApp: Reloaded visit ${reloadedVisit.idVisita} from database")
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            println("Error reloading visit after update: ${e.message}")
                            e.printStackTrace()
                        }
                    }
                }
            )
        }
        editPatientId != null -> {
            EditPatientScreen(
                patientId = editPatientId!!,
                onBackClick = {
                    editPatientId = null
                },
                onPatientUpdated = { _ ->
                    editPatientId = null
                    // Refresh patients list
                    coroutineScope.launch {
                        refreshPatients()
                    }
                }
            )
        }
        patientDetailsId != null -> {
            PatientDetailsScreenNew(
                patientId = patientDetailsId!!,
                onBackClick = {
                    patientDetailsId = null
                },
                onEditClick = {
                    editPatientId = patientDetailsId
                    patientDetailsId = null
                }
            )
        }
        showAddPatientScreen -> {
            AddPatientScreen(
                onBackClick = { showAddPatientScreen = false },
                onPatientSaved = {
                    showAddPatientScreen = false
                    // Refresh the patients list
                    coroutineScope.launch {
                        refreshPatients()
                    }
                }
            )
                 }
         else -> {
            // Main application content with FAB
            Scaffold(
                floatingActionButton = {
                    // Show FAB for both tabs
                    when (selectedTabIndex) {
                        0 -> {
                            // Add patient FAB
                            FloatingActionButton(
                                onClick = { showAddPatientScreen = true },
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
                                    contentDescription = stringResource(Res.string.add_patient)
                                )
                            }
                        }
                        1 -> {
                            // Add visit FAB
                            FloatingActionButton(
                                onClick = { showAddVisitScreen = true },
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
                                    contentDescription = "Aggiungi Visita"
                                )
                            }
                        }
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Application header with language toggle
                    TopAppBar(
                        title = { Text(stringResource(Res.string.app_title)) },
                        actions = {
                            // Simple language indicator - use LanguageSwitcher component for full functionality
                            Text(
                                text = "Multi-language App",
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
                        selectedTabIndex = selectedTabIndex,
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title) }
                            )
                        }
                    }

                    // Content based on selected tab
                    if (isLoading) {
                        // Show loading indicator while initializing database
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = stringResource(Res.string.loading_patients),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    } else {
                        // Show content when data is loaded
                        when (selectedTabIndex) {
                            0 -> PatientsScreen(
                                patients = patients,
                                onPatientClick = { patient ->
                                    patientDetailsId = patient.id
                                },
                                onDeletePatient = deletePatient
                            )

                            1 -> {
                                // Map visits to display items with patient names
                                // This follows clean architecture by enriching data at the composition level
                                val visitDisplayItems = visits.toDisplayItems(patients)
                                
                                VisitsScreen(
                                    visitDisplayItems = visitDisplayItems,
                                    onVisitClick = { visit ->
                                    // Reload visit from database to ensure we have latest data
                                    coroutineScope.launch {
                                        try {
                                            if (isDatabaseSupported()) {
                                                val repository = DatabaseInitializer.getVisitRepository()
                                                if (repository != null) {
                                                    val dbVisit = repository.getVisitById(visit.idVisita)
                                                    if (dbVisit != null) {
                                                        // Convert database visit to UI visit with complete data
                                                        val reloadedVisit = databaseUtils.toUIVisit(dbVisit)
                                                        selectedVisit = reloadedVisit
                                                        println("OsteopathManagementApp: Reloaded visit ${reloadedVisit.idVisita} from database for details view")
                                                    } else {
                                                        // Fallback to visit from list if not found in database
                                                        selectedVisit = visit
                                                    }
                                                } else {
                                                    // Fallback to visit from list if repository unavailable
                                                    selectedVisit = visit
                                                }
                                            } else {
                                                // Fallback to visit from list if database not supported
                                                selectedVisit = visit
                                            }
                                        } catch (e: Exception) {
                                            println("Error reloading visit for details view: ${e.message}")
                                            e.printStackTrace()
                                            // Fallback to visit from list on error
                                            selectedVisit = visit
                                        }
                                    }
                                },
                                onDeleteVisit = { visit ->
                                    visitToDelete = visit
                                    showDeleteVisitDialog = true
                                }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Delete visit confirmation dialog
    if (showDeleteVisitDialog && visitToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteVisitDialog = false
                visitToDelete = null
            },
            title = {
                Text("Conferma eliminazione")
            },
            text = {
                Text("Sei sicuro di voler eliminare la visita del ${visitToDelete?.dataVisita} per il paziente ${visitToDelete?.idPaziente}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        visitToDelete?.let { visit ->
                            deleteVisit(visit)
                        }
                        showDeleteVisitDialog = false
                        visitToDelete = null
                    }
                ) {
                    Text("Elimina")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteVisitDialog = false
                        visitToDelete = null
                    }
                ) {
                    Text("Annulla")
                }
            }
        )
    }
}