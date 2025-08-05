package com.narde.gestionaleosteopatabetto.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.stringResource
import gestionaleosteopatabetto.composeapp.generated.resources.Res
import gestionaleosteopatabetto.composeapp.generated.resources.*
import com.narde.gestionaleosteopatabetto.ui.viewmodels.AddPatientViewModel
import com.narde.gestionaleosteopatabetto.ui.viewmodels.PatientField
import com.narde.gestionaleosteopatabetto.ui.components.ItalianDateInput
import com.narde.gestionaleosteopatabetto.ui.viewmodels.ConsentType

/**
 * Helper function to handle TAB key navigation for desktop
 * Moves focus to next field when TAB is pressed, previous field when Shift+TAB is pressed
 */
@Composable
private fun Modifier.handleTabKeyNavigation(): Modifier {
    val focusManager = LocalFocusManager.current
    return this.onPreviewKeyEvent { keyEvent ->
        when {
            // Handle TAB key press (move to next field)
            keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown && !keyEvent.isShiftPressed -> {
                focusManager.moveFocus(FocusDirection.Next)
                true // Consume the event
            }
            // Handle Shift+TAB key press (move to previous field)
            keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown && keyEvent.isShiftPressed -> {
                focusManager.moveFocus(FocusDirection.Previous)
                true // Consume the event
            }
            else -> false // Don't consume other events
        }
    }
}

/**
 * ✅ REFACTORED: Clean separation of concerns with complete functionality
 * - UI only handles presentation and user interactions
 * - Business logic is in ViewModel
 * - State management is centralized
 * - All original sections restored (Address, Parents, TAB navigation)
 * - Easy to test business logic independently
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPatientScreen(
    onBackClick: () -> Unit,
    onPatientSaved: () -> Unit,
    viewModel: AddPatientViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.add_new_patient)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // Personal Information Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.personal_information),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
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
                            modifier = Modifier
                                .weight(1f)
                                .handleTabKeyNavigation()
                        )
                        OutlinedTextField(
                            value = uiState.lastName,
                            onValueChange = { viewModel.updateField(PatientField.LastName, it) },
                            label = { Text(stringResource(Res.string.last_name)) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Next) }
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .handleTabKeyNavigation()
                        )
                    }
                    
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
                        
                        // Gender selection
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(Res.string.gender),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = uiState.gender == "M",
                                        onClick = { viewModel.updateField(PatientField.Gender, "M") }
                                    )
                                    Text(stringResource(Res.string.male))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = uiState.gender == "F",
                                        onClick = { viewModel.updateField(PatientField.Gender, "F") }
                                    )
                                    Text(stringResource(Res.string.female))
                                }
                            }
                        }
                    }
                    
                    OutlinedTextField(
                        value = uiState.placeOfBirth,
                        onValueChange = { viewModel.updateField(PatientField.PlaceOfBirth, it) },
                        label = { Text(stringResource(Res.string.place_of_birth)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .handleTabKeyNavigation()
                    )
                    
                    OutlinedTextField(
                        value = uiState.taxCode,
                        onValueChange = { viewModel.updateField(PatientField.TaxCode, it) },
                        label = { Text(stringResource(Res.string.tax_code)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .handleTabKeyNavigation()
                    )
                    
                    // Contact information
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
                            modifier = Modifier
                                .weight(1f)
                                .handleTabKeyNavigation()
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
                            modifier = Modifier
                                .weight(1f)
                                .handleTabKeyNavigation()
                        )
                    }
                    
                    // Show age if calculated
                    if (uiState.age != null) {
                        Text(
                            text = stringResource(Res.string.patient_age, uiState.age.toString()),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        if (uiState.isMinor) {
                            Text(
                                text = stringResource(Res.string.patient_is_minor),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            // Address Information Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.address_information),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    OutlinedTextField(
                        value = uiState.street,
                        onValueChange = { viewModel.updateField(PatientField.Street, it) },
                        label = { Text(stringResource(Res.string.street_address)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .handleTabKeyNavigation()
                    )
                    
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
                            modifier = Modifier
                                .weight(2f)
                                .handleTabKeyNavigation()
                        )
                        OutlinedTextField(
                            value = uiState.zipCode,
                            onValueChange = { viewModel.updateField(PatientField.ZipCode, it) },
                            label = { Text(stringResource(Res.string.zip_code)) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Next) }
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .handleTabKeyNavigation()
                        )
                    }
                    
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
                            modifier = Modifier
                                .weight(1f)
                                .handleTabKeyNavigation()
                        )
                        OutlinedTextField(
                            value = uiState.country,
                            onValueChange = { viewModel.updateField(PatientField.Country, it) },
                            label = { Text(stringResource(Res.string.country)) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() }
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .handleTabKeyNavigation()
                        )
                    }
                }
            }
            
            // Parent Information Section (only for minors - age < 18)
            if (uiState.isMinor) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Header with expand/collapse button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .handleTabKeyNavigation(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(Res.string.parent_information_minor, uiState.age?.toString() ?: "?"),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(
                                onClick = { viewModel.toggleParentSection() }
                            ) {
                                Icon(
                                    imageVector = if (uiState.isParentSectionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (uiState.isParentSectionExpanded) "Collapse parent information" else "Expand parent information"
                                )
                            }
                        }
                        
                        // Expandable content
                        AnimatedVisibility(
                            visible = uiState.isParentSectionExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Father information
                                Text(
                                    text = stringResource(Res.string.father_info),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = uiState.fatherFirstName,
                                        onValueChange = { viewModel.updateField(PatientField.FatherFirstName, it) },
                                        label = { Text(stringResource(Res.string.father_first_name)) },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                        keyboardActions = KeyboardActions(
                                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .handleTabKeyNavigation()
                                    )
                                    OutlinedTextField(
                                        value = uiState.fatherLastName,
                                        onValueChange = { viewModel.updateField(PatientField.FatherLastName, it) },
                                        label = { Text(stringResource(Res.string.father_last_name)) },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                        keyboardActions = KeyboardActions(
                                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .handleTabKeyNavigation()
                                    )
                                }
                                
                                // Divider between father and mother
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                )
                                
                                // Mother information
                                Text(
                                    text = stringResource(Res.string.mother_info),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = uiState.motherFirstName,
                                        onValueChange = { viewModel.updateField(PatientField.MotherFirstName, it) },
                                        label = { Text(stringResource(Res.string.mother_first_name)) },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                        keyboardActions = KeyboardActions(
                                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .handleTabKeyNavigation()
                                    )
                                    OutlinedTextField(
                                        value = uiState.motherLastName,
                                        onValueChange = { viewModel.updateField(PatientField.MotherLastName, it) },
                                        label = { Text(stringResource(Res.string.mother_last_name)) },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                        keyboardActions = KeyboardActions(
                                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .handleTabKeyNavigation()
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Privacy Consent Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.privacy_information),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.handleTabKeyNavigation()
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
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.handleTabKeyNavigation()
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
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.handleTabKeyNavigation()
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
            }
            
            // Error message
            if (uiState.errorMessage.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.errorMessage,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            // Save button    
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .handleTabKeyNavigation(),
                onClick = { viewModel.savePatient(onPatientSaved) },
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (uiState.isSaving) stringResource(Res.string.saving) else stringResource(Res.string.save_patient))
            }
            
            // Bottom padding for FAB
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}