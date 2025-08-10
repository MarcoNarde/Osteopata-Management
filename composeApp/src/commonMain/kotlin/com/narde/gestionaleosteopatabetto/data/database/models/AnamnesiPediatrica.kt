package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Pediatric history model for patients in Realm
 * Contains information about pregnancy, birth, development milestones,
 * and significant childhood health issues
 */
class AnamnesiPediatrica : RealmObject {
    var gravidanza: Gravidanza? = null
    var parto: Parto? = null
    var sviluppo: Sviluppo? = null
    var noteGenerali: String = ""
}

/**
 * Pregnancy information model
 */
class Gravidanza : RealmObject {
    var complicazioni: Boolean = false
    var note: String = ""
}

/**
 * Birth information model
 */
class Parto : RealmObject {
    var tipo: String = "" // "naturale", "cesareo", "forcipe", etc.
    var complicazioni: Boolean = false
    var pesoNascitaGrammi: Int = 0
    var punteggioApgar5min: Int = 0 // APGAR score at 5 minutes
    var note: String = ""
}

/**
 * Development milestones model
 */
class Sviluppo : RealmObject {
    var primiPassiMesi: Int = 0 // Age in months when first steps were taken
    var primeParoleMesi: Int = 0 // Age in months when first words were spoken
    var problemiSviluppo: Boolean = false
    var note: String = ""
}

/**
 * Significant childhood health problems model
 */
class ProblemaSignificativo : RealmObject {
    var problema: String = ""
    var eta: String = "" // Age when the problem occurred
    var risolto: Boolean = false
} 