package com.narde.gestionaleosteopatabetto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import com.narde.gestionaleosteopatabetto.utils.DateUtils
import com.narde.gestionaleosteopatabetto.ui.components.ItalianDateInput
import com.narde.gestionaleosteopatabetto.ui.viewmodels.EditPatientViewModel
import com.narde.gestionaleosteopatabetto.ui.viewmodels.PatientField
import com.narde.gestionaleosteopatabetto.ui.viewmodels.ConsentType
import org.jetbrains.compose.resources.stringResource
import gestionaleosteopatabetto.composeapp.generated.resources.Res
import gestionaleosteopatabetto.composeapp.generated.resources.*

/**
 * Patient details screen showing comprehensive patient information
 * Displays all available patient data in organized sections
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(
    patient: Patient,
    databasePatient: DatabasePatient?,
    onBackClick: () -> Unit,
    onPatientUpdated: () -> Unit = {},
    editViewModel: EditPatientViewModel = viewModel()
) {
    var isEditMode by remember { mutableStateOf(false) }
    val uiState by editViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    // State for managing current patient data that can be updated after save
    var currentDatabasePatient by remember(databasePatient) { mutableStateOf(databasePatient) }
    var currentUIPatient by remember(patient) { mutableStateOf(patient) }

    // Initialize the edit form when database patient is available
    LaunchedEffect(currentDatabasePatient) {
        currentDatabasePatient?.let {
            editViewModel.initializeWithPatient(it)
        }
    }

    // Function to reload patient data after successful save
    val reloadPatientData = remember {
        {
            if (isDatabaseSupported()) {
                val repository = DatabaseInitializer.getPatientRepository()
                repository?.let { repo ->
                    try {
                        // Use the current database patient ID if available, fallback to UI patient ID
                        val patientId = currentDatabasePatient?.idPaziente ?: patient.id
                        val updatedDbPatient = repo.getPatientById(patientId)
                        if (updatedDbPatient != null) {
                            currentDatabasePatient = updatedDbPatient
                            // Convert to UI patient to update the display
                            val databaseUtils = createDatabaseUtils()
                            currentUIPatient = databaseUtils.toUIPatient(updatedDbPatient)
                            println("✅ Patient data reloaded successfully: ${updatedDbPatient.datiPersonali?.nome} ${updatedDbPatient.datiPersonali?.cognome}")
                        }
                    } catch (e: Exception) {
                        println("Error reloading patient data: ${e.message}")
                    }
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) stringResource(Res.string.edit_patient) else currentUIPatient.name
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
                    if (currentDatabasePatient != null) {
                        if (isEditMode) {
                            // Cancel button with enhanced contrast for better readability
                            TextButton(
                                onClick = {
                                    isEditMode = false
                                    // Re-initialize with current data
                                    currentDatabasePatient?.let {
                                        editViewModel.initializeWithPatient(it)
                                    }
                                },
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
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                                )
                            }
                            // Save button with enhanced contrast for better readability
                            TextButton(
                                onClick = {
                                    editViewModel.updatePatient {
                                        // Reload patient data from database first
                                        reloadPatientData()
                                        isEditMode = false
                                        onPatientUpdated()
                                    }
                                },
                                enabled = !uiState.isUpdating && !uiState.isUpdateSuccessful,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                when {
                                    uiState.isUpdating -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = MaterialTheme.colorScheme.primary,
                                            strokeWidth = 2.dp
                                        )
                                    }

                                    uiState.isUpdateSuccessful -> {
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
                                        uiState.isUpdating -> stringResource(Res.string.updating)
                                        uiState.isUpdateSuccessful -> stringResource(Res.string.patient_updated_success)
                                        else -> stringResource(Res.string.save_changes)
                                    },
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                                )
                            }
                        } else {
                            // Edit button
                            IconButton(
                                onClick = { isEditMode = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = stringResource(Res.string.edit_patient)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Success message
            if (uiState.isUpdateSuccessful) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.patient_update_success_message),
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Error message
            if (uiState.errorMessage.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = uiState.errorMessage,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            if (isEditMode && currentDatabasePatient != null) {
                EditPatientForm(
                    uiState = uiState,
                    viewModel = editViewModel,
                    focusManager = focusManager
                )
            } else {
                ViewPatientInfo(
                    patient = currentUIPatient,
                    databasePatient = currentDatabasePatient
                )
            }
        }
    }
}

/**
 * View-only patient information - restored to original simple format
 */
@Composable
private fun ViewPatientInfo(
    patient: Patient,
    databasePatient: DatabasePatient?
) {
    // Personal Information Section
    PatientInfoSection(
        title = stringResource(Res.string.personal_information),
        content = {
            PatientInfoRow(
                label = stringResource(Res.string.patient_id),
                value = patient.id
            )
            PatientInfoRow(
                label = stringResource(Res.string.patient_name),
                value = patient.name
            )

            // Calculate and display age properly
            databasePatient?.dati_personali?.data_nascita?.takeIf { it.isNotEmpty() }
                ?.let { birthDate ->
                    val calculatedAge = calculateAgeFromBirthDate(birthDate)
                    if (calculatedAge > 0) {
                        PatientInfoRow(
                            label = stringResource(Res.string.patient_age),
                            value = calculatedAge.toString()
                        )
                    }
                }

            // Additional fields from database
            databasePatient?.let { dbPatient ->
                if (dbPatient.dati_personali?.data_nascita?.isNotEmpty() == true) {
                    PatientInfoRow(
                        label = stringResource(Res.string.birth_date_label),
                        // Convert ISO format from database to Italian format for display
                        value = DateUtils.convertIsoToItalianFormat(
                            dbPatient.dati_personali?.data_nascita ?: ""
                        )
                    )
                }
                dbPatient.dati_personali?.sesso?.takeIf { it.isNotEmpty() }?.let {
                    PatientInfoRow(
                        label = stringResource(Res.string.gender),
                        value = if (it == "M") stringResource(Res.string.gender_male) else stringResource(
                            Res.string.gender_female
                        )
                    )
                }
                dbPatient.dati_personali?.luogo_nascita?.takeIf { it.isNotEmpty() }?.let {
                    PatientInfoRow(
                        label = stringResource(Res.string.place_of_birth_label),
                        value = it
                    )
                }
                dbPatient.dati_personali?.codice_fiscale?.takeIf { it.isNotEmpty() }?.let {
                    PatientInfoRow(
                        label = stringResource(Res.string.tax_code_label),
                        value = it
                    )
                }
            }
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Anthropometric Measurements Section
    databasePatient?.dati_personali?.let { personalData ->
        if (personalData.altezza > 0 || personalData.peso > 0.0 || personalData.latoDominante.isNotEmpty()) {
            PatientInfoSection(
                title = stringResource(Res.string.anthropometric_measurements),
                content = {
                    if (personalData.altezza > 0) {
                        PatientInfoRow(
                            label = stringResource(Res.string.height_cm),
                            value = "${personalData.altezza} cm"
                        )
                    }
                    if (personalData.peso > 0.0) {
                        PatientInfoRow(
                            label = stringResource(Res.string.weight_kg),
                            value = "${personalData.peso} kg"
                        )
                    }
                    if (personalData.latoDominante.isNotEmpty()) {
                        val dominantSideText = when (personalData.latoDominante) {
                            "dx" -> stringResource(Res.string.dominant_side_right)
                            "sx" -> stringResource(Res.string.dominant_side_left)
                            else -> personalData.latoDominante
                        }
                        PatientInfoRow(
                            label = stringResource(Res.string.dominant_side),
                            value = dominantSideText
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Contact Information Section
    PatientInfoSection(
        title = stringResource(Res.string.contact_information),
        content = {
            PatientInfoRow(
                label = stringResource(Res.string.phone_number),
                value = patient.phone
            )
            PatientInfoRow(
                label = stringResource(Res.string.email),
                value = patient.email
            )
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Address Information Section
    databasePatient?.indirizzo?.let { address ->
        if (address.via.isNotEmpty() || address.citta.isNotEmpty() || address.cap.isNotEmpty() || address.provincia.isNotEmpty()
        ) {
            PatientInfoSection(
                title = stringResource(Res.string.address_information),
                content = {
                    address.via.takeIf { it.isNotEmpty() }?.let {
                        PatientInfoRow(
                            label = stringResource(Res.string.address_street),
                            value = it
                        )
                    }
                    address.citta.takeIf { it.isNotEmpty() }?.let {
                        PatientInfoRow(
                            label = stringResource(Res.string.address_city),
                            value = it
                        )
                    }
                    address.cap.takeIf { it.isNotEmpty() }?.let {
                        PatientInfoRow(
                            label = stringResource(Res.string.address_zip),
                            value = it
                        )
                    }
                    address.provincia.takeIf { it.isNotEmpty() }?.let {
                        PatientInfoRow(
                            label = stringResource(Res.string.address_province),
                            value = it
                        )
                    }
                    address.nazione.takeIf { it.isNotEmpty() }?.let {
                        PatientInfoRow(
                            label = stringResource(Res.string.address_country),
                            value = it
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Privacy Information Section with disabled checkboxes
    databasePatient?.privacy?.let { privacy ->
        PatientInfoSection(
            title = stringResource(Res.string.privacy_information),
            content = {
                // Privacy consent checkboxes (disabled, read-only)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = privacy.consenso_trattamento,
                        onCheckedChange = { },
                        enabled = false
                    )
                    Text(
                        text = stringResource(Res.string.privacy_treatment_consent),
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = privacy.consenso_marketing,
                        onCheckedChange = { },
                        enabled = false
                    )
                    Text(
                        text = stringResource(Res.string.privacy_marketing_consent),
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = privacy.consenso_terze_parti,
                        onCheckedChange = { },
                        enabled = false
                    )
                    Text(
                        text = stringResource(Res.string.privacy_third_party_consent),
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Consent date if available
                if (privacy.data_consenso.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    PatientInfoRow(
                        label = stringResource(Res.string.privacy_consent_date),
                        value = privacy.data_consenso
                    )
                }

                // Privacy notes if available
                privacy.note_privacy.takeIf { it.isNotEmpty() }?.let {
                    PatientInfoRow(
                        label = stringResource(Res.string.privacy_notes),
                        value = it
                    )
                }
            }
        )
    }
}

/**
 * Edit form for patient information
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPatientForm(
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

/**
 * Helper function to calculate age from birth date string in Italian DD/MM/AAAA format
 */
private fun calculateAgeFromBirthDate(birthDateString: String): Int {
    return DateUtils.calculateAgeFromItalianDate(birthDateString) ?: 0
}