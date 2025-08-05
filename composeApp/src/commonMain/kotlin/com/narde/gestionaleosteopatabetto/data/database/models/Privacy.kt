package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmObject

/**
 * Privacy consent model for patients in Realm
 * Contains GDPR compliance information
 */
class Privacy : RealmObject {
    var consensoTrattamento: Boolean = false
    var consensoMarketing: Boolean = false
    var consensoTerzeparti: Boolean = false
    var dataConsenso: String = ""
    var notePrivacy: String = ""
}