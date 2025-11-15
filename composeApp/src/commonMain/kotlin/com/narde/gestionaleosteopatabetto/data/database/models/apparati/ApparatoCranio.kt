package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Cranial apparatus evaluation model
 * Contains evaluation of cranial functions: olfaction, vision, hearing,
 * headaches, TMJ disorders, and orthodontic history
 */
class ApparatoCranio : RealmObject {
    // Simple boolean symptoms using SintomoBase pattern
    var problemiOlfatto: Boolean = false
    var problemiVista: Boolean = false
    var problemiUdito: Boolean = false
    var disturbiOcclusali: Boolean = false
    var malattieParodontali: Boolean = false
    var linguaDolente: Boolean = false
    
    // Complete headache evaluation
    var cefalea: Cefalea? = null
    
    // Migraine evaluation
    var emicrania: Emicrania? = null
    
    // TMJ (Temporomandibular Joint) evaluation
    var atm: ProblemiATM? = null
    
    // Orthodontic appliance history
    var apparecchioOrtodontico: ApparecchioOrtodontico? = null
}

/**
 * Headache evaluation - complete symptom with all details
 */
class Cefalea : RealmObject {
    var presente: Boolean = false
    var intensitaVas: Int = 0 // Range: 0-10
    var frequenza: String = "" // Values: "raro", "occasionale", "frequente", "costante"
    var durataOre: Int = 0 // Duration in hours
    var caratteristiche: CaratteristicheCefalea? = null
}

/**
 * Headache characteristics
 */
class CaratteristicheCefalea : RealmObject {
    var tipo: String = "" // Values: "tensiva", "emicranica", "cluster"
    var localizzazione: RealmList<String> = realmListOf() // e.g., ["frontale", "temporale"]
    var fattoriScatenanti: RealmList<String> = realmListOf() // e.g., ["stress", "postura", "schermi"]
    var fattoriAllevianti: RealmList<String> = realmListOf() // e.g., ["riposo", "buio", "analgesici"]
}

/**
 * Migraine evaluation
 */
class Emicrania : RealmObject {
    var presente: Boolean = false
    var conAura: Boolean = false // Migraine with aura
    var frequenza: String = "" // Values: "raro", "occasionale", "frequente", "costante"
}

/**
 * TMJ (Temporomandibular Joint) problems evaluation
 */
class ProblemiATM : RealmObject {
    var problemiPresenti: Boolean = false // Main flag
    var sintomi: SintomiATM? = null
}

/**
 * TMJ symptoms
 */
class SintomiATM : RealmObject {
    var clickArticolare: Boolean = false
    var doloreMasticazione: Boolean = false
    var limitazioneApertura: Boolean = false
    var serramentoDiurno: Boolean = false
    var bruxismoNotturno: Boolean = false
    var deviazioneMandibolare: Boolean = false
}

/**
 * Orthodontic appliance history
 */
class ApparecchioOrtodontico : RealmObject {
    var portato: Boolean = false
    var periodo: String = "" // e.g., "adolescenza", "infanzia"
    var etaInizio: Int = 0 // Age when started
    var etaFine: Int = 0 // Age when finished
    var durataAnni: Int = 0 // Duration in years (can be calculated)
    var tipo: String = "" // Values: "fisso", "mobile", "invisibile"
}

