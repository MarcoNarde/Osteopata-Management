package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Main consultation reason model for Realm
 * Contains detailed information about the primary reason for consultation
 */
class MotivoPrincipale : RealmObject {
    var descrizione: String = ""
    var insorgenza: String = "" // Onset date or description
    var dolore: String = "" // Pain description
    var vas: Int = 0 // Visual Analog Score: 0-10
    var fattori: String = "" // Contributing factors
}
