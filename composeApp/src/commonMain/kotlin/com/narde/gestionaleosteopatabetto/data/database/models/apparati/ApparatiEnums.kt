package com.narde.gestionaleosteopatabetto.data.database.models.apparati

/**
 * Common enums used across apparatus evaluation models
 * These enums represent standardized values for symptoms and conditions
 */

/**
 * Frequency of symptom occurrence
 */
enum class Frequenza {
    RARO,        // Rare
    OCCASIONALE, // Occasional
    FREQUENTE,   // Frequent
    COSTANTE     // Constant
}

/**
 * Intensity level of symptoms or conditions
 */
enum class Intensita {
    LIEVE,    // Mild
    MODERATA, // Moderate
    GRAVE     // Severe
}

/**
 * Side or laterality of symptoms
 */
enum class Lato {
    DESTRO,     // Right
    SINISTRO,   // Left
    BILATERALE  // Bilateral
}

/**
 * Sleep quality assessment
 */
enum class QualitaSonno {
    OTTIMA,  // Excellent
    BUONA,   // Good
    SCARSA,  // Poor
    PESSIMA  // Very poor
}

/**
 * Duration classification of symptoms
 */
enum class Durata {
    ACUTO,     // Acute
    SUBACUTO,  // Subacute
    CRONICO    // Chronic
}

/**
 * Bowel movement frequency
 */
enum class FrequenzaAlvo {
    NORMALE,           // Normal (1-3 times per day)
    STIPSI,            // Constipation
    DIARREA,           // Diarrhea
    ALVO_ALTERNO       // Alternating
}

/**
 * Stool consistency
 */
enum class ConsistenzaFeci {
    DURE,      // Hard
    NORMALE,   // Normal
    MOLLE,     // Soft
    LIQUIDE    // Liquid
}

/**
 * Stool color
 */
enum class ColoreFeci {
    NORMALE,      // Normal
    SCURE,        // Dark
    CHIARE,       // Light
    VERDASTRE,    // Greenish
    CON_SANGUE    // With blood
}

/**
 * Reflex status
 */
enum class StatoRiflessi {
    NORMALI,    // Normal
    AUMENTATI,  // Increased
    DIMINUITI,  // Decreased
    ASSENTI     // Absent
}

/**
 * Sensitivity status
 */
enum class StatoSensibilita {
    NORMALE,    // Normal
    ALTERATA,   // Altered
    ASSENTE     // Absent
}

/**
 * Weight variation direction
 */
enum class VariazionePeso {
    STABILE,   // Stable
    AUMENTO,   // Increase
    PERDITA    // Loss
}

/**
 * Appetite status
 */
enum class StatoAppetito {
    NORMALE,    // Normal
    AUMENTATO,  // Increased
    DIMINUITO   // Decreased
}

/**
 * Morning stiffness timing
 */
enum class OrarioRigidita {
    MATTUTINA,  // Morning
    SERALE,     // Evening
    COSTANTE    // Constant
}

/**
 * Trauma severity
 */
enum class Gravita {
    LIEVE,    // Mild
    MODERATA, // Moderate
    GRAVE     // Severe
}

