package com.narde.gestionaleosteopatabetto.ui.components.apparati.forms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.ui.mvi.AddVisitEvent
import com.narde.gestionaleosteopatabetto.ui.mvi.ApparatoCranioState

/**
 * Form component for Cranial apparatus evaluation
 */
@Composable
fun ApparatoCranioForm(
    state: ApparatoCranioState,
    onEvent: (AddVisitEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Valutazione Apparato Craniale",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        
        // Simple boolean symptoms
        Text(
            text = "Sintomi Base",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CheckboxWithLabel(
                checked = state.problemiOlfatto,
                onCheckedChange = { checked ->
                    onEvent(AddVisitEvent.UpdateCranioField("problemiOlfatto", checked))
                },
                label = "Problemi olfatto"
            )
            
            CheckboxWithLabel(
                checked = state.problemiVista,
                onCheckedChange = { checked ->
                    onEvent(AddVisitEvent.UpdateCranioField("problemiVista", checked))
                },
                label = "Problemi vista"
            )
            
            CheckboxWithLabel(
                checked = state.problemiUdito,
                onCheckedChange = { checked ->
                    onEvent(AddVisitEvent.UpdateCranioField("problemiUdito", checked))
                },
                label = "Problemi udito"
            )
            
            CheckboxWithLabel(
                checked = state.disturbiOcclusali,
                onCheckedChange = { checked ->
                    onEvent(AddVisitEvent.UpdateCranioField("disturbiOcclusali", checked))
                },
                label = "Disturbi occlusali"
            )
            
            CheckboxWithLabel(
                checked = state.malattieParodontali,
                onCheckedChange = { checked ->
                    onEvent(AddVisitEvent.UpdateCranioField("malattieParodontali", checked))
                },
                label = "Malattie parodontali"
            )
            
            CheckboxWithLabel(
                checked = state.linguaDolente,
                onCheckedChange = { checked ->
                    onEvent(AddVisitEvent.UpdateCranioField("linguaDolente", checked))
                },
                label = "Lingua dolente"
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Headache section
        Text(
            text = "Cefalea",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        CheckboxWithLabel(
            checked = state.cefaleaPresente,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateCranioField("cefaleaPresente", checked))
            },
            label = "Cefalea presente"
        )
        
        if (state.cefaleaPresente) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = state.cefaleaIntensitaVas,
                        onValueChange = { value ->
                            onEvent(AddVisitEvent.UpdateCranioField("cefaleaIntensitaVas", value))
                        },
                        label = { Text("VAS (0-10)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = state.cefaleaFrequenza,
                        onValueChange = { value ->
                            onEvent(AddVisitEvent.UpdateCranioField("cefaleaFrequenza", value))
                        },
                        label = { Text("Frequenza") },
                        placeholder = { Text("raro, occasionale...") },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                OutlinedTextField(
                    value = state.cefaleaDurataOre,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateCranioField("cefaleaDurataOre", value))
                    },
                    label = { Text("Durata (ore)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = state.cefaleaTipo,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateCranioField("cefaleaTipo", value))
                    },
                    label = { Text("Tipo") },
                    placeholder = { Text("tensiva, emicranica, cluster") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = state.cefaleaLocalizzazione.joinToString(", "),
                    onValueChange = { value ->
                        val list = value.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        onEvent(AddVisitEvent.UpdateCranioField("cefaleaLocalizzazione", list))
                    },
                    label = { Text("Localizzazione") },
                    placeholder = { Text("frontale, temporale (separati da virgola)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Migraine section
        Text(
            text = "Emicrania",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        CheckboxWithLabel(
            checked = state.emicraniaPresente,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateCranioField("emicraniaPresente", checked))
            },
            label = "Emicrania presente"
        )
        
        if (state.emicraniaPresente) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 32.dp)
            ) {
                CheckboxWithLabel(
                    checked = state.emicraniaConAura,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateCranioField("emicraniaConAura", checked))
                    },
                    label = "Con aura"
                )
                
                OutlinedTextField(
                    value = state.emicraniaFrequenza,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateCranioField("emicraniaFrequenza", value))
                    },
                    label = { Text("Frequenza") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // TMJ section
        Text(
            text = "ATM (Articolazione Temporo-Mandibolare)",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        CheckboxWithLabel(
            checked = state.atmProblemiPresenti,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateCranioField("atmProblemiPresenti", checked))
            },
            label = "Problemi ATM presenti"
        )
        
        if (state.atmProblemiPresenti) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 32.dp)
            ) {
                CheckboxWithLabel(
                    checked = state.atmClickArticolare,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateCranioField("atmClickArticolare", checked))
                    },
                    label = "Click articolare"
                )
                
                CheckboxWithLabel(
                    checked = state.atmDoloreMasticazione,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateCranioField("atmDoloreMasticazione", checked))
                    },
                    label = "Dolore alla masticazione"
                )
                
                CheckboxWithLabel(
                    checked = state.atmLimitazioneApertura,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateCranioField("atmLimitazioneApertura", checked))
                    },
                    label = "Limitazione apertura"
                )
                
                CheckboxWithLabel(
                    checked = state.atmSerramentoDiurno,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateCranioField("atmSerramentoDiurno", checked))
                    },
                    label = "Serramento diurno"
                )
                
                CheckboxWithLabel(
                    checked = state.atmBruxismoNotturno,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateCranioField("atmBruxismoNotturno", checked))
                    },
                    label = "Bruxismo notturno"
                )
                
                CheckboxWithLabel(
                    checked = state.atmDeviazioneMandibolare,
                    onCheckedChange = { checked ->
                        onEvent(AddVisitEvent.UpdateCranioField("atmDeviazioneMandibolare", checked))
                    },
                    label = "Deviazione mandibolare"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Orthodontic appliance section
        Text(
            text = "Apparecchio Ortodontico",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        CheckboxWithLabel(
            checked = state.apparecchioOrtodonticoPortato,
            onCheckedChange = { checked ->
                onEvent(AddVisitEvent.UpdateCranioField("apparecchioOrtodonticoPortato", checked))
            },
            label = "Apparecchio ortodontico portato"
        )
        
        if (state.apparecchioOrtodonticoPortato) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 32.dp)
            ) {
                OutlinedTextField(
                    value = state.apparecchioOrtodonticoPeriodo,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateCranioField("apparecchioOrtodonticoPeriodo", value))
                    },
                    label = { Text("Periodo") },
                    placeholder = { Text("es. adolescenza, infanzia") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = state.apparecchioOrtodonticoEtaInizio,
                        onValueChange = { value ->
                            onEvent(AddVisitEvent.UpdateCranioField("apparecchioOrtodonticoEtaInizio", value))
                        },
                        label = { Text("Età inizio") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = state.apparecchioOrtodonticoEtaFine,
                        onValueChange = { value ->
                            onEvent(AddVisitEvent.UpdateCranioField("apparecchioOrtodonticoEtaFine", value))
                        },
                        label = { Text("Età fine") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                OutlinedTextField(
                    value = state.apparecchioOrtodonticoTipo,
                    onValueChange = { value ->
                        onEvent(AddVisitEvent.UpdateCranioField("apparecchioOrtodonticoTipo", value))
                    },
                    label = { Text("Tipo") },
                    placeholder = { Text("fisso, mobile, invisibile") },
                    modifier = Modifier.fillMaxWidth()
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


