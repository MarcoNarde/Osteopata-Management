package com.narde.gestionaleosteopatabetto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Chronic Conditions Section
            ClinicalHistorySection(
                title = stringResource(Res.string.chronic_conditions),
                content = {
                    ChronicConditionsEditContent(uiState, viewModel, focusManager)
                }
            )
        }
        
        item {
            // Lifestyle Factors Section
            ClinicalHistorySection(
                title = stringResource(Res.string.lifestyle_factors),
                content = {
                    LifestyleFactorsEditContent(uiState, viewModel, focusManager)
                }
            )
        }
        
        item {
            // Pharmacological Therapies Section
            ClinicalHistorySection(
                title = stringResource(Res.string.pharmacological_therapies),
                content = {
                    PharmacologicalTherapiesEditContent(uiState, viewModel, focusManager)
                }
            )
        }
        
        item {
            // Interventions & Traumas Section
            ClinicalHistorySection(
                title = stringResource(Res.string.interventions_traumas),
                content = {
                    InterventionsTraumasEditContent(uiState, viewModel, focusManager)
                }
            )
        }
        
        item {
            // Diagnostic Tests Section
            ClinicalHistorySection(
                title = stringResource(Res.string.diagnostic_tests),
                content = {
                    DiagnosticTestsEditContent(uiState, viewModel, focusManager)
                }
            )
        }
        
        item {
            // Pediatric History Section
            ClinicalHistorySection(
                title = stringResource(Res.string.pediatric_history),
                content = {
                    PediatricHistoryEditContent(uiState, viewModel, focusManager)
                }
            )
        }
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
    _uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryUiState,
    _viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(Res.string.pharmacological_therapies_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Note: For now, pharmacological therapies are managed through the view mode
        // Future enhancement: Add dynamic list management for medications
        OutlinedTextField(
            value = "", // Placeholder for future implementation
            onValueChange = { },
            label = { Text(stringResource(Res.string.add_medication)) },
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
    }
}

/**
 * Interventions & Traumas Edit Content
 */
@Composable
private fun InterventionsTraumasEditContent(
    _uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryUiState,
    _viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(Res.string.interventions_traumas_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Note: For now, interventions are managed through the view mode
        // Future enhancement: Add dynamic list management for interventions
        OutlinedTextField(
            value = "", // Placeholder for future implementation
            onValueChange = { },
            label = { Text(stringResource(Res.string.add_intervention)) },
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
    }
}

/**
 * Diagnostic Tests Edit Content
 */
@Composable
private fun DiagnosticTestsEditContent(
    _uiState: com.narde.gestionaleosteopatabetto.ui.viewmodels.ClinicalHistoryUiState,
    _viewModel: ClinicalHistoryViewModel,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(Res.string.diagnostic_tests_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Note: For now, diagnostic tests are managed through the view mode
        // Future enhancement: Add dynamic list management for tests
        OutlinedTextField(
            value = "", // Placeholder for future implementation
            onValueChange = { },
            label = { Text(stringResource(Res.string.add_diagnostic_test)) },
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
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
