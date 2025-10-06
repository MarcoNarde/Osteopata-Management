package com.narde.gestionaleosteopatabetto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.database.models.Patient as DatabasePatient
import com.narde.gestionaleosteopatabetto.data.database.utils.*
import com.narde.gestionaleosteopatabetto.data.database.DatabaseInitializer
import com.narde.gestionaleosteopatabetto.data.database.isDatabaseSupported
import com.narde.gestionaleosteopatabetto.utils.DateUtils
import org.jetbrains.compose.resources.stringResource
import gestionaleosteopatabetto.composeapp.generated.resources.Res
import gestionaleosteopatabetto.composeapp.generated.resources.*

/**
 * NEW: Clean PatientDetailsScreen with view-only architecture
 * Follows the same pattern as VisitDetailsScreen for consistency
 * 
 * Key Features:
 * - Takes only patientId as parameter (like VisitDetailsScreen)
 * - Read-only display of all patient information
 * - Organized sections for Personal Data and Clinical History
 * - Edit button that navigates to future EditPatientScreen
 * - Clean separation of concerns
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreenNew(
    patientId: String,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // State for patient data loading
    var databasePatient by remember { mutableStateOf<DatabasePatient?>(null) }
    var uiPatient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
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
                        text = uiPatient?.name ?: "Dettagli Paziente",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
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
                    if (uiPatient != null) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(Res.string.edit),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
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
                
                uiPatient != null && databasePatient != null -> {
                    // Success state - display patient information
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Personal Information Section
                        PersonalInformationCard(uiPatient!!, databasePatient!!)
                        
                        // Anthropometric Measurements Section
                        AnthropometricMeasurementsCard(databasePatient!!)
                        
                        // Contact Information Section
                        ContactInformationCard(uiPatient!!)
                        
                        // Address Information Section
                        AddressInformationCard(databasePatient!!)
                        
                        // Privacy Information Section
                        PrivacyInformationCard(databasePatient!!)
                        
                        // Parent Information Section (for minors)
                        ParentInformationCard(uiPatient!!)
                        
                        // Clinical History Section
                        ClinicalHistoryCard(databasePatient!!)
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
}

/**
 * Personal Information Card - Basic patient demographics
 */
@Composable
private fun PersonalInformationCard(
    uiPatient: Patient,
    databasePatient: DatabasePatient
) {
    PatientInfoCard(
        title = stringResource(Res.string.personal_information),
        icon = Icons.Default.Person,
        content = {
            PatientInfoRow(
                label = stringResource(Res.string.patient_id),
                value = uiPatient.id
            )
            PatientInfoRow(
                label = stringResource(Res.string.patient_name),
                value = uiPatient.name
            )

            // Calculate and display age properly
            databasePatient.datiPersonali?.dataNascita?.takeIf { it.isNotEmpty() }
                ?.let { birthDateStr ->
                    val calculatedAge = try {
                        val birthDate = LocalDate.parse(birthDateStr)
                        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                        
                        var age = currentDate.year - birthDate.year
                        if (currentDate.monthNumber < birthDate.monthNumber || 
                            (currentDate.monthNumber == birthDate.monthNumber && currentDate.dayOfMonth < birthDate.dayOfMonth)) {
                            age--
                        }
                        age
                    } catch (e: Exception) {
                        0
                    }
                    if (calculatedAge > 0) {
                        PatientInfoRow(
                            label = stringResource(Res.string.patient_age),
                            value = calculatedAge.toString()
                        )
                    }
                }

            // Additional fields from database
            if (databasePatient.datiPersonali?.dataNascita?.isNotEmpty() == true) {
                PatientInfoRow(
                    label = stringResource(Res.string.birth_date_label),
                    value = DateUtils.convertIsoToItalianFormat(
                        databasePatient.datiPersonali?.dataNascita ?: ""
                    )
                )
            }
            databasePatient.datiPersonali?.sesso?.takeIf { it.isNotEmpty() }?.let {
                PatientInfoRow(
                    label = stringResource(Res.string.gender),
                    value = if (it == "M") stringResource(Res.string.gender_male) else stringResource(
                        Res.string.gender_female
                    )
                )
            }
            databasePatient.datiPersonali?.luogoNascita?.takeIf { it.isNotEmpty() }?.let {
                PatientInfoRow(
                    label = stringResource(Res.string.place_of_birth_label),
                    value = it
                )
            }
            databasePatient.datiPersonali?.codiceFiscale?.takeIf { it.isNotEmpty() }?.let {
                PatientInfoRow(
                    label = stringResource(Res.string.tax_code_label),
                    value = it
                )
            }
        }
    )
}

/**
 * Anthropometric Measurements Card
 */
@Composable
private fun AnthropometricMeasurementsCard(databasePatient: DatabasePatient) {
    val personalData = databasePatient.datiPersonali
    if (personalData != null && (personalData.altezza > 0 || personalData.peso > 0.0 || personalData.latoDominante.isNotEmpty())) {
        PatientInfoCard(
            title = stringResource(Res.string.anthropometric_measurements),
            icon = Icons.Default.Info,
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
    }
}

/**
 * Contact Information Card
 */
@Composable
private fun ContactInformationCard(uiPatient: Patient) {
        PatientInfoCard(
            title = stringResource(Res.string.contact_information),
            icon = Icons.Default.Phone,
        content = {
            PatientInfoRow(
                label = stringResource(Res.string.phone_number),
                value = uiPatient.phone
            )
            PatientInfoRow(
                label = stringResource(Res.string.email),
                value = uiPatient.email
            )
        }
    )
}

/**
 * Address Information Card
 */
@Composable
private fun AddressInformationCard(databasePatient: DatabasePatient) {
    val address = databasePatient.indirizzo
    if (address != null && (address.via.isNotEmpty() || address.citta.isNotEmpty() || address.cap.isNotEmpty() || address.provincia.isNotEmpty())) {
        PatientInfoCard(
            title = stringResource(Res.string.address_information),
            icon = Icons.Default.Home,
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
    }
}

/**
 * Privacy Information Card
 */
@Composable
private fun PrivacyInformationCard(databasePatient: DatabasePatient) {
    val privacy = databasePatient.privacy
    if (privacy != null) {
        PatientInfoCard(
            title = stringResource(Res.string.privacy_information),
            icon = Icons.Default.Lock,
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
 * Parent Information Card (only for minors)
 */
@Composable
private fun ParentInformationCard(uiPatient: Patient) {
    if (uiPatient.age > 0 && uiPatient.age < 18) {
        uiPatient.parentInfo?.let { parentInfo ->
            PatientInfoCard(
                title = stringResource(Res.string.parent_information_minor, uiPatient.age.toString()),
                icon = Icons.Default.Person,
                content = {
                    // Father information
                    parentInfo.father?.let { father ->
                        if (father.firstName.isNotEmpty() || father.lastName.isNotEmpty()) {
                            PatientInfoRow(
                                label = stringResource(Res.string.father_info),
                                value = "${father.firstName} ${father.lastName}".trim()
                            )
                            
                            if (father.phone.isNotEmpty()) {
                                PatientInfoRow(
                                    label = "${stringResource(Res.string.father_info)} - ${stringResource(Res.string.phone_number)}",
                                    value = father.phone
                                )
                            }
                            
                            if (father.email.isNotEmpty()) {
                                PatientInfoRow(
                                    label = "${stringResource(Res.string.father_info)} - ${stringResource(Res.string.email)}",
                                    value = father.email
                                )
                            }
                            
                            if (father.profession.isNotEmpty()) {
                                PatientInfoRow(
                                    label = "${stringResource(Res.string.father_info)} - Profession",
                                    value = father.profession
                                )
                            }
                        }
                    }
                    
                    // Mother information
                    parentInfo.mother?.let { mother ->
                        if (mother.firstName.isNotEmpty() || mother.lastName.isNotEmpty()) {
                            PatientInfoRow(
                                label = stringResource(Res.string.mother_info),
                                value = "${mother.firstName} ${mother.lastName}".trim()
                            )
                            
                            if (mother.phone.isNotEmpty()) {
                                PatientInfoRow(
                                    label = "${stringResource(Res.string.mother_info)} - ${stringResource(Res.string.phone_number)}",
                                    value = mother.phone
                                )
                            }
                            
                            if (mother.email.isNotEmpty()) {
                                PatientInfoRow(
                                    label = "${stringResource(Res.string.mother_info)} - ${stringResource(Res.string.email)}",
                                    value = mother.email
                                )
                            }
                            
                            if (mother.profession.isNotEmpty()) {
                                PatientInfoRow(
                                    label = "${stringResource(Res.string.mother_info)} - Profession",
                                    value = mother.profession
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

/**
 * Clinical History Card - Comprehensive medical history
 */
@Composable
private fun ClinicalHistoryCard(databasePatient: DatabasePatient) {
    val storiaClinica = databasePatient.storiaClinica
    
    if (storiaClinica != null) {
        PatientInfoCard(
            title = "Storia Clinica",
            icon = Icons.Default.Info,
            content = {
                // Chronic Conditions Section
                storiaClinica.patologieCroniche?.let { patologie ->
                    Text(
                        text = stringResource(Res.string.chronic_conditions),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    ChronicConditionsContent(patologie)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Lifestyle Factors Section
                storiaClinica.stileVita?.let { stileVita ->
                    Text(
                        text = stringResource(Res.string.lifestyle_factors),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LifestyleFactorsContent(stileVita)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Pharmacological Therapies Section
                if (storiaClinica.terapieFarmacologiche.isNotEmpty()) {
                    Text(
                        text = stringResource(Res.string.pharmacological_therapies),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    PharmacologicalTherapiesContent(storiaClinica.terapieFarmacologiche)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Interventions & Traumas Section
                if (storiaClinica.interventiTrauma.isNotEmpty()) {
                    Text(
                        text = stringResource(Res.string.interventions_traumas),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    InterventionsTraumasContent(storiaClinica.interventiTrauma)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Diagnostic Tests Section
                if (storiaClinica.esamiStrumentali.isNotEmpty()) {
                    Text(
                        text = stringResource(Res.string.diagnostic_tests),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    DiagnosticTestsContent(storiaClinica.esamiStrumentali)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Pediatric History Section
                storiaClinica.anamnesiPediatrica?.let { anamnesi ->
                    Text(
                        text = stringResource(Res.string.pediatric_history),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    PediatricHistoryContent(anamnesi)
                }
            }
        )
    } else {
        // No clinical history available
        PatientInfoCard(
            title = "Storia Clinica",
            icon = Icons.Default.Info,
            content = {
                Text(
                    text = stringResource(Res.string.no_clinical_history),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

/**
 * Reusable PatientInfoCard component
 */
@Composable
private fun PatientInfoCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
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

// Import the clinical history content components from the original PatientDetailsScreen
// These are the same components that display chronic conditions, lifestyle factors, etc.

/**
 * Chronic conditions content
 */
@Composable
private fun ChronicConditionsContent(patologie: com.narde.gestionaleosteopatabetto.data.database.models.PatologieCroniche) {
    // Drug allergies
    patologie.allergieFarmaci?.let { allergie ->
        if (allergie.presente) {
            PatientInfoRow(
                label = stringResource(Res.string.drug_allergies),
                value = stringResource(Res.string.present)
            )
            if (allergie.listaAllergie.isNotEmpty()) {
                PatientInfoRow(
                    label = stringResource(Res.string.allergies_list),
                    value = allergie.listaAllergie.joinToString(", ")
                )
            }
        } else {
            PatientInfoRow(
                label = stringResource(Res.string.drug_allergies),
                value = stringResource(Res.string.not_present)
            )
        }
    }
    
    // Diabetes
    patologie.diabete?.let { diabete ->
        if (diabete.presente) {
            PatientInfoRow(
                label = stringResource(Res.string.diabetes),
                value = stringResource(Res.string.present)
            )
            if (diabete.tipologia.isNotEmpty()) {
                PatientInfoRow(
                    label = stringResource(Res.string.type),
                    value = diabete.tipologia
                )
            }
        } else {
            PatientInfoRow(
                label = stringResource(Res.string.diabetes),
                value = stringResource(Res.string.not_present)
            )
        }
    }
    
    // Other conditions
    if (patologie.ipertiroidismo) {
        PatientInfoRow(
            label = stringResource(Res.string.hyperthyroidism),
            value = stringResource(Res.string.present)
        )
    }
    
    if (patologie.cardiopatia) {
        PatientInfoRow(
            label = stringResource(Res.string.heart_disease),
            value = stringResource(Res.string.present)
        )
    }
    
    if (patologie.ipertensioneArteriosa) {
        PatientInfoRow(
            label = stringResource(Res.string.arterial_hypertension),
            value = stringResource(Res.string.present)
        )
    }
    
    // Other pathologies
    if (patologie.altrePatologie.isNotEmpty()) {
        patologie.altrePatologie.forEach { patologia ->
            PatientInfoRow(
                label = patologia.patologia,
                value = "${patologia.stato} (${patologia.dataInsorgenza})"
            )
        }
    }
}

/**
 * Lifestyle factors content
 */
@Composable
private fun LifestyleFactorsContent(stileVita: com.narde.gestionaleosteopatabetto.data.database.models.StileVita) {
    // Smoking habits
    stileVita.tabagismo?.let { tabagismo ->
        PatientInfoRow(
            label = stringResource(Res.string.smoking_habits),
            value = tabagismo.stato
        )
        
        if (tabagismo.sigaretteGiorno > 0) {
            PatientInfoRow(
                label = stringResource(Res.string.cigarettes_per_day),
                value = tabagismo.sigaretteGiorno.toString()
            )
        }
        
        if (tabagismo.anniFumo > 0) {
            PatientInfoRow(
                label = stringResource(Res.string.years_smoking),
                value = tabagismo.anniFumo.toString()
            )
        }
        
        if (tabagismo.dataSmettere.isNotEmpty()) {
            PatientInfoRow(
                label = stringResource(Res.string.quit_date),
                value = tabagismo.dataSmettere
            )
        }
    }
    
    // Work information
    if (stileVita.lavoro.isNotEmpty()) {
        PatientInfoRow(
            label = stringResource(Res.string.work_type),
            value = stileVita.lavoro
        )
    }
    
    if (stileVita.professione.isNotEmpty()) {
        PatientInfoRow(
            label = stringResource(Res.string.profession),
            value = stileVita.professione
        )
    }
    
    if (stileVita.oreLavoroGiorno > 0) {
        PatientInfoRow(
            label = stringResource(Res.string.work_hours_per_day),
            value = stileVita.oreLavoroGiorno.toString()
        )
    }
    
    // Physical activity
    stileVita.attivitaSportiva?.let { sport ->
        if (sport.presente) {
            PatientInfoRow(
                label = stringResource(Res.string.physical_activity),
                value = stringResource(Res.string.present)
            )
            
            if (sport.sport.isNotEmpty()) {
                PatientInfoRow(
                    label = stringResource(Res.string.sports),
                    value = sport.sport.joinToString(", ")
                )
            }
            
            if (sport.frequenza.isNotEmpty()) {
                PatientInfoRow(
                    label = stringResource(Res.string.frequency),
                    value = sport.frequenza
                )
            }
            
            if (sport.intensita.isNotEmpty()) {
                PatientInfoRow(
                    label = stringResource(Res.string.intensity),
                    value = sport.intensita
                )
            }
        } else {
            PatientInfoRow(
                label = stringResource(Res.string.physical_activity),
                value = stringResource(Res.string.not_present)
            )
        }
    }
}

/**
 * Pharmacological therapies content
 */
@Composable
private fun PharmacologicalTherapiesContent(terapie: io.realm.kotlin.types.RealmList<com.narde.gestionaleosteopatabetto.data.database.models.TerapiaFarmacologica>) {
    terapie.forEachIndexed { index, terapia ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "${stringResource(Res.string.medication)} ${index + 1}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                PatientInfoRow(
                    label = stringResource(Res.string.medication),
                    value = terapia.farmaco
                )
                
                if (terapia.dosaggio.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.dosage),
                        value = terapia.dosaggio
                    )
                }
                
                if (terapia.frequenza.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.frequency),
                        value = terapia.frequenza
                    )
                }
                
                if (terapia.dataInizio.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.start_date),
                        value = terapia.dataInizio
                    )
                }
                
                val endDate = terapia.dataFine
                if (endDate != null && endDate.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.end_date),
                        value = endDate
                    )
                } else {
                    PatientInfoRow(
                        label = stringResource(Res.string.status),
                        value = stringResource(Res.string.ongoing)
                    )
                }
                
                if (terapia.indicazione.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.indication),
                        value = terapia.indicazione
                    )
                }
            }
        }
    }
}

/**
 * Interventions and traumas content
 */
@Composable
private fun InterventionsTraumasContent(interventi: io.realm.kotlin.types.RealmList<com.narde.gestionaleosteopatabetto.data.database.models.InterventoTrauma>) {
    interventi.forEachIndexed { index, intervento ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "${stringResource(Res.string.intervention_type)} ${index + 1}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                PatientInfoRow(
                    label = stringResource(Res.string.intervention_type),
                    value = intervento.tipo
                )
                
                if (intervento.data.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.start_date),
                        value = intervento.data
                    )
                }
                
                if (intervento.descrizione.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.description),
                        value = intervento.descrizione
                    )
                }
                
                if (intervento.trattamento.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.treatment),
                        value = intervento.trattamento
                    )
                }
                
                if (intervento.esito.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.outcome),
                        value = intervento.esito
                    )
                }
            }
        }
    }
}

/**
 * Diagnostic tests content
 */
@Composable
private fun DiagnosticTestsContent(esami: io.realm.kotlin.types.RealmList<com.narde.gestionaleosteopatabetto.data.database.models.EsameStrumentale>) {
    esami.forEachIndexed { index, esame ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "${stringResource(Res.string.test_type)} ${index + 1}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                PatientInfoRow(
                    label = stringResource(Res.string.test_type),
                    value = esame.tipo
                )
                
                if (esame.data.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.start_date),
                        value = esame.data
                    )
                }
                
                if (esame.distretto.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.body_area),
                        value = esame.distretto
                    )
                }
                
                if (esame.risultato.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.results),
                        value = esame.risultato
                    )
                }
                
                if (esame.struttura.isNotEmpty()) {
                    PatientInfoRow(
                        label = stringResource(Res.string.facility),
                        value = esame.struttura
                    )
                }
            }
        }
    }
}

/**
 * Pediatric history content
 */
@Composable
private fun PediatricHistoryContent(anamnesi: com.narde.gestionaleosteopatabetto.data.database.models.AnamnesiPediatrica) {
    // Pregnancy
    anamnesi.gravidanza?.let { gravidanza ->
        PatientInfoRow(
            label = stringResource(Res.string.pregnancy),
            value = if (gravidanza.complicazioni) stringResource(Res.string.complications) else stringResource(Res.string.not_present)
        )
        
        if (gravidanza.note.isNotEmpty()) {
            PatientInfoRow(
                label = stringResource(Res.string.general_notes),
                value = gravidanza.note
            )
        }
    }
    
    // Birth
    anamnesi.parto?.let { parto ->
        if (parto.tipo.isNotEmpty()) {
            PatientInfoRow(
                label = stringResource(Res.string.birth_type),
                value = parto.tipo
            )
        }
        
        PatientInfoRow(
            label = stringResource(Res.string.complications),
            value = if (parto.complicazioni) stringResource(Res.string.present) else stringResource(Res.string.not_present)
        )
        
        if (parto.pesoNascitaGrammi > 0) {
            PatientInfoRow(
                label = stringResource(Res.string.birth_weight_grams),
                value = parto.pesoNascitaGrammi.toString()
            )
        }
        
        if (parto.punteggioApgar5min > 0) {
            PatientInfoRow(
                label = stringResource(Res.string.apgar_score_5min),
                value = parto.punteggioApgar5min.toString()
            )
        }
        
        if (parto.note.isNotEmpty()) {
            PatientInfoRow(
                label = stringResource(Res.string.general_notes),
                value = parto.note
            )
        }
    }
    
    // Development
    anamnesi.sviluppo?.let { sviluppo ->
        if (sviluppo.primiPassiMesi > 0) {
            PatientInfoRow(
                label = stringResource(Res.string.first_steps_months),
                value = sviluppo.primiPassiMesi.toString()
            )
        }
        
        if (sviluppo.primeParoleMesi > 0) {
            PatientInfoRow(
                label = stringResource(Res.string.first_words_months),
                value = sviluppo.primeParoleMesi.toString()
            )
        }
        
        PatientInfoRow(
            label = stringResource(Res.string.development_problems),
            value = if (sviluppo.problemiSviluppo) stringResource(Res.string.present) else stringResource(Res.string.not_present)
        )
        
        if (sviluppo.note.isNotEmpty()) {
            PatientInfoRow(
                label = stringResource(Res.string.general_notes),
                value = sviluppo.note
            )
        }
    }
    
    // General notes
    if (anamnesi.noteGenerali.isNotEmpty()) {
        PatientInfoRow(
            label = stringResource(Res.string.general_notes),
            value = anamnesi.noteGenerali
        )
    }
}
