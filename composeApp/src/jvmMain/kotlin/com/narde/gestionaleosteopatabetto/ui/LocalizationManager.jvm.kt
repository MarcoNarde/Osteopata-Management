package com.narde.gestionaleosteopatabetto.ui

import java.util.Locale

actual fun changeLanguage(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
}

actual fun getCurrentLanguage(): String {
    return Locale.getDefault().language
}