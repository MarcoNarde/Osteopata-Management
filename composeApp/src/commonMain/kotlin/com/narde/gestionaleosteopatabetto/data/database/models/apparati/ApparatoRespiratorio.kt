package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Respiratory apparatus evaluation model
 * Contains evaluation of respiratory functions: dyspnea, cough, allergies,
 * sinusitis, snoring, and other respiratory symptoms
 */
class ApparatoRespiratorio : RealmObject {
    // Dyspnea (shortness of breath) evaluation
    var dispnea: Dispnea? = null
    
    // Simple boolean symptoms
    var oppressioneToracica: Boolean = false
    
    // Cough evaluation
    var tosse: Tosse? = null
    
    // Respiratory allergies
    var allergieRespiratorie: AllergieRespiratorie? = null
    
    // Simple boolean symptoms
    var raucedine: Boolean = false
    
    // Nasal congestion
    var congestioneNasale: CongestioneNasale? = null
    
    // Sinusitis evaluation
    var sinusite: Sinusite? = null
    
    // Snoring evaluation
    var russare: Russare? = null
    
    // Simple boolean symptoms
    var brucioreGola: Boolean = false
    
    // Frequent colds
    var raffreddoriFrequenti: RaffreddoriFrequenti? = null
}

/**
 * Dyspnea (shortness of breath) evaluation
 */
class Dispnea : RealmObject {
    var presente: Boolean = false
    var sottoSforzo: Boolean = false // On exertion
    var aRiposo: Boolean = false // At rest
    var notturna: Boolean = false // Nocturnal
}

/**
 * Cough evaluation
 */
class Tosse : RealmObject {
    var presente: Boolean = false
    var tipo: String = "" // Values: "secca", "produttiva", "mista"
    var notturna: Boolean = false
    var cronica: Boolean = false
    var conSangue: Boolean = false // Hemoptysis
}

/**
 * Respiratory allergies evaluation
 */
class AllergieRespiratorie : RealmObject {
    var presente: Boolean = false
    var allergeni: RealmList<String> = realmListOf() // e.g., ["polline_betulla", "graminacee", "acari"]
    var stagionalita: String = "" // e.g., "primavera", "estate", "autunno", "inverno", "tutto_anno"
    var sintomi: RealmList<String> = realmListOf() // e.g., ["starnuti", "lacrimazione", "prurito_naso", "tosse"]
    var terapia: String = "" // Current treatment
}

/**
 * Nasal congestion evaluation
 */
class CongestioneNasale : RealmObject {
    var presente: Boolean = false
    var cronica: Boolean = false
    var stagionale: Boolean = false
    var monolaterale: Boolean = false
}

/**
 * Sinusitis evaluation
 */
class Sinusite : RealmObject {
    var presente: Boolean = false
    var ricorrente: Boolean = false
    var localizzazione: String = "" // Values: "frontale", "mascellare", "etmoidale", "sfenoidale"
    var cronica: Boolean = false
}

/**
 * Snoring evaluation
 */
class Russare : RealmObject {
    var presente: Boolean = false
    var intensita: String = "" // Values: "lieve", "moderata", "grave"
    var frequenza: String = "" // e.g., "ogni_notte", "occasionale"
    var disturbaSonno: Boolean = false
    var apneeNotturne: Boolean = false // Important for diagnosis
}

/**
 * Frequent colds evaluation
 */
class RaffreddoriFrequenti : RealmObject {
    var presente: Boolean = false
    var frequenzaAnno: Int = 0 // Number of colds per year
}

