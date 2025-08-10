package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Lifestyle factors model for patients in Realm
 * Contains information about smoking habits, work type,
 * physical activity, and daily routines
 */
class StileVita : RealmObject {
    var tabagismo: Tabagismo? = null
    var lavoro: String = "" // "sedentario", "moderato", "pesante"
    var professione: String = ""
    var oreLavoroGiorno: Int = 0
    var attivitaSportiva: AttivitaSportiva? = null
}

/**
 * Smoking habits information model
 */
class Tabagismo : RealmObject {
    var stato: String = "" // "non_fumatore", "<10", "10-20", ">20"
    var sigaretteGiorno: Int = 0
    var anniFumo: Int = 0
    var dataSmettere: String = "" // Date when stopped smoking
}

/**
 * Physical activity information model
 */
class AttivitaSportiva : RealmObject {
    var presente: Boolean = false
    var sport: RealmList<String> = realmListOf() // List of sports or physical activities
    var frequenza: String = "" // "occasionale", "settimanale", "quotidiana"
    var intensita: String = "" // "bassa", "media", "alta"
} 