package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/**
 * Visit database model for Realm
 * Represents a clinical visit with patient assessment, consultation reasons,
 * and treatment data
 */
class Visita : RealmObject {
    @PrimaryKey
    var idVisita: String = ""
    var idPaziente: String = ""
    var dataVisita: String = ""
    var osteopata: String = "Roberto Caeran"
    var datiVisitaCorrente: DatiVisitaCorrente? = null
    var motivoConsulto: MotivoConsulto? = null
    var noteGenerali: String = ""
}
