package com.narde.gestionaleosteopatabetto.data.database.utils

import com.narde.gestionaleosteopatabetto.data.database.models.*

/**
 * Compatibility extensions to support legacy snake_case property access
 * This allows old code to work with new camelCase properties
 */

// Patient extensions
val Patient.dati_personali: DatiPersonali? get() = datiPersonali
val Patient.id_paziente: String get() = idPaziente
val Patient.medico_curante: MedicoCurante? get() = medicocurante

// DatiPersonali extensions  
val DatiPersonali.data_nascita: String get() = dataNascita
val DatiPersonali.luogo_nascita: String get() = luogoNascita
val DatiPersonali.codice_fiscale: String get() = codiceFiscale
val DatiPersonali.telefono_paziente: String get() = telefonoPaziente
val DatiPersonali.email_paziente: String get() = emailPaziente
val DatiPersonali.stato_maritale: String get() = statoMaritale

// Indirizzo extensions
val Indirizzo.tipo_indirizzo: String get() = tipoIndirizzo

// Privacy extensions
val Privacy.consenso_trattamento: Boolean get() = consensoTrattamento
val Privacy.consenso_marketing: Boolean get() = consensoMarketing  
val Privacy.consenso_terze_parti: Boolean get() = consensoTerzeparti
val Privacy.data_consenso: String get() = dataConsenso
val Privacy.note_privacy: String get() = notePrivacy

// Writable extensions for ViewModels (these would need to be var properties)
fun Patient.updateLastModified() {
    // Placeholder for last modified functionality if needed
}