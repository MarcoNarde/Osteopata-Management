package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Nervous apparatus evaluation model
 * Divided into 3 main subsystems: central, vegetative (autonomic), and peripheral
 * Contains evaluation of CNS pathologies, autonomic nervous system problems,
 * peripheral neuropathies, and clinical neurological assessment
 * NOTE: Headache and migraine are in cranio.json (avoid duplication)
 */
class ApparatoNervoso : RealmObject {
    // Central nervous system
    var sistemaCentrale: SistemaNervosoCentrale? = null
    
    // Vegetative (autonomic) nervous system
    var sistemaVegetativo: SistemaNervosoVegetativo? = null
    
    // Peripheral nervous system
    var sistemaPeriferico: SistemaNervosoPeriferico? = null
    
    // Clinical neurological assessment (objective examination)
    var valutazioneClinica: ValutazioneNeurologica? = null
    
    // General notes
    var note: String = ""
}

/**
 * Central nervous system evaluation
 */
class SistemaNervosoCentrale : RealmObject {
    // CNS pathologies
    var patologie: PatologieSNC? = null
    
    // Reference to headache/migraine (in cranio.json and psico_neuro)
    // Not duplicated here, use reference in business logic
}

/**
 * CNS pathologies
 */
class PatologieSNC : RealmObject {
    var epilessia: Boolean = false // ALERT if true
    var ictus: Ictus? = null
    var tia: Boolean = false // Transient ischemic attack
    var sclerosiMultipla: Boolean = false
    var parkinson: Boolean = false
    var altrePatologie: String = "" // Other pathologies
}

/**
 * Stroke evaluation
 */
class Ictus : RealmObject {
    var presente: Boolean = false
    var data: String = "" // Format: YYYY-MM-DD
    var tipo: String = "" // Values: "ischemico", "emorragico"
    var esiti: String = "" // Sequelae
}

/**
 * Vegetative (autonomic) nervous system evaluation
 */
class SistemaNervosoVegetativo : RealmObject {
    var problemiPresenti: Boolean = false // Main flag
    var sintomi: SintomiVegetativi? = null
}

/**
 * Autonomic nervous system symptoms
 */
class SintomiVegetativi : RealmObject {
    var ipotensioneOrtostatica: Boolean = false // Important
    var sudorazioneAnomala: Boolean = false
    var problemiTermoregolazione: Boolean = false
    var disturbiVescicali: Boolean = false
    var disturbiIntestinali: Boolean = false
    var variabilitaFrequenzaCardiaca: Boolean = false
}

/**
 * Peripheral nervous system evaluation
 */
class SistemaNervosoPeriferico : RealmObject {
    // Neuropathies
    var neuropatie: Neuropatie? = null
    
    // Paresthesias (sensitivity alterations)
    var parestesie: Parestesie? = null
    
    // Hypoesthesia (reduced sensitivity)
    var ipoestesie: Ipoestesie? = null
    
    // Motor deficits
    var deficitMotori: DeficitMotori? = null
    
    // Radiculopathies (nerve root compression)
    var radicolopatie: Radicolopatie? = null
    
    // Carpal tunnel syndrome (common pathology)
    var tunnelCarpale: TunnelCarpale? = null
}

/**
 * Neuropathies evaluation
 */
class Neuropatie : RealmObject {
    var presente: Boolean = false
    var tipo: String = "" // Values: "diabetica", "alcolica", "compressiva"
    var localizzazione: RealmList<String> = realmListOf()
    var inTrattamento: Boolean = false
}

/**
 * Paresthesias (sensitivity alterations)
 */
class Parestesie : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
    var tipo: String = "" // Values: "formicolio", "bruciore", "punture"
    var costante: Boolean = false
    var posizionale: Boolean = false // Important for diagnosis
    var notturna: Boolean = false
}

/**
 * Hypoesthesia (reduced sensitivity)
 */
class Ipoestesie : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
    var distribuzione: String = "" // Values: "dermatomerica", "periferica", "a_guanto"
}

/**
 * Motor deficits
 */
class DeficitMotori : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
    var grado: Int = 0 // Muscle strength scale 0-5
    var acutoOCronico: String = "" // Values: "acuto", "cronico"
}

/**
 * Radiculopathies (nerve root compression)
 */
class Radicolopatie : RealmObject {
    var presente: Boolean = false
    var livello: String = "" // Values: "C5", "L5", "S1"
    var lato: String = "" // Values: "destro", "sinistro", "bilaterale"
    var sintomi: RealmList<String> = realmListOf() // e.g., ["dolore", "debolezza", "parestesie"]
}

/**
 * Carpal tunnel syndrome (common pathology)
 */
class TunnelCarpale : RealmObject {
    var presente: Boolean = false
    var lato: String = "" // Values: "destro", "sinistro", "bilaterale"
    var gravita: String = "" // Values: "lieve", "moderata", "grave"
    var testPositivi: RealmList<String> = realmListOf() // e.g., ["Tinel", "Phalen"]
}

/**
 * Clinical neurological assessment (objective examination)
 */
class ValutazioneNeurologica : RealmObject {
    // Reflexes
    var riflessi: Riflessi? = null
    
    // Sensitivity
    var sensibilita: Sensibilita? = null
    
    // Coordination
    var coordinazione: Coordinazione? = null
}

/**
 * Reflexes evaluation
 */
class Riflessi : RealmObject {
    var stato: String = "" // Values: "normali", "aumentati", "diminuiti", "assenti"
    var dettagli: String = "" // Specific details for each reflex
}

/**
 * Sensitivity evaluation
 */
class Sensibilita : RealmObject {
    var tattile: String = "" // Values: "normale", "alterata", "assente"
    var dolorifica: String = "" // Pain sensitivity
    var termica: String = "" // Thermal sensitivity
    var propriocettiva: String = "" // Proprioceptive sensitivity
    var vibratoria: String = "" // Vibratory sensitivity
    var note: String = ""
}

/**
 * Coordination evaluation
 */
class Coordinazione : RealmObject {
    var stato: String = "" // Values: "normale", "alterata"
    var atassia: Boolean = false
    var dismetria: Boolean = false
    var testEseguiti: RealmList<String> = realmListOf() // e.g., ["dito-naso", "tallone-ginocchio"]
}

