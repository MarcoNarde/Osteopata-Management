package com.narde.gestionaleosteopatabetto.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.narde.gestionaleosteopatabetto.ui.SupportedLanguage
import com.narde.gestionaleosteopatabetto.ui.changeLanguage
import com.narde.gestionaleosteopatabetto.ui.rememberLanguageState

/**
 * Language switcher component
 * Allows users to switch between English and Italian using Compose Multiplatform Resources
 */
@Composable
fun LanguageSwitcher() {
    var currentLanguage by rememberLanguageState()
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Language / Lingua:",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // English button
            Button(
                onClick = {
                    changeLanguage(SupportedLanguage.ENGLISH.code)
                    currentLanguage = SupportedLanguage.ENGLISH.code
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentLanguage == SupportedLanguage.ENGLISH.code) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(SupportedLanguage.ENGLISH.displayName)
            }
            
            // Italian button
            Button(
                onClick = {
                    changeLanguage(SupportedLanguage.ITALIAN.code)
                    currentLanguage = SupportedLanguage.ITALIAN.code
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentLanguage == SupportedLanguage.ITALIAN.code) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(SupportedLanguage.ITALIAN.displayName)
            }
        }
    }
}