package com.narde.gestionaleosteopatabetto.data.database

actual fun isDatabaseSupported(): Boolean = true

actual fun getDatabaseStatusMessage(): String = "Realm database fully supported on Android"
