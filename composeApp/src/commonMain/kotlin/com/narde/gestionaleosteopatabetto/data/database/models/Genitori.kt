package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Parents information model for patients in Realm
 * Contains information about patient's parents
 */
class Genitori : RealmObject {
    var padre: Padre? = null
    var madre: Madre? = null
}

/**
 * Father information model
 */
class Padre : RealmObject {
    var nome: String = ""
    var cognome: String = ""
    var telefono: String = ""
    var email: String = ""
    var professione: String = ""
}

/**
 * Mother information model
 */
class Madre : RealmObject {
    var nome: String = ""
    var cognome: String = ""
    var telefono: String = ""
    var email: String = ""
    var professione: String = ""
}