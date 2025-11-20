package com.narde.gestionaleosteopatabetto.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.data.model.Visit
import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.ui.components.apparati.ExpandableApparatusCard
import com.narde.gestionaleosteopatabetto.ui.components.apparati.ApparatusMetadata

/**
 * Visit details screen showing comprehensive visit information
 * Displays all available visit data in organized sections
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitDetailsScreen(
    visit: Visit,
    patient: Patient,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Dettagli Visita",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Indietro"
                    )
                }
            },
            actions = {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Modifica visita",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Visit ID Card
            VisitInfoCard(visit)
            
            // Patient Information Card
            PatientInfoCard(patient)
            
            // Current Visit Data Card
            visit.datiVisitaCorrente?.let { currentData ->
                CurrentVisitDataCard(currentData)
            }
            
            // Consultation Reason Card
            visit.motivoConsulto?.let { motivo ->
                ConsultationReasonCard(motivo)
            }
            
            // General Notes Card
            if (visit.noteGenerali.isNotEmpty()) {
                GeneralNotesCard(visit.noteGenerali)
            }
            
            // Apparatus Evaluation Sections
            // Always show apparatus sections, even if valutazioneApparati is null
            // Debug: Check if valutazioneApparati exists
            LaunchedEffect(visit.idVisita) {
                println("VisitDetailsScreen: Visit ID: ${visit.idVisita}")
                println("VisitDetailsScreen: valutazioneApparati is null: ${visit.valutazioneApparati == null}")
                visit.valutazioneApparati?.let { valutazioneApparati ->
                    println("VisitDetailsScreen: valutazioneApparati exists")
                    println("VisitDetailsScreen: cranio is null: ${valutazioneApparati.cranio == null}")
                    valutazioneApparati.cranio?.let { cranio ->
                        println("VisitDetailsScreen: cranio exists")
                        println("VisitDetailsScreen: problemiOlfatto: ${cranio.problemiOlfatto}")
                        println("VisitDetailsScreen: problemiVista: ${cranio.problemiVista}")
                    }
                }
            }
            ApparatusSections(valutazione = visit.valutazioneApparati)
        }
    }
}

/**
 * State to track which apparatus cards are expanded
 */
@Composable
private fun rememberApparatusExpandedState(): MutableMap<String, Boolean> {
    return remember { mutableStateMapOf() }
}

/**
 * Display all apparatus sections
 * Always shows all 12 apparatus cards, displaying "Nessun dato inserito" when no data exists
 */
@Composable
private fun ApparatusSections(
    valutazione: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ValutazioneApparati?
) {
    val expandedStates = rememberApparatusExpandedState()
    
    // Render all 12 apparatus cards
    ApparatusMetadata.allApparatus.forEach { apparatusInfo ->
        val isExpanded = expandedStates[apparatusInfo.key] ?: false
        val apparatusData = valutazione?.let { valutazioneApparati ->
            val data = when (apparatusInfo.key) {
                "cranio" -> valutazioneApparati.cranio
                "respiratorio" -> valutazioneApparati.respiratorio
                "cardiovascolare" -> valutazioneApparati.cardiovascolare
                "gastrointestinale" -> valutazioneApparati.gastrointestinale
                "urinario" -> valutazioneApparati.urinario
                "riproduttivo" -> valutazioneApparati.riproduttivo
                "psicoNeuroEndocrino" -> valutazioneApparati.psicoNeuroEndocrino
                "unghieCute" -> valutazioneApparati.unghieCute
                "metabolismo" -> valutazioneApparati.metabolismo
                "linfonodi" -> valutazioneApparati.linfonodi
                "muscoloScheletrico" -> valutazioneApparati.muscoloScheletrico
                "nervoso" -> valutazioneApparati.nervoso
                else -> null
            }
            // Debug logging for cranio
            if (apparatusInfo.key == "cranio") {
                println("ApparatusSections: valutazioneApparati exists: ${valutazioneApparati != null}")
                println("ApparatusSections: cranio data: $data")
                (data as? com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoCranio)?.let { cranio ->
                    println("ApparatusSections: cranio.problemiOlfatto: ${cranio.problemiOlfatto}")
                }
            }
            data
        } ?: null
        
        val hasData = apparatusData != null
        // Debug logging
        if (apparatusInfo.key == "cranio") {
            println("ApparatusSections: hasData for cranio: $hasData")
        }
        
        ExpandableApparatusCard(
            apparatusKey = apparatusInfo.key,
            apparatusName = apparatusInfo.italianName,
            icon = apparatusInfo.icon,
            isExpanded = isExpanded,
            hasData = hasData,
            onToggleExpanded = {
                expandedStates[apparatusInfo.key] = !isExpanded
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (hasData) {
                // Display apparatus-specific data
                when (apparatusInfo.key) {
                    "cranio" -> CranioDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoCranio)
                    "respiratorio" -> RespiratorioDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoRespiratorio)
                    "cardiovascolare" -> CardiovascolareDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoCardiovascolare)
                    "gastrointestinale" -> GastrointestinaleDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoGastrointestinale)
                    "urinario" -> UrinarioDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoUrinario)
                    "riproduttivo" -> RiproduttivoDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoRiproduttivo)
                    "psicoNeuroEndocrino" -> PsicoNeuroEndocrinoDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoPsicoNeuroEndocrino)
                    "unghieCute" -> UnghieCuteDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoUnghieCute)
                    "metabolismo" -> MetabolismoDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoMetabolismo)
                    "linfonodi" -> LinfonodiDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoLinfonodi)
                    "muscoloScheletrico" -> MuscoloScheletricoDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoMuscoloScheletrico)
                    "nervoso" -> NervosoDisplay(apparatusData as com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoNervoso)
                }
            } else {
                // Show "Nessun dato inserito" message
                Text(
                    text = "Nessun dato inserito",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun VisitInfoCard(visit: Visit) {
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
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Informazioni Visita",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
            InfoRow("ID Visita", visit.idVisita)
            InfoRow("Data Visita", visit.dataVisita)
            InfoRow("Osteopata", visit.osteopata)
        }
    }
}

@Composable
private fun PatientInfoCard(patient: Patient) {
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
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Informazioni Paziente",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
            // Basic patient information
            InfoRow("Nome Completo", patient.name)
            InfoRow("Età", "${patient.age} anni")
            InfoRow("Telefono", patient.phone)
            InfoRow("Email", patient.email)
            
            // BMI if available
            patient.bmi?.let { bmi ->
                InfoRow("BMI", String.format("%.1f", bmi))
            }
        }
    }
}

@Composable
private fun CurrentVisitDataCard(currentData: com.narde.gestionaleosteopatabetto.data.model.DatiVisitaCorrente) {
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
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Dati Visita Corrente",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
            InfoRow("Peso", "${currentData.peso} kg")
            InfoRow("BMI", currentData.bmi.toString())
            InfoRow("Pressione", currentData.pressione)
            InfoRow("Indici Craniali", currentData.indiciCraniali.toString())
        }
    }
}

@Composable
private fun ConsultationReasonCard(motivo: com.narde.gestionaleosteopatabetto.data.model.MotivoConsulto) {
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
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Motivo Consulto",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
            // Main reason
            motivo.principale?.let { principale ->
                Text(
                    text = "Motivo Principale",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                InfoRow("Descrizione", principale.descrizione)
                InfoRow("Insorgenza", principale.insorgenza)
                InfoRow("Dolore", principale.dolore)
                InfoRow("VAS", principale.vas.toString())
                InfoRow("Fattori", principale.fattori)
            }
            
            // Secondary reason
            motivo.secondario?.let { secondario ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Motivo Secondario",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                InfoRow("Descrizione", secondario.descrizione)
                InfoRow("Durata", secondario.durata)
                InfoRow("VAS", secondario.vas.toString())
            }
        }
    }
}

@Composable
private fun GeneralNotesCard(notes: String) {
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
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Note Generali",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
            Text(
                text = notes,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Helper function to format boolean values
 */
private fun formatBoolean(value: Boolean): String = if (value) "Sì" else "No"

/**
 * Helper function to format list values
 */
private fun formatList(list: List<String>): String {
    return if (list.isEmpty()) {
        "Nessuno"
    } else {
        list.joinToString(", ")
    }
}

/**
 * Display functions for each apparatus type
 * These functions display the apparatus data in a readable format
 */

@Composable
private fun CranioDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoCranio) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InfoRow("Problemi Olfatto", formatBoolean(apparato.problemiOlfatto))
        InfoRow("Problemi Vista", formatBoolean(apparato.problemiVista))
        InfoRow("Problemi Udito", formatBoolean(apparato.problemiUdito))
        InfoRow("Disturbi Occlusali", formatBoolean(apparato.disturbiOcclusali))
        InfoRow("Malattie Parodontali", formatBoolean(apparato.malattieParodontali))
        InfoRow("Lingua Dolente", formatBoolean(apparato.linguaDolente))
        
        apparato.cefalea?.let { cefalea ->
            if (cefalea.presente) {
                Text(
                    text = "Cefalea",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                InfoRow("Intensità VAS", cefalea.intensitaVas.toString())
                InfoRow("Frequenza", cefalea.frequenza)
                InfoRow("Durata (ore)", cefalea.durataOre.toString())
                cefalea.caratteristiche?.let { car ->
                    InfoRow("Tipo", car.tipo)
                    InfoRow("Localizzazione", formatList(car.localizzazione.toList()))
                    InfoRow("Fattori Scatenanti", formatList(car.fattoriScatenanti.toList()))
                    InfoRow("Fattori Allevianti", formatList(car.fattoriAllevianti.toList()))
                }
            }
        }
        
        apparato.emicrania?.let { emicrania ->
            if (emicrania.presente) {
                Text(
                    text = "Emicrania",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                InfoRow("Con Aura", formatBoolean(emicrania.conAura))
                InfoRow("Frequenza", emicrania.frequenza)
            }
        }
        
        apparato.atm?.let { atm ->
            if (atm.problemiPresenti) {
                Text(
                    text = "Problemi ATM",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                atm.sintomi?.let { sintomi ->
                    InfoRow("Click Articolare", formatBoolean(sintomi.clickArticolare))
                    InfoRow("Dolore Masticazione", formatBoolean(sintomi.doloreMasticazione))
                    InfoRow("Limitazione Apertura", formatBoolean(sintomi.limitazioneApertura))
                    InfoRow("Serramento Diurno", formatBoolean(sintomi.serramentoDiurno))
                    InfoRow("Bruxismo Notturno", formatBoolean(sintomi.bruxismoNotturno))
                    InfoRow("Deviazione Mandibolare", formatBoolean(sintomi.deviazioneMandibolare))
                }
            }
        }
        
        apparato.apparecchioOrtodontico?.let { apparecchio ->
            if (apparecchio.portato) {
                Text(
                    text = "Apparecchio Ortodontico",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                InfoRow("Periodo", apparecchio.periodo)
                InfoRow("Età Inizio", apparecchio.etaInizio.toString())
                InfoRow("Età Fine", apparecchio.etaFine.toString())
                InfoRow("Durata (anni)", apparecchio.durataAnni.toString())
                InfoRow("Tipo", apparecchio.tipo)
            }
        }
    }
}

@Composable
private fun RespiratorioDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoRespiratorio) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InfoRow("Oppressione Toracica", formatBoolean(apparato.oppressioneToracica))
        InfoRow("Raucedine", formatBoolean(apparato.raucedine))
        InfoRow("Bruciore Gola", formatBoolean(apparato.brucioreGola))
        
        apparato.dispnea?.let { dispnea ->
            if (dispnea.presente) {
                Text(
                    text = "Dispnea",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                InfoRow("Sotto Sforzo", formatBoolean(dispnea.sottoSforzo))
                InfoRow("A Riposo", formatBoolean(dispnea.aRiposo))
                InfoRow("Notturna", formatBoolean(dispnea.notturna))
            }
        }
        
        apparato.tosse?.let { tosse ->
            if (tosse.presente) {
                Text(
                    text = "Tosse",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                InfoRow("Tipo", tosse.tipo)
                InfoRow("Notturna", formatBoolean(tosse.notturna))
                InfoRow("Cronica", formatBoolean(tosse.cronica))
                InfoRow("Con Sangue", formatBoolean(tosse.conSangue))
            }
        }
        
        apparato.allergieRespiratorie?.let { allergie ->
            if (allergie.presente) {
                Text(
                    text = "Allergie Respiratorie",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                InfoRow("Allergeni", formatList(allergie.allergeni.toList()))
                InfoRow("Stagionalità", allergie.stagionalita)
                InfoRow("Sintomi", formatList(allergie.sintomi.toList()))
                InfoRow("Terapia", allergie.terapia)
            }
        }
        
        apparato.congestioneNasale?.let { congestione ->
            if (congestione.presente) {
                Text(
                    text = "Congestione Nasale",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                InfoRow("Cronica", formatBoolean(congestione.cronica))
                InfoRow("Stagionale", formatBoolean(congestione.stagionale))
                InfoRow("Monolaterale", formatBoolean(congestione.monolaterale))
            }
        }
        
        apparato.sinusite?.let { sinusite ->
            if (sinusite.presente) {
                Text(
                    text = "Sinusite",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                InfoRow("Ricorrente", formatBoolean(sinusite.ricorrente))
                InfoRow("Localizzazione", sinusite.localizzazione)
                InfoRow("Cronica", formatBoolean(sinusite.cronica))
            }
        }
        
        apparato.russare?.let { russare ->
            if (russare.presente) {
                Text(
                    text = "Russare",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                InfoRow("Intensità", russare.intensita)
                InfoRow("Frequenza", russare.frequenza)
                InfoRow("Disturba Sonno", formatBoolean(russare.disturbaSonno))
                InfoRow("Apnee Notturne", formatBoolean(russare.apneeNotturne))
            }
        }
        
        apparato.raffreddoriFrequenti?.let { raffreddori ->
            if (raffreddori.presente) {
                Text(
                    text = "Raffreddori Frequenti",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                InfoRow("Frequenza/Anno", raffreddori.frequenzaAnno.toString())
            }
        }
    }
}

@Composable
private fun CardiovascolareDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoCardiovascolare) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Add display logic for cardiovascular apparatus
        // This is a placeholder - implement based on ApparatoCardiovascolare structure
        Text(
            text = "Dati cardiovascolari disponibili",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun GastrointestinaleDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoGastrointestinale) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Add display logic for gastrointestinal apparatus
        // This is a placeholder - implement based on ApparatoGastrointestinale structure
        Text(
            text = "Dati gastrointestinali disponibili",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun UrinarioDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoUrinario) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Add display logic for urinary apparatus
        // This is a placeholder - implement based on ApparatoUrinario structure
        Text(
            text = "Dati urinari disponibili",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun RiproduttivoDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoRiproduttivo) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Add display logic for reproductive apparatus
        // This is a placeholder - implement based on ApparatoRiproduttivo structure
        Text(
            text = "Dati riproduttivi disponibili",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PsicoNeuroEndocrinoDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoPsicoNeuroEndocrino) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Add display logic for psycho-neuro-endocrine apparatus
        // This is a placeholder - implement based on ApparatoPsicoNeuroEndocrino structure
        Text(
            text = "Dati psico-neuro-endocrini disponibili",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun UnghieCuteDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoUnghieCute) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Add display logic for nails and skin apparatus
        // This is a placeholder - implement based on ApparatoUnghieCute structure
        Text(
            text = "Dati unghie e cute disponibili",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun MetabolismoDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoMetabolismo) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Add display logic for metabolism apparatus
        // This is a placeholder - implement based on ApparatoMetabolismo structure
        Text(
            text = "Dati metabolismo disponibili",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LinfonodiDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoLinfonodi) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Add display logic for lymph nodes apparatus
        // This is a placeholder - implement based on ApparatoLinfonodi structure
        Text(
            text = "Dati linfonodi disponibili",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun MuscoloScheletricoDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoMuscoloScheletrico) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Add display logic for musculoskeletal apparatus
        // This is a placeholder - implement based on ApparatoMuscoloScheletrico structure
        Text(
            text = "Dati muscolo-scheletrici disponibili",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun NervosoDisplay(apparato: com.narde.gestionaleosteopatabetto.data.database.models.apparati.ApparatoNervoso) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Add display logic for nervous apparatus
        // This is a placeholder - implement based on ApparatoNervoso structure
        Text(
            text = "Dati nervosi disponibili",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

