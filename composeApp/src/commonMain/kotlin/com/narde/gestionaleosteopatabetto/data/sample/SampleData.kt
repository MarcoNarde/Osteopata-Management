package com.narde.gestionaleosteopatabetto.data.sample

import com.narde.gestionaleosteopatabetto.data.model.Patient
import com.narde.gestionaleosteopatabetto.data.model.Visit
import com.narde.gestionaleosteopatabetto.data.model.VisitStatus
// Sample data for testing

/**
 * Sample data for demonstration purposes
 * Contains mock data for patients and visits to test the UI
 */
object SampleData {
    
    /**
     * Sample patients list for testing the application
     */
    val patients = listOf(
        Patient("1", "Mario Rossi", "+39 123 456 7890", "mario.rossi@email.com", 45, 24.5), // BMI included
        Patient("2", "Anna Bianchi", "+39 098 765 4321", "anna.bianchi@email.com", 32, 21.8), // BMI included
        Patient("3", "Giuseppe Verdi", "+39 555 123 4567", "giuseppe.verdi@email.com", 58), // BMI null
        Patient("4", "Maria Neri", "+39 333 987 6543", "maria.neri@email.com", 29, 26.3) // BMI included
    )
    
    /**
     * Sample visits list for testing the application
     * Using the new Visit model structure
     */
    val visits = listOf(
        Visit(
            idVisita = "VIS_001_2024_01_15",
            idPaziente = "1",
            dataVisita = "2024-01-15",
            osteopata = "Roberto Caeran",
            noteGenerali = "First consultation"
        ),
        Visit(
            idVisita = "VIS_002_2024_01_15",
            idPaziente = "2", 
            dataVisita = "2024-01-15",
            osteopata = "Roberto Caeran",
            noteGenerali = "Follow-up session"
        ),
        Visit(
            idVisita = "VIS_003_2024_01_16",
            idPaziente = "3",
            dataVisita = "2024-01-16", 
            osteopata = "Roberto Caeran",
            noteGenerali = "Treatment session"
        ),
        Visit(
            idVisita = "VIS_004_2024_01_16",
            idPaziente = "4",
            dataVisita = "2024-01-16",
            osteopata = "Roberto Caeran", 
            noteGenerali = "Initial assessment"
        )
    )
} 