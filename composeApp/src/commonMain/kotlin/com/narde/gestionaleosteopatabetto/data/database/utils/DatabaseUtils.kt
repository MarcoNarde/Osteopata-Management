package com.narde.gestionaleosteopatabetto.data.database.utils

import com.narde.gestionaleosteopatabetto.data.database.models.*
import com.narde.gestionaleosteopatabetto.data.model.Patient as UIPatient
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Database utility operations implementation
 */
class DatabaseUtils : DatabaseUtilsInterface {
    
    /**
     * Create a sample patient for testing/development
     */
    override fun createSamplePatient(): Patient {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        
        return Patient().apply {
            idPaziente = "PAT001"
            datiPersonali = DatiPersonali().apply {
                nome = "Mario"
                cognome = "Rossi"
                dataNascita = "1985-06-15" // Database stores in ISO format
                sesso = "M"
                luogoNascita = "Roma"
                codiceFiscale = "RSSMRA85H15H501Z"
                telefonoPaziente = "+39 333 123 4567"
                emailPaziente = "mario.rossi@email.com"
            }
            indirizzo = Indirizzo().apply {
                via = "Via Roma 123"
                citta = "Roma"
                cap = "00100"
                provincia = "RM"
                nazione = "Italia"
                tipoIndirizzo = "Residenza"
            }
            privacy = Privacy().apply {
                consensoTrattamento = true
                consensoMarketing = false
                consensoTerzeparti = false
                dataConsenso = currentDate.toString()
            }
        }
    }
    
    /**
     * Generate a patient ID based on patient count
     */
    override fun generatePatientId(patientCount: Long): String {
        val nextId = patientCount + 1
        return "PAT${nextId.toString().padStart(3, '0')}"
    }
    
    /**
     * Create a new empty patient with given ID
     */
    override fun createNewPatient(patientId: String): Patient {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        
        return Patient().apply {
            this.idPaziente = patientId
            datiPersonali = DatiPersonali()
            indirizzo = Indirizzo()
            privacy = Privacy().apply {
                dataConsenso = currentDate.toString()
            }
            genitori = Genitori().apply {
                padre = Padre()
                madre = Madre()
            }
            medicocurante = MedicoCurante()
        }
    }
    
    /**
     * Convert database patient to UI patient
     */
    override fun toUIPatient(databasePatient: Patient): UIPatient {
        // Calculate age from birth date (database stores in ISO format)
        val age = databasePatient.datiPersonali?.dataNascita?.takeIf { it.isNotEmpty() }?.let { birthDateStr ->
            try {
                // Database stores dates in ISO format, so parse directly
                val birthDate = LocalDate.parse(birthDateStr)
                val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                
                var calculatedAge = currentDate.year - birthDate.year
                if (currentDate.monthNumber < birthDate.monthNumber || 
                    (currentDate.monthNumber == birthDate.monthNumber && currentDate.dayOfMonth < birthDate.dayOfMonth)) {
                    calculatedAge--
                }
                calculatedAge
            } catch (e: Exception) {
                0
            }
        } ?: 0
        
        return UIPatient(
            id = databasePatient.idPaziente,
            name = "${databasePatient.datiPersonali?.nome ?: ""} ${databasePatient.datiPersonali?.cognome ?: ""}".trim(),
            phone = databasePatient.datiPersonali?.telefonoPaziente ?: "",
            email = databasePatient.datiPersonali?.emailPaziente ?: "",
            age = age,
            bmi = databasePatient.datiPersonali?.bmi
        )
    }
}

/**
 * Factory function to create DatabaseUtils instance
 */
fun createDatabaseUtils(): DatabaseUtilsInterface {
    return DatabaseUtils()
}