package com.narde.gestionaleosteopatabetto.domain.models

import kotlinx.datetime.LocalDate

/**
 * Domain model for Visit - represents the business entity
 * This is the core model used throughout the domain layer
 * Independent of any external frameworks or data sources
 */
data class Visit(
    val idVisita: String,
    val idPaziente: String,
    val dataVisita: LocalDate,
    val osteopata: String,
    val datiVisitaCorrente: DatiVisitaCorrente?,
    val motivoConsulto: MotivoConsulto?,
    val noteGenerali: String
) {
    val dataVisitaString: String
        get() = dataVisita.toString()
}

/**
 * Current visit data for domain layer
 */
data class DatiVisitaCorrente(
    val peso: Double,
    val bmi: Double,
    val pressione: String,
    val indiciCraniali: Double
)

/**
 * Consultation reason for domain layer
 */
data class MotivoConsulto(
    val principale: MotivoPrincipale?,
    val secondario: MotivoSecondario?
)

/**
 * Main consultation reason for domain layer
 */
data class MotivoPrincipale(
    val descrizione: String,
    val insorgenza: String,
    val dolore: String,
    val vas: Int,
    val fattori: String
)

/**
 * Secondary consultation reason for domain layer
 */
data class MotivoSecondario(
    val descrizione: String,
    val durata: String,
    val vas: Int
)

