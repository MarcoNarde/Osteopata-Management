package com.narde.gestionaleosteopatabetto.data.database.models.apparati

import io.realm.kotlin.types.RealmObject

/**
 * Main container for apparatus evaluation
 * Holds all 12 apparatus systems as optional nested objects
 * This model is linked to Visita and allows partial or complete apparatus evaluation
 * All apparatus systems are optional, allowing flexibility in data entry
 */
class ValutazioneApparati : RealmObject {
    // Cranial apparatus
    var cranio: ApparatoCranio? = null
    
    // Respiratory apparatus
    var respiratorio: ApparatoRespiratorio? = null
    
    // Cardiovascular apparatus
    var cardiovascolare: ApparatoCardiovascolare? = null
    
    // Gastrointestinal apparatus
    var gastrointestinale: ApparatoGastrointestinale? = null
    
    // Urinary apparatus
    var urinario: ApparatoUrinario? = null
    
    // Reproductive apparatus (gender-specific sections)
    var riproduttivo: ApparatoRiproduttivo? = null
    
    // Psycho-neuro-endocrine apparatus
    var psicoNeuroEndocrino: ApparatoPsicoNeuroEndocrino? = null
    
    // Nails and skin apparatus
    var unghieCute: ApparatoUnghieCute? = null
    
    // Metabolism apparatus
    var metabolismo: ApparatoMetabolismo? = null
    
    // Lymph nodes apparatus
    var linfonodi: ApparatoLinfonodi? = null
    
    // Musculoskeletal apparatus (central for osteopathy)
    var muscoloScheletrico: ApparatoMuscoloScheletrico? = null
    
    // Nervous apparatus
    var nervoso: ApparatoNervoso? = null
}

