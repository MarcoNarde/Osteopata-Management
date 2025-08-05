package com.narde.gestionaleosteopatabetto.data.database.models

import com.narde.gestionaleosteopatabetto.data.database.repository.PatientRepository
import com.narde.gestionaleosteopatabetto.data.database.repository.PatientRepositoryInterface

/**
 * Factory function to create PatientRepository instance
 */
fun createPatientRepository(): PatientRepositoryInterface {
    return PatientRepository()
}