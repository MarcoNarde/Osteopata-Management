package com.narde.gestionaleosteopatabetto

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.narde.gestionaleosteopatabetto.ui.OsteopathManagementApp
import com.narde.gestionaleosteopatabetto.ui.theme.OsteopathTealTheme

/**
 * Main application entry point
 * Sets up the custom Teal Theme and loads patients from database
 */
@Composable
@Preview
fun App() {
    OsteopathTealTheme {
        // Main application surface with tabs using the new teal color scheme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Main osteopath management application with database integration
            OsteopathManagementApp()
        }
    }
}