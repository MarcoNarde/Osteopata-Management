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
    
    /**
     * Create clinical history data from JSON structure
     * This function creates a sample clinical history based on the provided JSON structure
     */
    override fun createSampleClinicalHistory(): StoriaClinica {
        return StoriaClinica().apply {
            // Chronic conditions
            patologieCroniche = PatologieCroniche().apply {
                allergieFarmaci = AllergieFarmaci().apply {
                    presente = true
                    // Note: In a real Realm implementation, this would be initialized by the database
                    // For sample data, we'll leave it as null and handle it in the actual database operations
                }
                diabete = Diabete().apply {
                    presente = true
                    tipologia = "2"
                }
                ipertiroidismo = false
                cardiopatia = false
                ipertensioneArteriosa = true
                
                // Note: In a real Realm implementation, these lists would be initialized by the database
                // For sample data, we'll leave them as null and handle them in the actual database operations
            }
            
            // Lifestyle factors
            stileVita = StileVita().apply {
                tabagismo = Tabagismo().apply {
                    stato = "<10"
                    sigaretteGiorno = 5
                    anniFumo = 3
                }
                lavoro = "sedentario"
                professione = "Programmatore informatico"
                oreLavoroGiorno = 8
                attivitaSportiva = AttivitaSportiva().apply {
                    presente = true
                    // Note: In a real Realm implementation, the sport list would be initialized by the database
                    // For sample data, we'll leave it as null and handle it in the actual database operations
                    frequenza = "settimanale"
                    intensita = "media"
                }
            }
            
            // Note: In a real Realm implementation, these lists would be initialized by the database
            // For sample data, we'll leave them as null and handle them in the actual database operations
            
            // Pediatric history
            anamnesiPediatrica = AnamnesiPediatrica().apply {
                gravidanza = Gravidanza().apply {
                    complicazioni = false
                    note = ""
                }
                parto = Parto().apply {
                    tipo = "naturale"
                    complicazioni = false
                    pesoNascitaGrammi = 3200
                    punteggioApgar5min = 9
                    note = ""
                }
                sviluppo = Sviluppo().apply {
                    primiPassiMesi = 12
                    primeParoleMesi = 11
                    problemiSviluppo = false
                    note = ""
                }
                
                // Note: In a real Realm implementation, this list would be initialized by the database
                // For sample data, we'll leave it as null and handle it in the actual database operations
                
                noteGenerali = "Sviluppo nella norma, nessuna particolare problematica"
            }
        }
    }
}

/**
 * Factory function to create DatabaseUtils instance
 */
fun createDatabaseUtils(): DatabaseUtilsInterface {
    return DatabaseUtils()
}