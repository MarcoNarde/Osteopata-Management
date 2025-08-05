package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Address model for patients in Realm
 * Contains address information
 */
class Indirizzo : RealmObject {
    var via: String = ""
    var citta: String = ""
    var cap: String = ""
    var provincia: String = ""
    var nazione: String = ""
    var tipoIndirizzo: String = ""
}