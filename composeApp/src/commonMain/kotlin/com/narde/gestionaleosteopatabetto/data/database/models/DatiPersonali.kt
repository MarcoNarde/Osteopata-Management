package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Personal data model for patients in Realm
 * Contains basic patient information like name, birth date, contact details
 */
class DatiPersonali : RealmObject {
    var nome: String = ""
    var cognome: String = ""
    var dataNascita: String = ""
    var sesso: String = ""
    var luogoNascita: String = ""
    var codiceFiscale: String = ""
    var telefonoPaziente: String = ""
    var emailPaziente: String = ""
    var professione: String = ""
    var statoMaritale: String = ""
    var nazionalita: String = ""
}