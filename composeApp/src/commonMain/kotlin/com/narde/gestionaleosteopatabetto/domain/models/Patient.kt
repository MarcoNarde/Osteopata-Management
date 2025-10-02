package com.narde.gestionaleosteopatabetto.domain.models

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Domain model for Patient - represents the business entity
 * This is the core model used throughout the domain layer
 * Independent of any external frameworks or data sources
 */
data class Patient(
    val id: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate?,
    val gender: Gender,
    val placeOfBirth: String,
    val taxCode: String,
    val phone: String,
    val email: String,
    val address: Address?,
    val anthropometricData: AnthropometricData?,
    val privacyConsents: PrivacyConsents?,
    val parentInfo: ParentInfo?
) {
    val fullName: String
        get() = "$firstName $lastName"
    
    val age: Int?
        get() = birthDate?.let { birth ->
            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            var calculatedAge = currentDate.year - birth.year
            if (currentDate.monthNumber < birth.monthNumber || 
                (currentDate.monthNumber == birth.monthNumber && currentDate.dayOfMonth < birth.dayOfMonth)) {
                calculatedAge--
            }
            calculatedAge
        }
    
    val isMinor: Boolean
        get() {
            val currentAge = age
            return currentAge != null && currentAge < 18
        }
}

enum class Gender {
    MALE, FEMALE
}

data class Address(
    val street: String,
    val city: String,
    val zipCode: String,
    val province: String,
    val country: String,
    val type: String = "residenza"
)

data class AnthropometricData(
    val height: Int, // in cm
    val weight: Double, // in kg
    val bmi: Double?,
    val dominantSide: DominantSide
)

enum class DominantSide {
    RIGHT, LEFT
}

data class PrivacyConsents(
    val treatmentConsent: Boolean,
    val marketingConsent: Boolean,
    val thirdPartyConsent: Boolean,
    val consentDate: String,
    val notes: String?
)

data class ParentInfo(
    val father: Parent?,
    val mother: Parent?
)

data class Parent(
    val firstName: String,
    val lastName: String
)
