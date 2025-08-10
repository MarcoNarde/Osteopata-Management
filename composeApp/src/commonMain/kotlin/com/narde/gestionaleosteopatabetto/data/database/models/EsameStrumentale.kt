package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Diagnostic tests and examinations model for patients in Realm
 * Contains information about imaging studies, laboratory tests,
 * and other diagnostic procedures
 */
class EsameStrumentale : RealmObject {
    var id: String = "" // Unique identifier for the examination
    var data: String = "" // Format: YYYY-MM-DD
    var tipo: String = "" // "RMN", "TAC", "RX", "ecografia", "esami_ematochimici", etc.
    var distretto: String = "" // Body area examined
    var risultato: String = "" // Test results and findings
    var struttura: String = "" // Healthcare facility where the test was performed
} 