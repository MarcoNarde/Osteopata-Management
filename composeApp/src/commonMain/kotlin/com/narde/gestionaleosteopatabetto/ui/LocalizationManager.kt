package com.narde.gestionaleosteopatabetto.ui

import androidx.compose.runtime.*

/**
 * Expect/actual functions for language management across platforms
 */
expect fun changeLanguage(languageCode: String)
expect fun getCurrentLanguage(): String

/**
 * Composable to manage current language state across the app
 */
@Composable
fun rememberLanguageState(): MutableState<String> {
    return remember { mutableStateOf(getCurrentLanguage()) }
}

/**
 * Available languages in the app
 */
enum class SupportedLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    ITALIAN("it", "Italiano")
}