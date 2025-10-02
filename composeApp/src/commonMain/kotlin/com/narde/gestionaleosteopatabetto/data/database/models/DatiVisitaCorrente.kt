package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Current visit data model for Realm
 * Contains patient's current physical measurements and assessments
 */
class DatiVisitaCorrente : RealmObject {
    var peso: Double = 0.0 // Weight in kilograms
    var bmi: Double = 0.0 // Body Mass Index
    var pressione: String = "" // Blood pressure (e.g. "120/80")
    var indiciCraniali: Double = 0.0 // Cranial indices value
}
