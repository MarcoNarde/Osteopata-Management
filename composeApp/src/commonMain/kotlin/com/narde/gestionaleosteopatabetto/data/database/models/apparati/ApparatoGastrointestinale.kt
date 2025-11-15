package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Gastrointestinal apparatus evaluation model
 * Contains evaluation of GI functions: oral symptoms, digestive issues,
 * intestinal problems, bowel habits, and other GI-related conditions
 */
class ApparatoGastrointestinale : RealmObject {
    // Oral symptoms group
    var sintomiOrali: SintomiOrali? = null
    
    // Digestive symptoms group
    var sintomiDigestivi: SintomiDigestivi? = null
    
    // Intestinal symptoms group
    var intestino: SintomiIntestinali? = null
    
    // Bowel habits evaluation (detailed)
    var alvo: Alvo? = null
    
    // Other symptoms
    var altri: AltriSintomiGI? = null
}

/**
 * Oral symptoms group
 */
class SintomiOrali : RealmObject {
    var herpesFacciali: Boolean = false
    var difficoltaMasticare: Boolean = false
    var difficoltaDeglutire: Boolean = false
    var boccaSecca: Boolean = false
}

/**
 * Digestive symptoms group
 */
class SintomiDigestivi : RealmObject {
    // Stomach acidity
    var aciditaStomaco: AciditaStomaco? = null
    
    // Simple boolean symptoms
    var nauseaVomito: Boolean = false
    var reflusso: Boolean = false
}

/**
 * Stomach acidity evaluation
 */
class AciditaStomaco : RealmObject {
    var presente: Boolean = false
    var frequenza: String = "" // Values: "raro", "occasionale", "frequente", "costante"
    var orario: String = "" // Values: "mattina", "pomeriggio", "sera", "notte"
    var relazionePasti: String = "" // e.g., "dopo_pasti_abbondanti", "a_digiuino"
    var brucioreRetrosternale: Boolean = false // Retrosternal burning
}

/**
 * Intestinal symptoms group
 */
class SintomiIntestinali : RealmObject {
    var colonIrritabile: Boolean = false // Medical diagnosis
    var alvoAlterno: Boolean = false // Alternating bowel habits
    var emorroidi: Emorroidi? = null
}

/**
 * Hemorrhoids evaluation
 */
class Emorroidi : RealmObject {
    var presente: Boolean = false
    var sanguinanti: Boolean = false
    var dolorose: Boolean = false
}

/**
 * Bowel habits evaluation (detailed assessment)
 */
class Alvo : RealmObject {
    var frequenza: String = "" // e.g., "1-3_volte_giorno", "ogni_2_giorni"
    var regolarita: String = "" // Values: "regolare", "irregolare"
    var orarioPreferenziale: String = "" // e.g., "mattino", "sera"
    var consistenza: String = "" // Values: "dure", "normale", "molli", "liquide"
    var colore: String = "" // Values: "normale", "scure", "chiare", "verdastre", "con_sangue"
    var anomalie: RealmList<String> = realmListOf() // e.g., ["muco", "sangue", "cibo_non_digerito", "galleggianti"]
}

/**
 * Other GI symptoms
 */
class AltriSintomiGI : RealmObject {
    var problemiEpatici: Boolean = false
    var ittero: Boolean = false
    var vitiligine: Boolean = false
}

