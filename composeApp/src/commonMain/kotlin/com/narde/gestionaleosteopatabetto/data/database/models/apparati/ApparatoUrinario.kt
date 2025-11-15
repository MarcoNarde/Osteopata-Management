package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmObject

/**
 * Urinary apparatus evaluation model
 * Contains evaluation of urinary functions: infections, renal pathologies,
 * and urinary symptoms (incontinence, urgency, nocturia, pain/burning)
 */
class ApparatoUrinario : RealmObject {
    // Urinary infections
    var infezioni: InfezioniUrinarie? = null
    
    // Renal pathologies
    var patologie: PatologieRenali? = null
    
    // Urinary symptoms group
    var sintomi: SintomiUrinari? = null
}

/**
 * Urinary infections evaluation
 */
class InfezioniUrinarie : RealmObject {
    var presenti: Boolean = false
    var ricorrenti: Boolean = false
    var ultimaInfezione: String = "" // Format: YYYY-MM-DD
    var frequenzaAnno: Int = 0 // Number of infections per year
}

/**
 * Renal pathologies group
 */
class PatologieRenali : RealmObject {
    var malattieRenali: Boolean = false
    var tipoMalattia: String = "" // Type of renal disease
    var calcoli: Boolean = false // Kidney stones
    var storiaColiche: Boolean = false // History of renal colic
}

/**
 * Urinary symptoms group
 */
class SintomiUrinari : RealmObject {
    // Incontinence evaluation
    var incontinenza: Incontinenza? = null
    
    // Simple boolean symptoms
    var doloreBruciore: Boolean = false
    var urgenza: Boolean = false
    
    // Nocturia evaluation
    var nicturia: Nicturia? = null
}

/**
 * Incontinence evaluation
 */
class Incontinenza : RealmObject {
    var presente: Boolean = false
    var daSforzo: Boolean = false // Stress incontinence
    var daUrgenza: Boolean = false // Urge incontinence
    var notturna: Boolean = false // Nocturnal incontinence
    var quantita: String = "" // Values: "lieve", "moderata", "grave"
}

/**
 * Nocturia evaluation (frequent urination at night)
 */
class Nicturia : RealmObject {
    var presente: Boolean = false
    var numeroRisvegli: Int = 0 // Number of awakenings per night
}

