package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Localization pattern - for localized pain or symptoms
 * Used to describe pain or symptoms in specific anatomical locations
 */
class Localizzazione : RealmObject {
    var zona: String = "" // Anatomical zone/area
    var lato: String = "" // Values: "destro", "sinistro", "bilaterale"
    var intensitaVas: Int = 0 // Range: 0-10 (Visual Analog Scale)
    var tipo: String = "" // Type of sensation (e.g., "superficiale", "profondo", "lancinante", "urente")
    var irradiazione: String = "" // Radiation pattern (where the pain/symptom extends)
    var peggioramento: RealmList<String> = realmListOf() // Factors that worsen the condition
    var miglioramento: RealmList<String> = realmListOf() // Factors that improve the condition
}

