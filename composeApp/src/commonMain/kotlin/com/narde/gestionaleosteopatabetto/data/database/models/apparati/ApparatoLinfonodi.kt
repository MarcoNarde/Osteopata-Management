package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmObject

/**
 * Lymph nodes apparatus evaluation model
 * Simple apparatus with few fields
 * Contains evaluation of lymph node anomalies with localization details
 */
class ApparatoLinfonodi : RealmObject {
    // Lymph node anomalies
    var anomalie: AnomalieLinfonodi? = null
    
    // General notes
    var note: String = ""
}

/**
 * Lymph node anomalies evaluation
 * If presente == true, details should be filled
 */
class AnomalieLinfonodi : RealmObject {
    var presente: Boolean = false // Main flag
    
    // Details (to be filled if presente == true)
    var dettagli: DettagliLinfonodi? = null
}

/**
 * Lymph node anomaly details
 */
class DettagliLinfonodi : RealmObject {
    var localizzazione: String = "" // e.g., "cervicale", "ascellare", "inguinale"
    var dimensione: String = "" // e.g., "piccoli", "medi", "grandi" or size in cm
    var mobilita: Boolean = false // true = mobile (benign), false = fixed (suspicious)
    var dolenti: Boolean = false // Painful
    var consistenza: String = "" // Values: "molle", "dura", "elastica"
}

