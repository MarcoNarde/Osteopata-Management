package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Psycho-neuro-endocrine apparatus evaluation model
 * Complex apparatus divided into 3 macro-areas: psychological, sleep, and neurological
 * Contains evaluation of mood, cognitive functions, stress, sleep quality,
 * and neurological symptoms
 */
class ApparatoPsicoNeuroEndocrino : RealmObject {
    // Psychological aspects (first macro-area)
    var psicologico: AspettiPsicologici? = null
    
    // Sleep (second macro-area)
    var sonno: Sonno? = null
    
    // Neurological (third macro-area)
    var neurologico: Neurologico? = null
}

/**
 * Psychological aspects group
 */
class AspettiPsicologici : RealmObject {
    // Mood subgroup
    var umore: Umore? = null
    
    // Cognitive subgroup
    var cognitivo: Cognitivo? = null
    
    // Stress subgroup
    var stress: Stress? = null
}

/**
 * Mood evaluation
 */
class Umore : RealmObject {
    // Anxiety evaluation (extends SintomoCompleto pattern)
    var ansia: Ansia? = null
    
    // Depression evaluation
    var depressione: Depressione? = null
    
    // Simple boolean symptoms
    var irritabilita: Boolean = false
    var apatia: Boolean = false
}

/**
 * Anxiety evaluation
 */
class Ansia : RealmObject {
    var presente: Boolean = false
    var livello: String = "" // Values: "lieve", "moderato", "grave"
    var durataMesi: Int = 0 // Duration in months
    var interferisceVita: Boolean = false // Interferes with daily life
}

/**
 * Depression evaluation
 */
class Depressione : RealmObject {
    var presente: Boolean = false
    var diagnosiMedica: Boolean = false
    var inTrattamento: Boolean = false
}

/**
 * Cognitive evaluation
 */
class Cognitivo : RealmObject {
    var difficoltaConcentrazione: DifficoltaConcentrazione? = null
    var problemiMemoria: Boolean = false
    var pensieriRicorrenti: Boolean = false
    var pensieriOssessivi: Boolean = false
}

/**
 * Concentration difficulties
 */
class DifficoltaConcentrazione : RealmObject {
    var presente: Boolean = false
    var contesto: String = "" // e.g., "lavoro", "studio", "generale"
    var intensita: String = "" // Values: "lieve", "moderata", "grave"
}

/**
 * Stress evaluation
 */
class Stress : RealmObject {
    var sopraffatto: Boolean = false // Overwhelmed
    var preoccupazioneCostante: PreoccupazioneCostante? = null
    var difficoltaRilassarsi: Boolean = false
}

/**
 * Constant worry
 */
class PreoccupazioneCostante : RealmObject {
    var presente: Boolean = false
    var temi: RealmList<String> = realmListOf() // e.g., ["lavoro", "salute", "famiglia"]
}

/**
 * Sleep evaluation (second macro-area)
 */
class Sonno : RealmObject {
    var qualita: String = "" // Values: "ottima", "buona", "scarsa", "pessima"
    var oreNotte: Double = 0.0 // Hours of sleep per night
    
    // Sleep problems
    var problemi: ProblemiSonno? = null
    
    // Sleep disorders
    var disturbi: DisturbiSonno? = null
}

/**
 * Sleep problems
 */
class ProblemiSonno : RealmObject {
    var difficoltaAddormentarsi: DifficoltaAddormentarsi? = null
    var risvegliNotturni: RisvegliNotturni? = null
    var risveglioPrecoce: Boolean = false
}

/**
 * Difficulty falling asleep
 */
class DifficoltaAddormentarsi : RealmObject {
    var presente: Boolean = false
    var tempoMedioMinuti: Int = 0 // Average time in minutes to fall asleep
}

/**
 * Nocturnal awakenings
 */
class RisvegliNotturni : RealmObject {
    var presente: Boolean = false
    var numero: Int = 0 // Number of awakenings per night
    var difficoltaRiaddormentarsi: Boolean = false
}

/**
 * Sleep disorders
 */
class DisturbiSonno : RealmObject {
    var sonnambulismo: Boolean = false
    var incubi: Boolean = false
    var apnee: Boolean = false
    var memoriaSogni: Boolean = false
}

/**
 * Neurological evaluation (third macro-area)
 */
class Neurologico : RealmObject {
    // Serious symptoms (important flags)
    var sintomiGravi: SintomiGravi? = null
    
    // Sensory alterations
    var alterazioniSensoriali: AlterazioniSensoriali? = null
    
    // Balance and coordination
    var equilibrioCoordinazione: EquilibrioCoordinazione? = null
    
    // Other neurological symptoms
    var altro: AltriSintomiNeurologici? = null
}

/**
 * Serious neurological symptoms (ALERT flags)
 */
class SintomiGravi : RealmObject {
    var convulsioni: Boolean = false // ALERT if true
    var svenimenti: Boolean = false
    var blackout: Boolean = false
    var attacchiEpilettici: Boolean = false // ALERT if true
}

/**
 * Sensory alterations
 */
class AlterazioniSensoriali : RealmObject {
    var formicolii: FormicoliiNeurologici? = null
    var intorpidimento: Intorpidimento? = null
    var tremori: Tremori? = null
}

/**
 * Neurological paresthesias
 */
class FormicoliiNeurologici : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
    var frequenza: String = "" // Values: "raro", "occasionale", "frequente", "costante"
}

/**
 * Numbness
 */
class Intorpidimento : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
}

/**
 * Tremors
 */
class Tremori : RealmObject {
    var presente: Boolean = false
    var aRiposo: Boolean = false // At rest
    var intenzionali: Boolean = false // Intentional/action tremors
}

/**
 * Balance and coordination
 */
class EquilibrioCoordinazione : RealmObject {
    var vertigini: Vertigini? = null
    var difficoltaEquilibrio: Boolean = false
    var atassia: Boolean = false
    var difficoltaCoordinazione: Boolean = false
}

/**
 * Vertigo evaluation
 */
class Vertigini : RealmObject {
    var presente: Boolean = false
    var tipo: String = "" // Values: "rotatorie", "posizionali", "soggettive"
    var situazioni: RealmList<String> = realmListOf() // Situations that trigger
}

/**
 * Other neurological symptoms
 */
class AltriSintomiNeurologici : RealmObject {
    var fobie: Fobie? = null
    var agorafobia: Boolean = false
    var allucinazioni: Allucinazioni? = null
    var difficoltaParola: Boolean = false
    var difficoltaPensiero: Boolean = false
}

/**
 * Phobias
 */
class Fobie : RealmObject {
    var presente: Boolean = false
    var tipo: RealmList<String> = realmListOf() // Types of phobias
}

/**
 * Hallucinations
 */
class Allucinazioni : RealmObject {
    var presente: Boolean = false
    var tipo: String = "" // Values: "uditive", "visive"
}

