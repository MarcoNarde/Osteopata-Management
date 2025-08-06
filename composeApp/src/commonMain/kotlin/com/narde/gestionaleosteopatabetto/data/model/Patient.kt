package com.narde.gestionaleosteopatabetto.data.model

/**
 * Patient data model
 * Contains basic patient information for the osteopath management system
 */
data class Patient(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val age: Int,
    val bmi: Double? = null // Body Mass Index - can be null if not calculated
) 