package com.narde.gestionaleosteopatabetto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.database.models.Patient as DatabasePatient
import com.narde.gestionaleosteopatabetto.data.database.utils.*
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.ui.components.ItalianDateInput
import com.narde.gestionaleosteopatabetto.ui.viewmodels.EditPatientViewModel
import com.narde.gestionaleosteopatabetto.ui.viewmodels.PatientField
import com.narde.gestionaleosteopatabetto.ui.viewmodels.ConsentType
import com.narde.gestionaleosteopatabetto.ui.factories.ViewModelFactory
import org.jetbrains.compose.resources.stringResource
import gestionaleosteopatabetto.composeapp.generated.resources.Res
import gestionaleosteopatabetto.composeapp.generated.resources.*

/**
 * NEW: EditPatientScreen with comprehensive patient editing functionality
 * Follows the same pattern as EditVisitScreen for consistency
 * 
 * Key Features:
 * - Takes only patientId as parameter (like EditVisitScreen)
 * - Comprehensive edit form for all patient data
 * - Single ViewModel (EditPatientViewModel) for personal data
 * - ClinicalHistoryViewModel for clinical history editing
 * - MVI pattern consistency
 * - Form validation and error handling
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPatientScreen(
    patientId: String,
    onBackClick: () -> Unit,
    onPatientUpdated: (Patient) -> Unit,
    modifier: Modifier = Modifier
) {
    // State for patient data loading
    var databasePatient by remember { mutableStateOf<DatabasePatient?>(null) }
    var uiPatient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // ViewModels
    val editPatientViewModel: EditPatientViewModel = remember { 
        ViewModelFactory.createEditPatientViewModel() 
    }
    val clinicalHistoryViewModel: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryViewModel = viewModel()
    
    // Unified coordinator for patient edit operations
    val coordinator: com.narde.gestionaleosteopatabetto.ui.viewmodels.PatientEditCoordinator = remember {
        com.narde.gestionaleosteopatabetto.ui.viewmodels.PatientEditCoordinator(
            editPatientViewModel,
            clinicalHistoryViewModel
        )
    }
    
    val editPatientUiState by editPatientViewModel.uiState.collectAsState()
    val clinicalHistoryUiState by clinicalHistoryViewModel.uiState.collectAsState()
    val coordinatorState by coordinator.state.collectAsState()
    
    val focusManager = LocalFocusManager.current
    
    // Load patient data when screen is first displayed
    LaunchedEffect(patientId) {
        if (isDatabaseSupported()) {
            val repository = DatabaseInitializer.getPatientRepository()
            if (repository != null) {
                try {
                    isLoading = true
                    errorMessage = null
                    
                    val dbPatient = repository.getPatientById(patientId)
                    if (dbPatient != null) {
                        databasePatient = dbPatient
                        val databaseUtils = createDatabaseUtils()
                        uiPatient = databaseUtils.toUIPatient(dbPatient)
                        
                        // Initialize both ViewModels with patient data
                        editPatientViewModel.initializeWithPatient(dbPatient)
                        clinicalHistoryViewModel.initializeWithPatient(dbPatient)
                    } else {
                        errorMessage = "Paziente non trovato"
                    }
                } catch (e: Exception) {
                    errorMessage = "Errore durante il caricamento del paziente: ${e.message}"
                } finally {
                    isLoading = false
                }
            } else {
                errorMessage = "Database non supportato"
                isLoading = false
            }
        } else {
            errorMessage = "Database non supportato"
            isLoading = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiPatient?.name ?: "Modifica Paziente",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                actions = {
                    // Cancel button
                    TextButton(
                        onClick = onBackClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(Res.string.cancel_edit),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(Res.string.cancel_edit),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Save button
                    TextButton(
                        onClick = {
                            // Use unified coordinator to save both personal data and clinical history
                            coordinator.savePatient {
                                // Both saves completed - reload fresh data from database
                                if (isDatabaseSupported()) {
                                    val repository = DatabaseInitializer.getPatientRepository()
                                    repository?.getPatientById(patientId)?.let { updatedDbPatient ->
                                        val databaseUtils = createDatabaseUtils()
                                        val updatedUiPatient = databaseUtils.toUIPatient(updatedDbPatient)
                                        // onPatientUpdated already handles navigation back
                                        onPatientUpdated(updatedUiPatient)
                                    }
                                }
                            }
                        },
                        enabled = !coordinatorState.isLoading,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        when {
                            coordinatorState.isLoading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 2.dp
                                )
                            }
                            coordinatorState.isSuccess -> {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = stringResource(Res.string.patient_updated_success),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            else -> {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = stringResource(Res.string.save_changes),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when {
                                coordinatorState.isLoading -> stringResource(Res.string.updating)
                                coordinatorState.isSuccess -> stringResource(Res.string.patient_updated_success)
                                else -> stringResource(Res.string.save_changes)
                            },
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator()
                            Text(
                                text = "Caricamento...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                
                errorMessage != null -> {
                    // Error state
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = errorMessage!!,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onBackClick) {
                                Text(stringResource(Res.string.back))
                            }
                        }
                    }
                }
                
                databasePatient != null -> {
                    // Success state - display edit forms
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Success message from unified coordinator
                        if (coordinatorState.isSuccess) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(Res.string.patient_update_success_message),
                                        modifier = Modifier.padding(16.dp),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }

                        // Error message from unified coordinator
                        if (coordinatorState.errorMessage.isNotEmpty()) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = coordinatorState.errorMessage,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                        // Show specific errors if available
                                        if (coordinatorState.personalDataError != null) {
                                            Text(
                                                text = "Personal data: ${coordinatorState.personalDataError}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onErrorContainer,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                        if (coordinatorState.clinicalHistoryError != null) {
                                            Text(
                                                text = "Clinical history: ${coordinatorState.clinicalHistoryError}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onErrorContainer,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Personal Data Edit Form
                        item {
                            PersonalDataEditForm(
                                uiState = editPatientUiState,
                                viewModel = editPatientViewModel,
                                focusManager = focusManager
                            )
                        }
                        
                        // Clinical History Edit Form
                        item {
                            com.narde.gestionaleosteopatabetto.ui.components.ClinicalHistoryEditForm(
                                uiState = clinicalHistoryUiState,
                                viewModel = clinicalHistoryViewModel,
                                focusManager = focusManager
                            )
                        }
                    }
                }
                
                else -> {
                    // No data state
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Nessun dato paziente disponibile",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Loading overlay - only visible during save operation
    if (coordinatorState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = false) { }, // Intercept all clicks to prevent interaction
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(32.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Salvataggio dati paziente...",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Attendere prego",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Personal Data Edit Form - extracted from PatientDetailsScreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonalDataEditForm(
    uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.EditPatientUiState,
    viewModel: EditPatientViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    // Personal Information Section
    PatientInfoSection(
        title = stringResource(Res.string.personal_information),
        content = {
            // Patient ID (read-only)
            PatientInfoRow(
                label = stringResource(Res.string.patient_id),
                value = uiState.patientId
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Name fields
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.firstName,
                    onValueChange = { viewModel.updateField(PatientField.FirstName, it) },
                    label = { Text(stringResource(Res.string.first_name)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.lastName,
                    onValueChange = { viewModel.updateField(PatientField.LastName, it) },
                    label = { Text(stringResource(Res.string.last_name)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Birth date and gender
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ItalianDateInput(
                    value = uiState.birthDate,
                    onValueChange = { viewModel.updateField(PatientField.BirthDate, it) },
                    label = stringResource(Res.string.birth_date),
                    placeholder = stringResource(Res.string.birth_date_placeholder),
                    showAge = true,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )

                // Gender dropdown
                var genderExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = !genderExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = if (uiState.gender == "M") stringResource(Res.string.male)
                        else if (uiState.gender == "F") stringResource(Res.string.female)
                        else "",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(Res.string.gender)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.male)) },
                            onClick = {
                                viewModel.updateField(PatientField.Gender, "M")
                                genderExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.female)) },
                            onClick = {
                                viewModel.updateField(PatientField.Gender, "F")
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            // Show age if calculated
            if (uiState.age != null) {
                Text(
                    text = stringResource(Res.string.patient_age, uiState.age.toString()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Place of birth and tax code
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.placeOfBirth,
                    onValueChange = { viewModel.updateField(PatientField.PlaceOfBirth, it) },
                    label = { Text(stringResource(Res.string.place_of_birth)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.taxCode,
                    onValueChange = { viewModel.updateField(PatientField.TaxCode, it) },
                    label = { Text(stringResource(Res.string.tax_code)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Phone and email
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = { viewModel.updateField(PatientField.Phone, it) },
                    label = { Text(stringResource(Res.string.phone_number)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.updateField(PatientField.Email, it) },
                    label = { Text(stringResource(Res.string.email)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Anthropometric Measurements Section
    PatientInfoSection(
        title = stringResource(Res.string.anthropometric_measurements),
        content = {
            // Height and Weight row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.height,
                    onValueChange = { viewModel.updateField(PatientField.Height, it) },
                    label = { Text(stringResource(Res.string.height_cm)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.weight,
                    onValueChange = { viewModel.updateField(PatientField.Weight, it) },
                    label = { Text(stringResource(Res.string.weight_kg)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // BMI field
            OutlinedTextField(
                value = uiState.bmi,
                onValueChange = { viewModel.updateField(PatientField.BMI, it) },
                label = { Text(stringResource(Res.string.bmi)) },
                placeholder = { Text(stringResource(Res.string.bmi_placeholder)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dominant side selection
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.dominant_side),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = uiState.dominantSide == "dx",
                            onClick = { viewModel.updateField(PatientField.DominantSide, "dx") }
                        )
                        Text(stringResource(Res.string.dominant_side_right))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = uiState.dominantSide == "sx",
                            onClick = { viewModel.updateField(PatientField.DominantSide, "sx") }
                        )
                        Text(stringResource(Res.string.dominant_side_left))
                    }
                }
            }
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Address Information Section
    PatientInfoSection(
        title = stringResource(Res.string.address_information),
        content = {
            OutlinedTextField(
                value = uiState.street,
                onValueChange = { viewModel.updateField(PatientField.Street, it) },
                label = { Text(stringResource(Res.string.street_address)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.city,
                    onValueChange = { viewModel.updateField(PatientField.City, it) },
                    label = { Text(stringResource(Res.string.city)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(2f)
                )
                OutlinedTextField(
                    value = uiState.zipCode,
                    onValueChange = { viewModel.updateField(PatientField.ZipCode, it) },
                    label = { Text(stringResource(Res.string.zip_code)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.province,
                    onValueChange = { viewModel.updateField(PatientField.Province, it) },
                    label = { Text(stringResource(Res.string.province)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.country,
                    onValueChange = { viewModel.updateField(PatientField.Country, it) },
                    label = { Text(stringResource(Res.string.country)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Privacy Consents Section
    PatientInfoSection(
        title = stringResource(Res.string.privacy_information),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.treatmentConsent,
                    onCheckedChange = { viewModel.updateConsent(ConsentType.Treatment, it) }
                )
                Text(
                    text = stringResource(Res.string.privacy_treatment_consent_statement),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.marketingConsent,
                    onCheckedChange = { viewModel.updateConsent(ConsentType.Marketing, it) }
                )
                Text(
                    text = stringResource(Res.string.privacy_marketing_consent_statement),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.thirdPartyConsent,
                    onCheckedChange = { viewModel.updateConsent(ConsentType.ThirdParty, it) }
                )
                Text(
                    text = stringResource(Res.string.privacy_third_party_consent_statement),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    )
}

/**
 * Reusable component for patient information sections
 */
@Composable
private fun PatientInfoSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

/**
 * Component for displaying patient information rows
 */
@Composable
private fun PatientInfoRow(
    label: String,
    value: String
) {
    if (value.isNotEmpty()) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
