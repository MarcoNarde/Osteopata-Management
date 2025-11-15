package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Complete symptom pattern - comprehensive symptom description with all details
 * Used for complex symptoms requiring full documentation (VAS, frequency, duration, characteristics)
 */
class SintomoCompleto : RealmObject {
    var presente: Boolean = false
    var intensitaVas: Int = 0 // Range: 0-10 (Visual Analog Scale)
    var frequenza: String = "" // Values: "raro", "occasionale", "frequente", "costante"
    var durataOre: Int = 0 // Duration in hours
    var caratteristiche: CaratteristicheSintomo? = null
    var note: String = ""
}

/**
 * Symptom characteristics - detailed attributes of a symptom
 */
class CaratteristicheSintomo : RealmObject {
    var tipo: String = "" // Symptom type (e.g., "tensiva", "emicranica", "cluster" for headaches)
    var localizzazione: RealmList<String> = realmListOf() // List of anatomical locations
    var fattoriScatenanti: RealmList<String> = realmListOf() // Triggering factors
    var fattoriAllevianti: RealmList<String> = realmListOf() // Relieving factors
}

