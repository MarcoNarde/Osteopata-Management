package com.narde.gestionaleosteopatabetto.data.database.models

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.ext.realmListOf

/**
 * Clinical history model for patients in Realm
 * Contains comprehensive medical history including chronic conditions,
 * lifestyle factors, medications, interventions, and pediatric history
 */
class StoriaClinica : RealmObject {
    var patologieCroniche: PatologieCroniche? = null
    var stileVita: StileVita? = null
    var terapieFarmacologiche: RealmList<TerapiaFarmacologica> = realmListOf()
    var interventiTrauma: RealmList<InterventoTrauma> = realmListOf()
    var esamiStrumentali: RealmList<EsameStrumentale> = realmListOf()
    var anamnesiPediatrica: AnamnesiPediatrica? = null
} 