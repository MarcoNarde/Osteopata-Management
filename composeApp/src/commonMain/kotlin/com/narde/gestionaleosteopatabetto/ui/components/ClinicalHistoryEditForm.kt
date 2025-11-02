package com.narde.gestionaleosteopatabetto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryViewModel
import com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryField
import com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryBooleanField
import com.narde.gestionaleosteopatabetto.ui.viewmodels.DiagnosticTestUiState
import com.narde.gestionaleosteopatabetto.ui.viewmodels.InterventionUiState
import com.narde.gestionaleosteopatabetto.ui.viewmodels.PharmacologicalTherapyUiState
import org.jetbrains.compose.resources.stringResource
import gestionaleosteopatabetto.composeapp.generated.resources.Res
import gestionaleosteopatabetto.composeapp.generated.resources.*

/**
 * Clinical History Edit Form
 * Comprehensive form for editing all clinical history data
 */
@Composable
fun ClinicalHistoryEditForm(
    uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryUiState,
    viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Chronic Conditions Section
        ClinicalHistorySection(
            title = stringResource(Res.string.chronic_conditions),
            content = {
                ChronicConditionsEditContent(uiState, viewModel, focusManager)
            }
        )
        
        // Lifestyle Factors Section
        ClinicalHistorySection(
            title = stringResource(Res.string.lifestyle_factors),
            content = {
                LifestyleFactorsEditContent(uiState, viewModel, focusManager)
            }
        )
        
        // Pharmacological Therapies Section
        ClinicalHistorySection(
            title = stringResource(Res.string.pharmacological_therapies),
            content = {
                PharmacologicalTherapiesEditContent(uiState, viewModel, focusManager)
            }
        )
        
        // Interventions & Traumas Section
        ClinicalHistorySection(
            title = stringResource(Res.string.interventions_traumas),
            content = {
                InterventionsTraumasEditContent(uiState, viewModel, focusManager)
            }
        )
        
        // Diagnostic Tests Section
        ClinicalHistorySection(
            title = stringResource(Res.string.diagnostic_tests),
            content = {
                DiagnosticTestsEditContent(uiState, viewModel, focusManager)
            }
        )
        
        // Pediatric History Section
        ClinicalHistorySection(
            title = stringResource(Res.string.pediatric_history),
            content = {
                PediatricHistoryEditContent(uiState, viewModel, focusManager)
            }
        )
    }
}

/**
 * Chronic Conditions Edit Content
 */
@Composable
private fun ChronicConditionsEditContent(
    uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryUiState,
    viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Drug Allergies
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.drug_allergies),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = uiState.hasDrugAllergies,
                onCheckedChange = { viewModel.updateBooleanField(ClinicalHistoryBooleanField.HasDrugAllergies, it) }
            )
        }
        
        if (uiState.hasDrugAllergies) {
            OutlinedTextField(
                value = uiState.drugAllergiesList,
                onValueChange = { viewModel.updateField(ClinicalHistoryField.DrugAllergiesList, it) },
                label = { Text(stringResource(Res.string.drug_allergies)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
        }
        
        // Diabetes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.diabetes),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = uiState.hasDiabetes,
                onCheckedChange = { viewModel.updateBooleanField(ClinicalHistoryBooleanField.HasDiabetes, it) }
            )
        }
        
        if (uiState.hasDiabetes) {
            OutlinedTextField(
                value = uiState.diabetesType,
                onValueChange = { viewModel.updateField(ClinicalHistoryField.DiabetesType, it) },
                label = { Text(stringResource(Res.string.type)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
        }
        
        // Other conditions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.hyperthyroidism),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = uiState.hasHyperthyroidism,
                onCheckedChange = { viewModel.updateBooleanField(ClinicalHistoryBooleanField.HasHyperthyroidism, it) }
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.heart_disease),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = uiState.hasHeartDisease,
                onCheckedChange = { viewModel.updateBooleanField(ClinicalHistoryBooleanField.HasHeartDisease, it) }
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.arterial_hypertension),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = uiState.hasHypertension,
                onCheckedChange = { viewModel.updateBooleanField(ClinicalHistoryBooleanField.HasHypertension, it) }
            )
        }
    }
}

/**
 * Lifestyle Factors Edit Content
 */
@Composable
private fun LifestyleFactorsEditContent(
    uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryUiState,
    viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Smoking habits
        OutlinedTextField(
            value = uiState.smokingStatus,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.SmokingStatus, it) },
            label = { Text(stringResource(Res.string.smoking_habits)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        OutlinedTextField(
            value = uiState.cigarettesPerDay,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.CigarettesPerDay, it) },
            label = { Text(stringResource(Res.string.cigarettes_per_day)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        OutlinedTextField(
            value = uiState.yearsSmoking,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.YearsSmoking, it) },
            label = { Text(stringResource(Res.string.years_smoking)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        // Work information
        OutlinedTextField(
            value = uiState.workType,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.WorkType, it) },
            label = { Text(stringResource(Res.string.work_type)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        OutlinedTextField(
            value = uiState.profession,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.Profession, it) },
            label = { Text(stringResource(Res.string.profession)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        OutlinedTextField(
            value = uiState.workHoursPerDay,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.WorkHoursPerDay, it) },
            label = { Text(stringResource(Res.string.work_hours_per_day)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        // Physical activity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.physical_activity),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = uiState.hasPhysicalActivity,
                onCheckedChange = { viewModel.updateBooleanField(ClinicalHistoryBooleanField.HasPhysicalActivity, it) }
            )
        }
        
        if (uiState.hasPhysicalActivity) {
            OutlinedTextField(
                value = uiState.sportsList,
                onValueChange = { viewModel.updateField(ClinicalHistoryField.SportsList, it) },
                label = { Text(stringResource(Res.string.sports)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            
            OutlinedTextField(
                value = uiState.activityFrequency,
                onValueChange = { viewModel.updateField(ClinicalHistoryField.ActivityFrequency, it) },
                label = { Text(stringResource(Res.string.frequency)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            
            OutlinedTextField(
                value = uiState.activityIntensity,
                onValueChange = { viewModel.updateField(ClinicalHistoryField.ActivityIntensity, it) },
                label = { Text(stringResource(Res.string.intensity)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
        }
    }
}

/**
 * Pediatric History Edit Content
 */
@Composable
private fun PediatricHistoryEditContent(
    uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryUiState,
    viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Pregnancy
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.complications),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = uiState.pregnancyComplications,
                onCheckedChange = { viewModel.updateBooleanField(ClinicalHistoryBooleanField.PregnancyComplications, it) }
            )
        }
        
        OutlinedTextField(
            value = uiState.pregnancyNotes,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.PregnancyNotes, it) },
            label = { Text(stringResource(Res.string.pregnancy)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        // Birth
        OutlinedTextField(
            value = uiState.birthType,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.BirthType, it) },
            label = { Text(stringResource(Res.string.birth_type)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.complications),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = uiState.birthComplications,
                onCheckedChange = { viewModel.updateBooleanField(ClinicalHistoryBooleanField.BirthComplications, it) }
            )
        }
        
        OutlinedTextField(
            value = uiState.birthWeight,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.BirthWeight, it) },
            label = { Text(stringResource(Res.string.birth_weight_grams)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        OutlinedTextField(
            value = uiState.apgarScore,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.ApgarScore, it) },
            label = { Text(stringResource(Res.string.apgar_score_5min)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        OutlinedTextField(
            value = uiState.birthNotes,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.BirthNotes, it) },
            label = { Text(stringResource(Res.string.birth)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        // Development
        OutlinedTextField(
            value = uiState.firstStepsMonths,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.FirstStepsMonths, it) },
            label = { Text(stringResource(Res.string.first_steps_months)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        OutlinedTextField(
            value = uiState.firstWordsMonths,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.FirstWordsMonths, it) },
            label = { Text(stringResource(Res.string.first_words_months)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.development_problems),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = uiState.developmentProblems,
                onCheckedChange = { viewModel.updateBooleanField(ClinicalHistoryBooleanField.DevelopmentProblems, it) }
            )
        }
        
        OutlinedTextField(
            value = uiState.developmentNotes,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.DevelopmentNotes, it) },
            label = { Text(stringResource(Res.string.development)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        
        OutlinedTextField(
            value = uiState.pediatricGeneralNotes,
            onValueChange = { viewModel.updateField(ClinicalHistoryField.PediatricGeneralNotes, it) },
            label = { Text(stringResource(Res.string.general_notes)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
    }
}

/**
 * Pharmacological Therapies Edit Content
 */
@Composable
private fun PharmacologicalTherapiesEditContent(
    uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryUiState,
    viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Display list of medications
        if (uiState.pharmacologicalTherapies.isEmpty()) {
            Text(
                text = stringResource(Res.string.no_medications),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            uiState.pharmacologicalTherapies.forEachIndexed { index, therapy ->
                PharmacologicalTherapyCard(
                    therapy = therapy,
                    index = index,
                    viewModel = viewModel,
                    focusManager = focusManager,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Add new medication button
        Button(
            onClick = { viewModel.addPharmacologicalTherapy() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(Res.string.add_new_medication)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(Res.string.add_new_medication))
        }
    }
}

/**
 * Pharmacological Therapy Card Component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PharmacologicalTherapyCard(
    therapy: PharmacologicalTherapyUiState,
    index: Int,
    viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember(therapy.isExpanded) { mutableStateOf(therapy.isExpanded) }
    
    Card(
        modifier = modifier,
        onClick = { 
            isExpanded = !isExpanded
            viewModel.togglePharmacologicalTherapyExpanded(index)
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Collapsed state header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Medication name
                    if (therapy.medication.isNotEmpty()) {
                        Text(
                            text = therapy.medication,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    // Dosage and frequency preview
                    if (therapy.dosage.isNotEmpty() || therapy.frequency.isNotEmpty()) {
                        Text(
                            text = "${therapy.dosage} ${if (therapy.dosage.isNotEmpty() && therapy.frequency.isNotEmpty()) "• " else ""}${therapy.frequency}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    // Start date
                    if (therapy.startDate.isNotEmpty()) {
                        Text(
                            text = stringResource(Res.string.start_date) + ": ${therapy.startDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // Status indicator
                    if (therapy.isOngoing) {
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = stringResource(Res.string.ongoing),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    } else if (therapy.medication.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.medication_details),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
                
                // Expand/Collapse icon
                Icon(
                    imageVector = if (isExpanded) 
                        Icons.Default.KeyboardArrowUp 
                    else 
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }
            
            // Expanded state - full form
            if (isExpanded) {
                VerticalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                // Medication name field
                OutlinedTextField(
                    value = therapy.medication,
                    onValueChange = { viewModel.updatePharmacologicalTherapyField(index, "medication", it) },
                    label = { Text(stringResource(Res.string.medication)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                
                // Dosage and frequency row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = therapy.dosage,
                        onValueChange = { viewModel.updatePharmacologicalTherapyField(index, "dosage", it) },
                        label = { Text(stringResource(Res.string.dosage)) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
                    OutlinedTextField(
                        value = therapy.frequency,
                        onValueChange = { viewModel.updatePharmacologicalTherapyField(index, "frequency", it) },
                        label = { Text(stringResource(Res.string.frequency)) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
                }
                
                // Start date field
                ItalianDateInput(
                    value = therapy.startDate,
                    onValueChange = { viewModel.updatePharmacologicalTherapyField(index, "startDate", it) },
                    label = stringResource(Res.string.start_date),
                    placeholder = stringResource(Res.string.medication_date_placeholder),
                    showAge = false,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Ongoing checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = therapy.isOngoing,
                        onCheckedChange = { 
                            viewModel.togglePharmacologicalTherapyOngoing(index)
                        }
                    )
                    Text(
                        text = stringResource(Res.string.ongoing),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                // End date field (shown only if not ongoing)
                if (!therapy.isOngoing) {
                    ItalianDateInput(
                        value = therapy.endDate,
                        onValueChange = { viewModel.updatePharmacologicalTherapyField(index, "endDate", it) },
                        label = stringResource(Res.string.end_date),
                        placeholder = stringResource(Res.string.medication_date_placeholder),
                        showAge = false,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Indication field (multiline)
                OutlinedTextField(
                    value = therapy.indication,
                    onValueChange = { viewModel.updatePharmacologicalTherapyField(index, "indication", it) },
                    label = { Text(stringResource(Res.string.indication)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
                
                // Delete button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { viewModel.deletePharmacologicalTherapy(index) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(Res.string.delete_medication),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(Res.string.delete_medication),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

/**
 * Interventions & Traumas Edit Content
 */
@Composable
private fun InterventionsTraumasEditContent(
    uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryUiState,
    viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Display list of interventions
        if (uiState.interventions.isEmpty()) {
            Text(
                text = stringResource(Res.string.no_interventions),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            uiState.interventions.forEachIndexed { index, intervention ->
                InterventionCard(
                    intervention = intervention,
                    index = index,
                    viewModel = viewModel,
                    focusManager = focusManager,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Add new intervention button
        Button(
            onClick = { viewModel.addIntervention() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Add,
                contentDescription = stringResource(Res.string.add_new_intervention)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(Res.string.add_new_intervention))
        }
    }
}

/**
 * Intervention Card Component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InterventionCard(
    intervention: InterventionUiState,
    index: Int,
    viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember(intervention.isExpanded) { mutableStateOf(intervention.isExpanded) }
    
    Card(
        modifier = modifier,
        onClick = { 
            isExpanded = !isExpanded
            viewModel.toggleInterventionExpanded(index)
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Collapsed state header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Type badge
                    if (intervention.type.isNotEmpty()) {
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = getInterventionTypeLabel(intervention.type),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    }
                    // Date
                    if (intervention.date.isNotEmpty()) {
                        Text(
                            text = stringResource(Res.string.intervention_date) + ": ${intervention.date}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    // Description preview
                    if (intervention.description.isNotEmpty()) {
                        Text(
                            text = intervention.description.take(50) + if (intervention.description.length > 50) "..." else "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.intervention_details),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
                
                // Expand/Collapse icon
                Icon(
                    imageVector = if (isExpanded) 
                        Icons.Default.KeyboardArrowUp 
                    else 
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }
            
            // Expanded state - full form
            if (isExpanded) {
                VerticalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                // Date field
                ItalianDateInput(
                    value = intervention.date,
                    onValueChange = { viewModel.updateInterventionField(index, "date", it) },
                    label = stringResource(Res.string.intervention_date),
                    placeholder = stringResource(Res.string.intervention_date_placeholder),
                    showAge = false,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Type dropdown
                var typeExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = getInterventionTypeLabel(intervention.type),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(Res.string.intervention_type)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.trauma)) },
                            onClick = {
                                viewModel.updateInterventionField(index, "type", "trauma")
                                typeExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.surgical_intervention)) },
                            onClick = {
                                viewModel.updateInterventionField(index, "type", "intervento_chirurgico")
                                typeExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.other)) },
                            onClick = {
                                viewModel.updateInterventionField(index, "type", "altro")
                                typeExpanded = false
                            }
                        )
                    }
                }
                
                // Description field (multiline)
                OutlinedTextField(
                    value = intervention.description,
                    onValueChange = { viewModel.updateInterventionField(index, "description", it) },
                    label = { Text(stringResource(Res.string.description)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                
                // Treatment field (multiline)
                OutlinedTextField(
                    value = intervention.treatment,
                    onValueChange = { viewModel.updateInterventionField(index, "treatment", it) },
                    label = { Text(stringResource(Res.string.treatment)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                
                // Outcome dropdown
                var outcomeExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = outcomeExpanded,
                    onExpandedChange = { outcomeExpanded = !outcomeExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = getOutcomeLabel(intervention.outcome),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(Res.string.outcome)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = outcomeExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = outcomeExpanded,
                        onDismissRequest = { outcomeExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.complete_recovery)) },
                            onClick = {
                                viewModel.updateInterventionField(index, "outcome", "guarigione_completa")
                                outcomeExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.sequelae)) },
                            onClick = {
                                viewModel.updateInterventionField(index, "outcome", "sequeli")
                                outcomeExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.other)) },
                            onClick = {
                                viewModel.updateInterventionField(index, "outcome", "altro")
                                outcomeExpanded = false
                            }
                        )
                    }
                }
                
                // Delete button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { viewModel.deleteIntervention(index) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                            contentDescription = stringResource(Res.string.delete_intervention),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(Res.string.delete_intervention),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

/**
 * Helper function to get intervention type label
 */
private fun getInterventionTypeLabel(type: String): String {
    return when (type) {
        "trauma" -> "Trauma"
        "intervento_chirurgico" -> "Intervento Chirurgico"
        "altro" -> "Altro"
        else -> ""
    }
}

/**
 * Helper function to get outcome label
 */
private fun getOutcomeLabel(outcome: String): String {
    return when (outcome) {
        "guarigione_completa" -> "Guarigione Completa"
        "sequeli" -> "Sequele"
        "altro" -> "Altro"
        else -> ""
    }
}

/**
 * Diagnostic Tests Edit Content
 */
@Composable
private fun DiagnosticTestsEditContent(
    uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryUiState,
    viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Display list of diagnostic tests
        if (uiState.diagnosticTests.isEmpty()) {
            Text(
                text = stringResource(Res.string.no_tests),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            uiState.diagnosticTests.forEachIndexed { index, test ->
                DiagnosticTestCard(
                    test = test,
                    index = index,
                    viewModel = viewModel,
                    focusManager = focusManager,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Add new diagnostic test button
        Button(
            onClick = { viewModel.addDiagnosticTest() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(Res.string.add_new_diagnostic_test)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(Res.string.add_new_diagnostic_test))
        }
    }
}

/**
 * Diagnostic Test Card Component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiagnosticTestCard(
    test: DiagnosticTestUiState,
    index: Int,
    viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember(test.isExpanded) { mutableStateOf(test.isExpanded) }
    
    Card(
        modifier = modifier,
        onClick = { 
            isExpanded = !isExpanded
            viewModel.toggleDiagnosticTestExpanded(index)
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Collapsed state header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Type badge
                    if (test.type.isNotEmpty()) {
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = getDiagnosticTestTypeLabel(test.type),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    }
                    // Date
                    if (test.date.isNotEmpty()) {
                        Text(
                            text = stringResource(Res.string.diagnostic_test_date) + ": ${test.date}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    // Body area preview
                    if (test.bodyArea.isNotEmpty()) {
                        Text(
                            text = stringResource(Res.string.body_area) + ": ${test.bodyArea}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.diagnostic_test_details),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
                
                // Expand/Collapse icon
                Icon(
                    imageVector = if (isExpanded) 
                        Icons.Default.KeyboardArrowUp 
                    else 
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }
            
            // Expanded state - full form
            if (isExpanded) {
                VerticalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                // Date field
                ItalianDateInput(
                    value = test.date,
                    onValueChange = { viewModel.updateDiagnosticTestField(index, "date", it) },
                    label = stringResource(Res.string.diagnostic_test_date),
                    placeholder = stringResource(Res.string.diagnostic_test_date_placeholder),
                    showAge = false,
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Type dropdown
                var typeExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = getDiagnosticTestTypeLabel(test.type),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(Res.string.test_type)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.mri)) },
                            onClick = {
                                viewModel.updateDiagnosticTestField(index, "type", "RMN")
                                typeExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.ct_scan)) },
                            onClick = {
                                viewModel.updateDiagnosticTestField(index, "type", "TAC")
                                typeExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.x_ray)) },
                            onClick = {
                                viewModel.updateDiagnosticTestField(index, "type", "RX")
                                typeExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.ultrasound)) },
                            onClick = {
                                viewModel.updateDiagnosticTestField(index, "type", "ecografia")
                                typeExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.blood_tests)) },
                            onClick = {
                                viewModel.updateDiagnosticTestField(index, "type", "esami_ematochimici")
                                typeExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.other)) },
                            onClick = {
                                viewModel.updateDiagnosticTestField(index, "type", "altro")
                                typeExpanded = false
                            }
                        )
                    }
                }
                
                // Body area field
                OutlinedTextField(
                    value = test.bodyArea,
                    onValueChange = { viewModel.updateDiagnosticTestField(index, "bodyArea", it) },
                    label = { Text(stringResource(Res.string.body_area)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                
                // Results field (multiline)
                OutlinedTextField(
                    value = test.results,
                    onValueChange = { viewModel.updateDiagnosticTestField(index, "results", it) },
                    label = { Text(stringResource(Res.string.results)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                
                // Facility field
                OutlinedTextField(
                    value = test.facility,
                    onValueChange = { viewModel.updateDiagnosticTestField(index, "facility", it) },
                    label = { Text(stringResource(Res.string.facility)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                
                // Delete button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { viewModel.deleteDiagnosticTest(index) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(Res.string.delete_diagnostic_test),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(Res.string.delete_diagnostic_test),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

/**
 * Helper function to get diagnostic test type label
 */
private fun getDiagnosticTestTypeLabel(type: String): String {
    return when (type) {
        "RMN" -> "RMN"
        "TAC" -> "TAC"
        "RX" -> "RX"
        "ecografia" -> "Ecografia"
        "esami_ematochimici" -> "Esami Ematochimici"
        "altro" -> "Altro"
        else -> ""
    }
}

/**
 * Reusable component for clinical history sections
 */
@Composable
private fun ClinicalHistorySection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
