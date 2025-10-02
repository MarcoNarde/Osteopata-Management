package com.narde.gestionaleosteopatabetto.data.sample

import com.narde.gestionaleosteopatabetto.data.model.Visit

/**
 * Sample visit data for testing and development
 * Provides example visit objects that match the required schema
 */
object VisitSampleData {
    
    /**
     * Creates a sample visit with all fields populated
     * Example visit data matching the JSON schema requirements
     */
    fun createSampleVisit(): Visit {
        return Visit(
            idVisita = "VIS_ABC_2024_06_01",
            idPaziente = "PATIENT_1234",
            dataVisita = "2024-06-01",
            osteopata = "Roberto Caeran",
            noteGenerali = "Paziente appare in buone condizioni. Leggero disagio durante la palpazione."
        )
    }
    
    /**
     * Creates a sample visit with only main consultation reason
     * Demonstrates optional secondary reason field
     */
    fun createSampleVisitMainOnly(): Visit {
        return Visit(
            idVisita = "VIS_DEF_2024_06_02",
            idPaziente = "PATIENT_5678",
            dataVisita = "2024-06-02",
            osteopata = "Roberto Caeran",
            noteGenerali = "Prima visita del paziente. Valutazione iniziale completata."
        )
    }
    
    /**
     * Creates sample UI visit data objects
     */
    fun createSampleUiVisit(): Visit {
        return Visit(
            idVisita = "VIS_GHI_2024_06_03",
            idPaziente = "PATIENT_9012",
            dataVisita = "2024-06-03",
            osteopata = "Roberto Caeran",
            noteGenerali = "Paziente sportivo. Valutazione post-traumatica."
        )
    }
    
    /**
     * Validates that sample data matches the required schema
     * @return true if all sample data is valid according to schema
     */
    fun validateSampleData(): Boolean {
        val sampleVisit = createSampleVisit()
        val sampleVisitMainOnly = createSampleVisitMainOnly()
        
        // Validate required fields
        val requiredFieldsValid = listOf(
            sampleVisit.idVisita.isNotBlank(),
            sampleVisit.idPaziente.isNotBlank(),
            sampleVisit.dataVisita.isNotBlank(),
            sampleVisit.osteopata.isNotBlank(),
            sampleVisitMainOnly.idVisita.isNotBlank(),
            sampleVisitMainOnly.idPaziente.isNotBlank(),
            sampleVisitMainOnly.dataVisita.isNotBlank(),
            sampleVisitMainOnly.osteopata.isNotBlank()
        ).all { it }
        
        // Validate visit ID format
        val idFormatValid = listOf(
            sampleVisit.idVisita.matches(Regex("^VIS_[A-Z0-9]{3}_\\d{4}_\\d{2}_\\d{2}$")),
            sampleVisitMainOnly.idVisita.matches(Regex("^VIS_[A-Z0-9]{3}_\\d{4}_\\d{2}_\\d{2}$"))
        ).all { it }
        
        // Validate date format
        val dateFormatValid = listOf(
            sampleVisit.dataVisita.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")),
            sampleVisitMainOnly.dataVisita.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))
        ).all { it }
        
        return requiredFieldsValid && idFormatValid && dateFormatValid
    }
}
