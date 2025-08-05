package com.narde.gestionaleosteopatabetto.ui

import platform.Foundation.NSUserDefaults
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun changeLanguage(languageCode: String) {
    NSUserDefaults.standardUserDefaults
        .setObject(arrayListOf(languageCode), "AppleLanguages")
}

actual fun getCurrentLanguage(): String {
    return NSLocale.currentLocale.languageCode ?: "en"
}