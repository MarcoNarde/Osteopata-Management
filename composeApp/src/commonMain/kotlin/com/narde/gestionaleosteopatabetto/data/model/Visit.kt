package com.narde.gestionaleosteopatabetto.data.model

/**
 * Visit data model
 * Contains visit scheduling and status information for the osteopath management system
 */
data class Visit(
    val id: String,
    val patientName: String,
    val date: String,
    val time: String,
    val status: VisitStatus,
    val notes: String = ""
) 