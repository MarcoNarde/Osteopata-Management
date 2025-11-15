package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmObject

/**
 * Symptom with intensity pattern - boolean presence with VAS scale (0-10) and optional note
 * Used for symptoms that require intensity measurement using Visual Analog Scale
 */
class SintomoIntensita : RealmObject {
    var presente: Boolean = false
    var intensitaVas: Int = 0 // Range: 0-10 (Visual Analog Scale)
    var note: String = ""
}

