package com.narde.gestionaleosteopatabetto.data.model

/**
 * Visit status enumeration
 * Simple enum for visit status - localization handled in UI layer
 */
enum class VisitStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED;
    
    companion object {
        /**
         * Get status from string value
         * @param status The status string to convert
         * @return The corresponding VisitStatus enum value
         */
        fun fromString(status: String): VisitStatus {
            return when (status.lowercase()) {
                "scheduled", "programmata" -> SCHEDULED
                "completed", "completata" -> COMPLETED
                "cancelled", "annullata" -> CANCELLED
                else -> SCHEDULED // Default to scheduled
            }
        }
    }
} 