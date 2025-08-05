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
        Patient("1", "Mario Rossi", "+39 123 456 7890", "mario.rossi@email.com", 45),
        Patient("2", "Anna Bianchi", "+39 098 765 4321", "anna.bianchi@email.com", 32),
        Patient("3", "Giuseppe Verdi", "+39 555 123 4567", "giuseppe.verdi@email.com", 58),
        Patient("4", "Maria Neri", "+39 333 987 6543", "maria.neri@email.com", 29)
    )
    
    /**
     * Sample visits list for testing the application
     */
    val visits = listOf(
        Visit("1", "Mario Rossi", "2024-01-15", "09:00", VisitStatus.SCHEDULED, "First consultation"),
        Visit("2", "Anna Bianchi", "2024-01-15", "10:30", VisitStatus.COMPLETED, "Follow-up session"),
        Visit("3", "Giuseppe Verdi", "2024-01-16", "14:00", VisitStatus.SCHEDULED, "Treatment session"),
        Visit("4", "Maria Neri", "2024-01-16", "16:00", VisitStatus.SCHEDULED, "Initial assessment")
    )
} 