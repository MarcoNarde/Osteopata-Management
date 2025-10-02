package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Secondary consultation reason model for Realm
 * Contains information about optional secondary consultation reasons
 */
class MotivoSecondario : RealmObject {
    var descrizione: String = ""
    var durata: String = "" // Duration description (e.g. "2 weeks")
    var vas: Int = 0 // Visual Analog Score: 0-10
}
