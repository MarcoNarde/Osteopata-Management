package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Pharmacological therapy model for patients in Realm
 * Contains information about current and past medications,
 * including dosage, frequency, and treatment duration
 */
class TerapiaFarmacologica : RealmObject {
    var farmaco: String = ""
    var dosaggio: String = "" // e.g., "5mg", "10mg/5ml"
    var frequenza: String = "" // e.g., "1/die", "2/die", "al_bisogno"
    var dataInizio: String = "" // Format: YYYY-MM-DD
    var dataFine: String? = null // Format: YYYY-MM-DD, null if ongoing
    var indicazione: String = "" // Reason for prescription
} 