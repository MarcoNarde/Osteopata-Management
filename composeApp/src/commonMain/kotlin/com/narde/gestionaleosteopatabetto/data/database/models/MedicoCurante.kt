package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Primary care physician model for patients in Realm
 * Contains information about patient's primary doctor
 */
class MedicoCurante : RealmObject {
    var nome: String = ""
    var cognome: String = ""
    var telefono: String = ""
    var email: String = ""
    var indirizzo: String = ""
    var specializzazione: String = ""
    
    // Computed properties for legacy compatibility
    val nomeCognome: String get() = "$nome $cognome".trim()
    val indirizzoStudio: String get() = indirizzo
    
    // Legacy property names for compatibility
    val nome_cognome: String get() = nomeCognome
    val indirizzo_studio: String get() = indirizzoStudio
    val titolo: String get() = "Dott." // Default title
}