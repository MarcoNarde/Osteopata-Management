package com.narde.gestionaleosteopatabetto.ui.components.apparati.forms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.ui.mvi.AddVisitEvent
import com.narde.gestionaleosteopatabetto.ui.mvi.ApparatoLinfonodiState

/**
 * Form component for Lymph Nodes apparatus evaluation
 */
@Composable
fun ApparatoLinfonodiForm(
    state: ApparatoLinfonodiState,
    onEvent: (AddVisitEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Valutazione Linfonodi",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        
        // Anomalies section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.anomaliePresente,
                onCheckedChange = { checked ->
                    onEvent(AddVisitEvent.UpdateLinfonodiField("anomaliePresente", checked))
                },
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Anomalie presenti",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Details (shown only if anomalies present)
        if (state.anomaliePresente) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.anomalieLocalizzazione,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateLinfonodiField("anomalieLocalizzazione", value))
                    },
                    label = { Text("Localizzazione") },
                    placeholder = { Text("es. cervicale, ascellare, inguinale") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = state.anomalieDimensione,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateLinfonodiField("anomalieDimensione", value))
                    },
                    label = { Text("Dimensione") },
                    placeholder = { Text("es. piccoli, medi, grandi o cm") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.anomalieMobilita,
                        onCheckedChange = { checked ->
                            onEvent(AddVisitEvent.UpdateLinfonodiField("anomalieMobilita", checked))
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Mobili",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.anomalieDolenti,
                        onCheckedChange = { checked ->
                            onEvent(AddVisitEvent.UpdateLinfonodiField("anomalieDolenti", checked))
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Dolenti",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                OutlinedTextField(
                    value = state.anomalieConsistenza,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateLinfonodiField("anomalieConsistenza", value))
                    },
                    label = { Text("Consistenza") },
                    placeholder = { Text("es. molle, dura, elastica") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
        
        // General notes
        OutlinedTextField(
            value = state.note,
            onValueChange = { value ->
                onEvent(AddVisitEvent.UpdateLinfonodiField("note", value))
            },
            label = { Text("Note") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
        )
    }
}


