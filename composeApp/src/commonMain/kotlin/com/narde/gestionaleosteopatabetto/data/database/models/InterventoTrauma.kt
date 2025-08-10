package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Past interventions and traumas model for patients in Realm
 * Contains information about surgical procedures, accidents,
 * and other significant medical events
 */
class InterventoTrauma : RealmObject {
    var id: String = "" // Unique identifier for the intervention/trauma
    var data: String = "" // Format: YYYY-MM-DD
    var tipo: String = "" // "trauma", "intervento_chirurgico", "altro"
    var descrizione: String = ""
    var trattamento: String = "" // Treatment received
    var esito: String = "" // Outcome: "guarigione_completa", "sequeli", etc.
} 