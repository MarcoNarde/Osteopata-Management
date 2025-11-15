package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Reproductive apparatus evaluation model
 * NOTE: Gender-specific sections - UI/business logic determines which to use
 * based on patient.sesso field ("M" for male, "F" for female)
 * Contains both male and female sections, but only one should be populated
 */
class ApparatoRiproduttivo : RealmObject {
    // Male reproductive evaluation (loaded ONLY if patient.sesso == "M")
    var maschile: RiproduttivoMaschile? = null
    
    // Female reproductive evaluation (loaded ONLY if patient.sesso == "F")
    var femminile: RiproduttivoFemminile? = null
}

/**
 * Male reproductive evaluation
 */
class RiproduttivoMaschile : RealmObject {
    // Sexual dysfunctions
    var disfunzioni: DisfunzioniSessualiMaschili? = null
    
    // Genital symptoms
    var sintomi: SintomiGenitaliMaschili? = null
    
    // Male pathologies
    var patologie: PatologieMaschili? = null
}

/**
 * Male sexual dysfunctions
 */
class DisfunzioniSessualiMaschili : RealmObject {
    var erettile: Boolean = false
    var eiaculazionePrecoce: Boolean = false
    var eiaculazioneRitardata: Boolean = false
    var libidoRidotta: Boolean = false
}

/**
 * Male genital symptoms
 */
class SintomiGenitaliMaschili : RealmObject {
    var doloreGenitale: Boolean = false
    var perdite: Boolean = false
    var masseTesticolari: Boolean = false // IMPORTANT: requires urgent medical evaluation
}

/**
 * Male pathologies
 */
class PatologieMaschili : RealmObject {
    var ipertrofiaProstatica: IpertrofiaProstatica? = null
    var varicocele: Varicocele? = null
    var infertilita: InfertilitaMaschile? = null
}

/**
 * Prostate hypertrophy
 */
class IpertrofiaProstatica : RealmObject {
    var presente: Boolean = false
    var inTrattamento: Boolean = false
}

/**
 * Varicocele
 */
class Varicocele : RealmObject {
    var presente: Boolean = false
    var lato: String = "" // Values: "destro", "sinistro", "bilaterale"
    var grado: Int = 0 // Grade 1-3
}

/**
 * Male infertility
 */
class InfertilitaMaschile : RealmObject {
    var presente: Boolean = false
    var inValutazione: Boolean = false
}

/**
 * Female reproductive evaluation
 */
class RiproduttivoFemminile : RealmObject {
    // Female pathologies
    var patologie: PatologieFemminili? = null
    
    // Genital symptoms
    var sintomi: SintomiGenitaliFemminili? = null
    
    // Menopause evaluation
    var menopausa: Menopausa? = null
    
    // Menstrual cycle evaluation (complex, many fields)
    var ciclo: CicloMestruale? = null
}

/**
 * Female pathologies
 */
class PatologieFemminili : RealmObject {
    var cistiSeno: Boolean = false
    var noduliSeno: NoduliSeno? = null
    var cistiOvariche: CistiOvariche? = null
    var endometriosi: Endometriosi? = null
    var fibromi: Fibromi? = null
    var infertilita: InfertilitaFemminile? = null
}

/**
 * Breast nodules
 */
class NoduliSeno : RealmObject {
    var presente: Boolean = false
    var localizzazione: String = "" // Location
    var daQuanto: String = "" // How long (e.g., "3_mesi", "6_mesi")
}

/**
 * Ovarian cysts
 */
class CistiOvariche : RealmObject {
    var presente: Boolean = false
    var sintomatiche: Boolean = false
}

/**
 * Endometriosis
 */
class Endometriosi : RealmObject {
    var presente: Boolean = false
    var inTrattamento: Boolean = false
}

/**
 * Fibroids
 */
class Fibromi : RealmObject {
    var presente: Boolean = false
    var sintomatici: Boolean = false
}

/**
 * Female infertility
 */
class InfertilitaFemminile : RealmObject {
    var presente: Boolean = false
    var inValutazione: Boolean = false
}

/**
 * Female genital symptoms
 */
class SintomiGenitaliFemminili : RealmObject {
    var perditeAnomale: Boolean = false
    var prurito: Boolean = false
    var dolore: Boolean = false
    var dispareunia: Dispareunia? = null
    var libidoRidotta: Boolean = false
}

/**
 * Dyspareunia (painful intercourse)
 */
class Dispareunia : RealmObject {
    var presente: Boolean = false
    var superficiale: Boolean = false // Superficial pain
    var profonda: Boolean = false // Deep pain
}

/**
 * Menopause evaluation
 */
class Menopausa : RealmObject {
    var presente: Boolean = false
    var etaInsorgenza: Int = 0 // Age of onset
    var inTerapiaOrmonale: Boolean = false
}

/**
 * Menstrual cycle evaluation (complex structure)
 */
class CicloMestruale : RealmObject {
    var regolare: Boolean = true
    var durataGiorni: Int = 0 // Typical range: 3-7 days
    var intervalloGiorni: Int = 0 // Typical range: 21-35 days
    var ultimaMestruazione: String = "" // Format: YYYY-MM-DD
    
    // Premenstrual symptoms
    var sintomiPremestruali: RealmList<String> = realmListOf() // e.g., ["gonfiore", "irritabilita", "tenerezza_seno", "desiderio_cioccolata"]
    
    // Menstrual symptoms
    var sintomiMestruali: SintomiMestruali? = null
}

/**
 * Menstrual symptoms
 */
class SintomiMestruali : RealmObject {
    var crampi: Boolean = false
    var crampiIntensitaVas: Int = 0 // Range: 0-10
    var sanguinamentoAbbondante: Boolean = false
    var coaguli: Boolean = false
    var dismenorrea: Boolean = false // Significant menstrual pain
    var perditeTraCicli: Boolean = false // Bleeding between cycles
}

