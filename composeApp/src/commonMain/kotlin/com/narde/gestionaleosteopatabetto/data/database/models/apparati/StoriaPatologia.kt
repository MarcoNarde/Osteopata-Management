package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmObject

/**
 * Medical history pattern - for documented medical conditions
 * Used to track diagnosed conditions with treatment status
 */
class StoriaPatologia : RealmObject {
    var presente: Boolean = false
    var dataDiagnosi: String = "" // Format: YYYY-MM-DD
    var inTrattamento: Boolean = false
    var trattamentoAttuale: String = "" // Current treatment description
    var note: String = ""
}

