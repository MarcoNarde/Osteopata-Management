package com.narde.gestionaleosteopatabetto.ui

// For WASM, we'll use a simple state-based approach since we don't have full locale support
private var currentLang = "en"

actual fun changeLanguage(languageCode: String) {
    currentLang = languageCode
}

actual fun getCurrentLanguage(): String {
    return currentLang
}