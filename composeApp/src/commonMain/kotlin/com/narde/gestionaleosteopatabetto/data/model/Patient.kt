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
    val bmi: Double? = null, // Body Mass Index - can be null if not calculated
    val parentInfo: ParentInfo? = null // Parent information for minors
)

/**
 * Parent information data class
 */
data class ParentInfo(
    val father: Parent?,
    val mother: Parent?
)

/**
 * Parent data class
 */
data class Parent(
    val firstName: String,
    val lastName: String,
    val phone: String = "",
    val email: String = "",
    val profession: String = ""
) 