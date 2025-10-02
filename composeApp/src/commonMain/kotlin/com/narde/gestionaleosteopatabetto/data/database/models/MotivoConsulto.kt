package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Consultation reason model for Realm
 * Contains main and optional secondary consultation reasons
 */
class MotivoConsulto : RealmObject {
    var principale: MotivoPrincipale? = null
    var secondario: MotivoSecondario? = null
}
