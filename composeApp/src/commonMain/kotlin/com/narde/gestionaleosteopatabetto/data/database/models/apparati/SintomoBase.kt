package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmObject

/**
 * Base symptom pattern - simple boolean presence with optional note
 * Used for symptoms that only need a yes/no answer and optional description
 */
class SintomoBase : RealmObject {
    var presente: Boolean = false
    var note: String = ""
}

