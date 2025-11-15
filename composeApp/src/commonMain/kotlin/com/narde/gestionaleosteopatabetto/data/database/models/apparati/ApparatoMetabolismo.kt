package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmObject

/**
 * Metabolism apparatus evaluation model
 * Contains evaluation of energy levels, thermoregulation, weight variations,
 * systemic symptoms, and appetite
 * NOTE: Sleep reference - see psico_neuro_endocrino.sonno (avoid duplication)
 */
class ApparatoMetabolismo : RealmObject {
    // Energy evaluation
    var energia: Energia? = null
    
    // Logical reference to sleep (in psico_neuro_endocrino)
    // Not duplicated here, use reference in business logic
    
    // Thermoregulation evaluation
    var termoregolazione: Termoregolazione? = null
    
    // Weight evaluation (important weight variations)
    var peso: Peso? = null
    
    // Systemic symptoms
    var sintomiSistemici: SintomiSistemici? = null
    
    // Appetite evaluation
    var appetito: Appetito? = null
}

/**
 * Energy evaluation
 */
class Energia : RealmObject {
    // Persistent fatigue (extends SintomoCompleto pattern)
    var stanchezzaPersistente: StanchezzaPersistente? = null
    
    // Daytime sleepiness
    var sonnolenzaDiurna: SonnolenzaDiurna? = null
}

/**
 * Persistent fatigue evaluation
 */
class StanchezzaPersistente : RealmObject {
    var presente: Boolean = false
    var intensita: String = "" // Values: "lieve", "moderata", "grave"
    var durataMesi: Int = 0 // Duration in months
    var peggioraSera: Boolean = false // Worsens in evening
    var miglioraRiposo: Boolean = false // Improves with rest
}

/**
 * Daytime sleepiness
 */
class SonnolenzaDiurna : RealmObject {
    var presente: Boolean = false
    var orariSpecifici: String = "" // Specific times when it occurs
}

/**
 * Thermoregulation evaluation
 */
class Termoregolazione : RealmObject {
    var maniPiediFreddi: ManiPiediFreddi? = null
    var sudorazioneNotturna: Boolean = false
    var sudorazioneEccessiva: Boolean = false
    var intolleranzaCaldo: Boolean = false
    var intolleranzaFreddo: Boolean = false
}

/**
 * Cold hands and feet
 */
class ManiPiediFreddi : RealmObject {
    var presente: Boolean = false
    var costante: Boolean = false
    var stagionale: Boolean = false
}

/**
 * Weight evaluation (important weight variations)
 */
class Peso : RealmObject {
    var variazioneRecente: String = "" // Values: "stabile", "aumento", "perdita"
    var kgVariati: Int = 0 // Kilograms changed
    var periodoMesi: Int = 0 // Period in months
    var intenzionale: Boolean = false // Intentional weight change
}

/**
 * Systemic symptoms
 */
class SintomiSistemici : RealmObject {
    var doloriDiffusi: Boolean = false
    var febbreRicorrente: Boolean = false
    var febbricolaPersistente: Boolean = false
    var vampate: Vampate? = null
}

/**
 * Hot flashes
 */
class Vampate : RealmObject {
    var presente: Boolean = false
    var frequenza: String = "" // Frequency description
    var relazioneMenopausa: Boolean = false // Related to menopause
}

/**
 * Appetite evaluation
 */
class Appetito : RealmObject {
    var stato: String = "" // Values: "normale", "aumentato", "diminuito"
    var seteEccessiva: Boolean = false // Important for diabetes
    var polifagia: Boolean = false // Excessive hunger (important for diabetes)
    var note: String = ""
}

