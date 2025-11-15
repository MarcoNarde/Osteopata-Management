package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Musculoskeletal apparatus evaluation model
 * COMPLEX apparatus - central for osteopathy
 * Contains evaluation of pain, stiffness, weakness, traumas, functional limitations,
 * posture, and enthesitis/tendinitis
 */
class ApparatoMuscoloScheletrico : RealmObject {
    // Pain evaluation (PRINCIPAL structure for osteopathy)
    var dolore: DoloreMuscoloScheletrico? = null
    
    // Stiffness evaluation (important for diagnosis)
    var rigidita: Rigidita? = null
    
    // Weakness evaluation
    var debolezza: Debolezza? = null
    
    // Joint alterations (inflammatory signs)
    var alterazioniArticolari: AlterazioniArticolari? = null
    
    // Muscle problems
    var problemiMuscolari: ProblemiMuscolari? = null
    
    // Past traumas (clinical history)
    var traumiPregressi: RealmList<TraumaPregresso> = realmListOf()
    
    // Functional limitations (IMPORTANT for therapeutic goals)
    var limitazioni: LimitazioniFunzionali? = null
    
    // Posture evaluation
    var postura: Postura? = null
    
    // Enthesitis and tendinitis
    var entesitiTendiniti: EntesitiTendiniti? = null
}

/**
 * Musculoskeletal pain evaluation (PRINCIPAL structure)
 * Each localization is a distinct pain
 */
class DoloreMuscoloScheletrico : RealmObject {
    var presente: Boolean = false // Main flag
    
    // List of pain localizations (each is a distinct pain)
    var sedi: RealmList<LocalizzazioneDolore> = realmListOf()
}

/**
 * Pain localization (extends Localizzazione pattern)
 */
class LocalizzazioneDolore : RealmObject {
    var zona: String = "" // Anatomical area
    var lato: String = "" // Values: "destro", "sinistro", "bilaterale"
    var vas: Int = 0 // Range: 0-10 - IMPORTANT for follow-up
    var tipo: String = "" // Values: "superficiale", "profondo", "lancinante", "urente"
    var durata: String = "" // Values: "acuto", "subacuto", "cronico"
    var irradiazione: String = "" // Radiation pattern
    var costante: Boolean = false // Constant pain
    
    // Factors that worsen
    var peggioramento: RealmList<String> = realmListOf() // e.g., ["seduto_prolungato", "flessione", "rotazione"]
    
    // Factors that improve
    var miglioramento: RealmList<String> = realmListOf() // e.g., ["riposo", "movimento_leggero", "calore"]
}

/**
 * Stiffness evaluation (important for diagnosis)
 */
class Rigidita : RealmObject {
    var presente: Boolean = false
    var sedi: RealmList<String> = realmListOf() // List of locations
    var orario: String = "" // Values: "mattutina", "serale", "costante"
    var durataMinuti: Int = 0 // Duration in minutes - IMPORTANT: >30min suggests inflammation
    var miglioraConMovimento: Boolean = false // Diagnostic for type of stiffness
    var peggioraConRiposo: Boolean = false
}

/**
 * Weakness evaluation
 */
class Debolezza : RealmObject {
    var stanchezzaMuscolare: Boolean = false
    var affaticamentoFacile: Boolean = false
    var zoneSpecifiche: RealmList<String> = realmListOf() // If localized weakness
}

/**
 * Joint alterations (inflammatory signs)
 */
class AlterazioniArticolari : RealmObject {
    var deformita: Boolean = false
    var rossore: Boolean = false
    var gonfiore: GonfioreArticolare? = null
    var caloreLocale: Boolean = false
}

/**
 * Joint swelling
 */
class GonfioreArticolare : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
}

/**
 * Muscle problems
 */
class ProblemiMuscolari : RealmObject {
    var spasmi: Spasmi? = null
    var crampi: Crampi? = null
    var contratture: Contratture? = null
    var triggerPoints: Boolean = false // Important for treatment
    var tickMuscolari: TickMuscolari? = null
}

/**
 * Muscle spasms
 */
class Spasmi : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
}

/**
 * Muscle cramps
 */
class Crampi : RealmObject {
    var presente: Boolean = false
    var notturni: Boolean = false // Nocturnal cramps
    var duranteAttivita: Boolean = false // During activity
}

/**
 * Muscle contractures
 */
class Contratture : RealmObject {
    var presente: Boolean = false
    var zone: RealmList<String> = realmListOf() // e.g., ["paravertebrali_lombari"]
}

/**
 * Muscle ticks
 */
class TickMuscolari : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
}

/**
 * Past trauma (traumatological history)
 */
class TraumaPregresso : RealmObject {
    var tipo: String = "" // Type of trauma
    var sede: String = "" // Location
    var data: String = "" // Format: YYYY-MM-DD
    var meccanismo: String = "" // How it happened (e.g., "inversione")
    var gravita: String = "" // Values: "lieve", "moderata", "grave"
    var trattamento: String = "" // Treatment received
    var recuperoCompleto: Boolean = false
    var sequele: Boolean = false // Permanent sequelae
    var limitazioniResidue: Boolean = false
}

/**
 * Functional limitations (IMPORTANT for therapeutic goals)
 */
class LimitazioniFunzionali : RealmObject {
    var presente: Boolean = false
    
    // Limited activities
    var attivitaLimitate: RealmList<String> = realmListOf() // e.g., ["sollevamento_pesi", "flessione_prolungata", "stare_seduto_oltre_30min"]
    
    // Painful specific movements
    var doloreMovimentiSpecifici: RealmList<String> = realmListOf() // e.g., ["flessione_lombare", "rotazione_dx"]
}

/**
 * Posture evaluation
 */
class Postura : RealmObject {
    var alterazioni: RealmList<String> = realmListOf() // e.g., ["antepulsione_capo", "ipercifosi_dorsale", "rotazione_spalle_interne"]
    var asimmetrie: Asimmetrie? = null
    var scoliosi: Scoliosi? = null
}

/**
 * Postural asymmetries
 */
class Asimmetrie : RealmObject {
    var presente: Boolean = false
    var dettagli: String = "" // Details of asymmetries
}

/**
 * Scoliosis evaluation
 */
class Scoliosi : RealmObject {
    var presente: Boolean = false
    var tipo: String = "" // Values: "lombare", "dorsale", "dorso-lombare"
    var convessita: String = "" // Values: "destro", "sinistro"
}

/**
 * Enthesitis and tendinitis
 */
class EntesitiTendiniti : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
    var cronica: Boolean = false
}

