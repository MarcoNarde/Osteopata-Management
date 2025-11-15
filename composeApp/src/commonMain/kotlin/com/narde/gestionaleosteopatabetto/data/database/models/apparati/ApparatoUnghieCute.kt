package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Nails and skin apparatus evaluation model
 * Contains evaluation of nail problems, skin pathologies, lesions, tattoos,
 * piercings, and dermatological check-ups
 */
class ApparatoUnghieCute : RealmObject {
    // Nail problems
    var unghie: Unghie? = null
    
    // Skin pathologies group
    var patologieCutanee: PatologieCutanee? = null
    
    // Lesions
    var lesioni: Lesioni? = null
    
    // Body modifications
    var modificheCorporee: ModificheCorporee? = null
    
    // Dermatological check-ups
    var controlli: ControlliDermatologici? = null
}

/**
 * Nail problems evaluation
 */
class Unghie : RealmObject {
    var problemi: Boolean = false // Main flag
    var dettagli: RealmList<String> = realmListOf() // e.g., ["fragilita", "micosi", "striature"]
}

/**
 * Skin pathologies group
 */
class PatologieCutanee : RealmObject {
    var eczemi: Eczemi? = null
    var psoriasi: Psoriasi? = null
    var dermatite: Dermatite? = null
    var verruche: Boolean = false
    var vitiligine: Boolean = false
}

/**
 * Eczema evaluation
 */
class Eczemi : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
    var cronico: Boolean = false
}

/**
 * Psoriasis evaluation
 */
class Psoriasi : RealmObject {
    var presente: Boolean = false
    var localizzazione: RealmList<String> = realmListOf()
    var estensione: String = "" // Values: "lieve", "moderata", "estesa"
    var inTrattamento: Boolean = false
}

/**
 * Dermatitis evaluation
 */
class Dermatite : RealmObject {
    var presente: Boolean = false
    var tipo: String = "" // Values: "atopica", "seborroica", "contatto"
    var localizzazione: RealmList<String> = realmListOf()
}

/**
 * Lesions evaluation
 */
class Lesioni : RealmObject {
    // Scars list
    var cicatrici: RealmList<Cicatrice> = realmListOf()
    
    // Keloid tendency
    var cheloidiTendenza: Boolean = false // Important for treatments
}

/**
 * Scar information
 */
class Cicatrice : RealmObject {
    var localizzazione: String = "" // e.g., "ginocchio_destro"
    var origine: String = "" // Values: "trauma", "chirurgica", "ustione"
    var data: String = "" // Format: YYYY-MM-DD
    var dimensioneCm: Int = 0 // Size in centimeters
    var cheloide: Boolean = false // Keloid scar
}

/**
 * Body modifications
 */
class ModificheCorporee : RealmObject {
    var tatuaggi: Tatuaggi? = null
    var piercing: Piercing? = null
}

/**
 * Tattoos
 */
class Tatuaggi : RealmObject {
    var presente: Boolean = false
    var numero: Int = 0
    var localizzazioni: RealmList<String> = realmListOf()
}

/**
 * Piercings
 */
class Piercing : RealmObject {
    var presente: Boolean = false
    var numero: Int = 0
    var localizzazioni: RealmList<String> = realmListOf()
}

/**
 * Dermatological check-ups
 */
class ControlliDermatologici : RealmObject {
    var neiControllati: Boolean = false // Moles checked
    var ultimoControllo: String = "" // Format: YYYY-MM-DD
    var neiAtipici: Boolean = false // Atypical moles
    var prossimoControllo: String = "" // Format: YYYY-MM-DD
}

