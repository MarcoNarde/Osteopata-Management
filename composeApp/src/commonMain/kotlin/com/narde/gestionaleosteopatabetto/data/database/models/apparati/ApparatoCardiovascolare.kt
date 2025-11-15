package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Cardiovascular apparatus evaluation model
 * Contains evaluation of cardiovascular functions: chest pain, cardiac history,
 * hypertension, edema, varicose veins, arrhythmias, and positional paresthesias
 */
class ApparatoCardiovascolare : RealmObject {
    // Chest pain evaluation
    var doloreToracico: DoloreToracico? = null
    
    // Cardiac history
    var storiaCardiaca: StoriaCardiaca? = null
    
    // Hypertension evaluation
    var ipertensione: Ipertensione? = null
    
    // Edema evaluation
    var edemi: Edemi? = null
    
    // Varicose veins evaluation
    var veneVaricose: VeneVaricose? = null
    
    // Arrhythmias evaluation
    var aritmie: Aritmie? = null
    
    // Positional paresthesias (important for vascular evaluation)
    var formicoliiPosizionali: FormicoliiPosizionali? = null
}

/**
 * Chest pain evaluation
 */
class DoloreToracico : RealmObject {
    var presente: Boolean = false
    var sottoSforzo: Boolean = false // On exertion
    var aRiposo: Boolean = false // At rest
    var irradiazione: String = "" // e.g., "braccio_sinistro", "mandibola"
}

/**
 * Cardiac history
 */
class StoriaCardiaca : RealmObject {
    var attaccoCardiaco: Boolean = false
    var soffi: Boolean = false // Heart murmurs
    var interventi: RealmList<InterventoCardiaco> = realmListOf()
}

/**
 * Cardiac intervention
 */
class InterventoCardiaco : RealmObject {
    var data: String = "" // Format: YYYY-MM-DD
    var tipo: String = "" // Type of intervention
    var esito: String = "" // Outcome
}

/**
 * Hypertension evaluation (extends StoriaPatologia pattern)
 */
class Ipertensione : RealmObject {
    var presente: Boolean = false
    var inTerapia: Boolean = false
    var valoriMedi: String = "" // e.g., "140/90"
    var misurazioneDomiciliare: Boolean = false
}

/**
 * Edema evaluation (localized symptom)
 */
class Edemi : RealmObject {
    var presente: Boolean = false
    var localizzazione: String = "" // Values: "caviglie", "piedi", "gambe"
    var bilaterale: Boolean = false
    var serale: Boolean = false // Important for diagnosis (worse in evening)
    var mattutino: Boolean = false
}

/**
 * Varicose veins evaluation
 */
class VeneVaricose : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf() // List of locations
    var sintomatiche: Boolean = false
}

/**
 * Arrhythmias evaluation
 */
class Aritmie : RealmObject {
    var presente: Boolean = false
    var palpitazioni: Boolean = false
    var aRiposo: Boolean = false
    var sottoSforzo: Boolean = false
    var polsoIrregolare: Boolean = false
}

/**
 * Positional paresthesias (important for vascular evaluation)
 */
class FormicoliiPosizionali : RealmObject {
    var presente: Boolean = false
    var mani: Boolean = false
    var piedi: Boolean = false
    var posizioniSpecifiche: RealmList<String> = realmListOf() // Specific positions that trigger
}

