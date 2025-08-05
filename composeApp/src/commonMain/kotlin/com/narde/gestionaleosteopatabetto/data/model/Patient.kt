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
    val age: Int
) 