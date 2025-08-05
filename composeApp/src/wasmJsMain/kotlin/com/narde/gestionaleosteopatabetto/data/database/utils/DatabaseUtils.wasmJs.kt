package com.narde.gestionaleosteopatabetto.data.database.utils

import com.narde.gestionaleosteopatabetto.data.database.models.*

/**
 * WASM stub implementation of DatabaseUtils
 * Provides basic functionality without database persistence
 */
class WasmDatabaseUtils : DatabaseUtilsInterface {
    override fun createSamplePatient(): Patient {
        return Patient(
            id = "WASM_SAMPLE_001",
            createdAt = 0L,
            lastModified = 0L,
            datiPersonali = DatiPersonali(
                nome = "Marco",
                cognome = "Rossi",
                codiceFiscale = "RSSMRC80A01H501X",
                dataNascita = "01/01/1980",
                luogoNascita = "Roma",
                sesso = "M",
                telefono = "123-456-7890",
                email = "marco.rossi@example.com"
            ),
            indirizzo = Indirizzo(
                via = "Via Roma 123",
                citta = "Roma",
                provincia = "RM",
                cap = "00100"
            )
        )
    }
    
    override fun createNewPatient(patientId: String): Patient {
        return Patient(
            id = patientId,
            createdAt = 0L,
            lastModified = 0L
        )
    }
    
    override fun generatePatientId(existingPatientCount: Long): String {
        return "WASM_PAT_${(existingPatientCount + 1).toString().padStart(6, '0')}"
    }
}

/**
 * WASM factory function
 */
actual fun createDatabaseUtils(): DatabaseUtilsInterface = WasmDatabaseUtils()