package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmObject

/**
 * Symptom with frequency pattern - boolean presence with frequency enum and optional note
 * Used for symptoms that occur with varying frequency
 */
class SintomoFrequenza : RealmObject {
    var presente: Boolean = false
    var frequenza: String = "" // Values: "raro", "occasionale", "frequente", "costante"
    var note: String = ""
}

