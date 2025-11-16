package com.narde.gestionaleosteopatabetto.ui.viewmodels

import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.model.Visit

/**
 * UI display model for visits with enriched patient information
 * Follows clean architecture principles by separating UI concerns from data models
 * 
 * This model enriches the Visit data model with patient name for display purposes
 * without modifying the original Visit model, maintaining separation of concerns
 */
data class VisitDisplayItem(
    val visit: Visit,
    val patientName: String
) {
    /**
     * Convenience property to access visit ID
     */
    val idVisita: String
        get() = visit.idVisita
    
    /**
     * Convenience property to access patient ID
     */
    val idPaziente: String
        get() = visit.idPaziente
}

/**
 * Extension function to convert list of visits to display items
 * Maps visits with their corresponding patient names from the patients list
 * 
 * @param patients List of patients to match against visit patient IDs
 * @return List of VisitDisplayItem with enriched patient name information
 * 
 * If a patient is not found for a visit, falls back to displaying the patient ID
 */
fun List<Visit>.toDisplayItems(patients: List<Patient>): List<VisitDisplayItem> {
    return this.map { visit ->
        // Find matching patient by ID
        val patient = patients.find { it.id == visit.idPaziente }
        
        // Use patient name if found, otherwise fallback to patient ID
        val patientName = patient?.name?.takeIf { it.isNotBlank() } 
            ?: "Paziente: ${visit.idPaziente}"
        
        VisitDisplayItem(
            visit = visit,
            patientName = patientName
        )
    }
}
