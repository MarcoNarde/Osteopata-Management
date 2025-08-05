package com.narde.gestionaleosteopatabetto.data.database.models

import com.narde.gestionaleosteopatabetto.data.database.repository.PatientRepositoryInterface

/**
 * WASM implementation - returns null since database is not supported
 */
actual fun createPatientRepository(): PatientRepositoryInterface? = null