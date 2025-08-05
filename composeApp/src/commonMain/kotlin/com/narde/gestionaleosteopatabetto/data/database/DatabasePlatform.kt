package com.narde.gestionaleosteopatabetto.data.database

/**
 * Platform-specific database availability check
 * Returns true if Realm database is supported on current platform
 */
expect fun isDatabaseSupported(): Boolean

/**
 * Platform-specific database initialization message
 * Returns appropriate message based on platform support
 */
expect fun getDatabaseStatusMessage(): String