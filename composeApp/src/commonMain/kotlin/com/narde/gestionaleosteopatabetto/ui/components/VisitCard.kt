package com.narde.gestionaleosteopatabetto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.data.model.Visit
import com.narde.gestionaleosteopatabetto.data.model.VisitStatus
import org.jetbrains.compose.resources.stringResource
import gestionaleosteopatabetto.composeapp.generated.resources.Res
import gestionaleosteopatabetto.composeapp.generated.resources.*

/**
 * Card component to display visit information
 * Shows visit date, patient, and status with visual indicators
 */
@Composable
fun VisitCard(visit: Visit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Visit status icon with color
            Card(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (visit.status) {
                        VisitStatus.SCHEDULED -> MaterialTheme.colorScheme.tertiaryContainer
                        VisitStatus.COMPLETED -> MaterialTheme.colorScheme.primaryContainer
                        VisitStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Visit Icon",
                        tint = when (visit.status) {
                            VisitStatus.SCHEDULED -> MaterialTheme.colorScheme.onTertiaryContainer
                            VisitStatus.COMPLETED -> MaterialTheme.colorScheme.onPrimaryContainer
                            VisitStatus.CANCELLED -> MaterialTheme.colorScheme.onErrorContainer
                        },
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Visit information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Patient name as the main title
                Text(
                    text = visit.patientName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Visit details in a cleaner layout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.visit_date, visit.date),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(Res.string.visit_time, visit.time),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // Visit status chip
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = when (visit.status) {
                                                VisitStatus.SCHEDULED -> stringResource(Res.string.status_scheduled)
            VisitStatus.COMPLETED -> stringResource(Res.string.status_completed)
            VisitStatus.CANCELLED -> stringResource(Res.string.status_cancelled)
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = when (visit.status) {
                                VisitStatus.SCHEDULED -> MaterialTheme.colorScheme.tertiaryContainer
                                VisitStatus.COMPLETED -> MaterialTheme.colorScheme.primaryContainer
                                VisitStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
                            },
                            labelColor = when (visit.status) {
                                VisitStatus.SCHEDULED -> MaterialTheme.colorScheme.onTertiaryContainer
                                VisitStatus.COMPLETED -> MaterialTheme.colorScheme.onPrimaryContainer
                                VisitStatus.CANCELLED -> MaterialTheme.colorScheme.onErrorContainer
                            }
                        )
                    )
                }
                
                if (visit.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(Res.string.visit_notes, visit.notes),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
} 