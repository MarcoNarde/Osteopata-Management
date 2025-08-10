package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Chronic conditions model for patients in Realm
 * Contains information about drug allergies, chronic diseases,
 * and other ongoing medical conditions
 */
class PatologieCroniche : RealmObject {
    var allergieFarmaci: AllergieFarmaci? = null
    var diabete: Diabete? = null
    var ipertiroidismo: Boolean = false
    var cardiopatia: Boolean = false
    var ipertensioneArteriosa: Boolean = false

    var altrePatologie: RealmList<AltraPatologia> = realmListOf()
}

/**
 * Drug allergies information model
 */
class AllergieFarmaci : RealmObject {
    var presente: Boolean = false
    var listaAllergie: RealmList<String> = realmListOf()// List of drug allergies
}

/**
 * Diabetes information model
 */
class Diabete : RealmObject {
    var presente: Boolean = false
    var tipologia: String = "" // "1", "2", "gestazionale", etc.
}

/**
 * Other chronic conditions model
 */
class AltraPatologia : RealmObject {
    var patologia: String = ""
    var dataInsorgenza: String = "" // Format: YYYY-MM-DD
    var stato: String = "" // "attiva", "in_trattamento", "risolta", etc.
} 