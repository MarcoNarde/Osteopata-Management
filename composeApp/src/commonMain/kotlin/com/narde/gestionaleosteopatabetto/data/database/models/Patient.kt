package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/**
 * Patient database model for Realm
 * Represents a patient with personal data, address, privacy settings, and family information
 */
class Patient : RealmObject {
    @PrimaryKey
    var idPaziente: String = ""
    var datiPersonali: DatiPersonali? = null
    var indirizzo: Indirizzo? = null
    var privacy: Privacy? = null
    var genitori: Genitori? = null
    var medicocurante: MedicoCurante? = null
}