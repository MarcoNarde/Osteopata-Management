package com.narde.gestionaleosteopatabetto.data.model

/**
 * Visit data model for UI layer
 * Contains visit information for the osteopath management system
 */
data class Visit(
    val idVisita: String,
    val idPaziente: String,
    val dataVisita: String,
    val osteopata: String = "Roberto Caeran",
    val datiVisitaCorrente: DatiVisitaCorrente? = null,
    val motivoConsulto: MotivoConsulto? = null,
    val noteGenerali: String = ""
)

/**
 * Current visit data for UI layer
 */
data class DatiVisitaCorrente(
    val peso: Double = 0.0,
    val bmi: Double = 0.0,
    val pressione: String = "",
    val indiciCraniali: Double = 0.0
)

/**
 * Consultation reason for UI layer
 */
data class MotivoConsulto(
    val principale: MotivoPrincipale? = null,
    val secondario: MotivoSecondario? = null
)

/**
 * Main consultation reason for UI layer
 */
data class MotivoPrincipale(
    val descrizione: String = "",
    val insorgenza: String = "",
    val dolore: String = "",
    val vas: Int = 0,
    val fattori: String = ""
)

/**
 * Secondary consultation reason for UI layer
 */
data class MotivoSecondario(
    val descrizione: String = "",
    val durata: String = "",
    val vas: Int = 0
)