package com.narde.gestionaleosteopatabetto.ui.components.apparati.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.ui.mvi.AddVisitEvent
import com.narde.gestionaleosteopatabetto.ui.mvi.ApparatoRespiratorioState

/**
 * Form component for Respiratory apparatus evaluation
 */
@Composable
fun ApparatoRespiratorioForm(
    state: ApparatoRespiratorioState,
    onEvent: (AddVisitEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Valutazione Apparato Respiratorio",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        
        // Dyspnea section
        Text(
            text = "Dispnea",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        CheckboxWithLabel(
            checked = state.dispneaPresente,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateRespiratorioField("dispneaPresente", checked))
            },
            label = "Dispnea presente"
        )
        
        if (state.dispneaPresente) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 32.dp)
            ) {
                CheckboxWithLabel(
                    checked = state.dispneaSottoSforzo,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateRespiratorioField("dispneaSottoSforzo", checked))
                    },
                    label = "Sotto sforzo"
                )
                
                CheckboxWithLabel(
                    checked = state.dispneaARiposo,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateRespiratorioField("dispneaARiposo", checked))
                    },
                    label = "A riposo"
                )
                
                CheckboxWithLabel(
                    checked = state.dispneaNotturna,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateRespiratorioField("dispneaNotturna", checked))
                    },
                    label = "Notturna"
                )
            }
        }
        
        // Other symptoms
        CheckboxWithLabel(
            checked = state.oppressioneToracica,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateRespiratorioField("oppressioneToracica", checked))
            },
            label = "Oppressione toracica"
        )
        
        // Cough section
        Text(
            text = "Tosse",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        CheckboxWithLabel(
            checked = state.tossePresente,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateRespiratorioField("tossePresente", checked))
            },
            label = "Tosse presente"
        )
        
        if (state.tossePresente) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 32.dp)
            ) {
                OutlinedTextField(
                    value = state.tosseTipo,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateRespiratorioField("tosseTipo", value))
                    },
                    label = { Text("Tipo") },
                    placeholder = { Text("secca, produttiva, mista") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                CheckboxWithLabel(
                    checked = state.tosseNotturna,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateRespiratorioField("tosseNotturna", checked))
                    },
                    label = "Notturna"
                )
                
                CheckboxWithLabel(
                    checked = state.tosseCronica,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateRespiratorioField("tosseCronica", checked))
                    },
                    label = "Cronica"
                )
                
                CheckboxWithLabel(
                    checked = state.tosseConSangue,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateRespiratorioField("tosseConSangue", checked))
                    },
                    label = "Con sangue"
                )
            }
        }
        
        // Respiratory allergies
        Text(
            text = "Allergie Respiratorie",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        CheckboxWithLabel(
            checked = state.allergieRespiratoriePresente,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateRespiratorioField("allergieRespiratoriePresente", checked))
            },
            label = "Allergie respiratorie presenti"
        )
        
        if (state.allergieRespiratoriePresente) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 32.dp)
            ) {
                OutlinedTextField(
                    value = state.allergieRespiratorieAllergeni.joinToString(", "),
                    onValueChange = { value ->
                        val list = value.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        onEvent(AddVisitEvent.UpdateRespiratorioField("allergieRespiratorieAllergeni", list))
                    },
                    label = { Text("Allergeni") },
                    placeholder = { Text("polline, graminacee, acari (separati da virgola)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = state.allergieRespiratorieStagionalita,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateRespiratorioField("allergieRespiratorieStagionalita", value))
                    },
                    label = { Text("Stagionalità") },
                    placeholder = { Text("primavera, estate, tutto l'anno") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Other symptoms
        CheckboxWithLabel(
            checked = state.raucedine,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateRespiratorioField("raucedine", checked))
            },
            label = "Raucedine"
        )
        
        CheckboxWithLabel(
            checked = state.brucioreGola,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateRespiratorioField("brucioreGola", checked))
            },
            label = "Bruciore gola"
        )
        
        // Snoring
        Text(
            text = "Russare",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        CheckboxWithLabel(
            checked = state.russarePresente,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateRespiratorioField("russarePresente", checked))
            },
            label = "Russare presente"
        )
        
        if (state.russarePresente) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 32.dp)
            ) {
                OutlinedTextField(
                    value = state.russareIntensita,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateRespiratorioField("russareIntensita", value))
                    },
                    label = { Text("Intensità") },
                    placeholder = { Text("lieve, moderata, grave") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                CheckboxWithLabel(
                    checked = state.russareApneeNotturne,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateRespiratorioField("russareApneeNotturne", checked))
                    },
                    label = "Apnee notturne"
                )
            }
        }
    }
}

/**
 * Helper composable for checkbox with label
 */
@Composable
private fun CheckboxWithLabel(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}


