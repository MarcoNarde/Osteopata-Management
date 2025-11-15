package com.narde.gestionaleosteopatabetto.data.database.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

/**
 * Validation utilities for apparatus evaluation data
 * Provides validation functions for VAS ranges, dates, required fields, and enum values
 */
object ApparatiValidation {
    
    /**
     * Valid VAS range: 0-10
     */
    private const val VAS_MIN = 0
    private const val VAS_MAX = 10
    
    /**
     * Valid age range: 0-120 years
     */
    private const val AGE_MIN = 0
    private const val AGE_MAX = 120
    
    /**
     * Valid weight range: 0-300 kg
     */
    private const val WEIGHT_MIN = 0.0
    private const val WEIGHT_MAX = 300.0
    
    /**
     * Valid height range: 0-250 cm
     */
    private const val HEIGHT_MIN = 0
    private const val HEIGHT_MAX = 250
    
    /**
     * Valid duration in minutes: must be positive
     */
    private const val DURATION_MINUTES_MIN = 0
    
    /**
     * Valid frequency per year: must be non-negative
     */
    private const val FREQUENCY_YEAR_MIN = 0
    
    /**
     * Validate VAS (Visual Analog Scale) value
     * @param vas The VAS value to validate
     * @return true if vas is in valid range (0-10), false otherwise
     */
    fun isValidVas(vas: Int): Boolean {
        return vas in VAS_MIN..VAS_MAX
    }
    
    /**
     * Validate VAS value and return error message if invalid
     * @param vas The VAS value to validate
     * @return null if valid, error message if invalid
     */
    fun validateVas(vas: Int): String? {
        return if (!isValidVas(vas)) {
            "VAS value must be between $VAS_MIN and $VAS_MAX"
        } else {
            null
        }
    }
    
    /**
     * Validate date string format (YYYY-MM-DD) and ensure it's not in the future
     * @param dateString The date string to validate
     * @return true if date is valid and not in the future, false otherwise
     */
    fun isValidDate(dateString: String): Boolean {
        if (dateString.isBlank()) {
            return true // Empty dates are allowed (optional fields)
        }
        
        return try {
            val date = LocalDate.parse(dateString)
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            date <= today // Date cannot be in the future
        } catch (e: Exception) {
            false // Invalid date format
        }
    }
    
    /**
     * Validate date string and return error message if invalid
     * @param dateString The date string to validate
     * @return null if valid, error message if invalid
     */
    fun validateDate(dateString: String): String? {
        if (dateString.isBlank()) {
            return null // Empty dates are allowed
        }
        
        return try {
            val date = LocalDate.parse(dateString)
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            if (date > today) {
                "Date cannot be in the future"
            } else {
                null
            }
        } catch (e: Exception) {
            "Invalid date format. Expected YYYY-MM-DD"
        }
    }
    
    /**
     * Validate age value
     * @param age The age to validate
     * @return true if age is in valid range (0-120), false otherwise
     */
    fun isValidAge(age: Int): Boolean {
        return age in AGE_MIN..AGE_MAX
    }
    
    /**
     * Validate age and return error message if invalid
     * @param age The age to validate
     * @return null if valid, error message if invalid
     */
    fun validateAge(age: Int): String? {
        return if (!isValidAge(age)) {
            "Age must be between $AGE_MIN and $AGE_MAX years"
        } else {
            null
        }
    }
    
    /**
     * Validate weight value
     * @param weight The weight to validate
     * @return true if weight is in valid range (0-300 kg), false otherwise
     */
    fun isValidWeight(weight: Double): Boolean {
        return weight in WEIGHT_MIN..WEIGHT_MAX
    }
    
    /**
     * Validate weight and return error message if invalid
     * @param weight The weight to validate
     * @return null if valid, error message if invalid
     */
    fun validateWeight(weight: Double): String? {
        return if (!isValidWeight(weight)) {
            "Weight must be between $WEIGHT_MIN and $WEIGHT_MAX kg"
        } else {
            null
        }
    }
    
    /**
     * Validate height value
     * @param height The height to validate
     * @return true if height is in valid range (0-250 cm), false otherwise
     */
    fun isValidHeight(height: Int): Boolean {
        return height in HEIGHT_MIN..HEIGHT_MAX
    }
    
    /**
     * Validate height and return error message if invalid
     * @param height The height to validate
     * @return null if valid, error message if invalid
     */
    fun validateHeight(height: Int): String? {
        return if (!isValidHeight(height)) {
            "Height must be between $HEIGHT_MIN and $HEIGHT_MAX cm"
        } else {
            null
        }
    }
    
    /**
     * Validate duration in minutes
     * @param durationMinutes The duration to validate
     * @return true if duration is valid (>= 0), false otherwise
     */
    fun isValidDurationMinutes(durationMinutes: Int): Boolean {
        return durationMinutes >= DURATION_MINUTES_MIN
    }
    
    /**
     * Validate duration in minutes and return error message if invalid
     * @param durationMinutes The duration to validate
     * @return null if valid, error message if invalid
     */
    fun validateDurationMinutes(durationMinutes: Int): String? {
        return if (!isValidDurationMinutes(durationMinutes)) {
            "Duration must be non-negative"
        } else {
            null
        }
    }
    
    /**
     * Validate frequency per year
     * @param frequencyYear The frequency to validate
     * @return true if frequency is valid (>= 0), false otherwise
     */
    fun isValidFrequencyYear(frequencyYear: Int): Boolean {
        return frequencyYear >= FREQUENCY_YEAR_MIN
    }
    
    /**
     * Validate frequency per year and return error message if invalid
     * @param frequencyYear The frequency to validate
     * @return null if valid, error message if invalid
     */
    fun validateFrequencyYear(frequencyYear: Int): String? {
        return if (!isValidFrequencyYear(frequencyYear)) {
            "Frequency per year must be non-negative"
        } else {
            null
        }
    }
    
    /**
     * Validate enum value against allowed values
     * @param value The value to validate
     * @param allowedValues List of allowed string values
     * @return true if value is in allowed values or empty, false otherwise
     */
    fun isValidEnumValue(value: String, allowedValues: List<String>): Boolean {
        if (value.isBlank()) {
            return true // Empty values are allowed (optional fields)
        }
        return allowedValues.contains(value.lowercase())
    }
    
    /**
     * Validate enum value and return error message if invalid
     * @param value The value to validate
     * @param allowedValues List of allowed string values
     * @param fieldName Name of the field being validated (for error message)
     * @return null if valid, error message if invalid
     */
    fun validateEnumValue(value: String, allowedValues: List<String>, fieldName: String): String? {
        if (value.isBlank()) {
            return null // Empty values are allowed
        }
        
        return if (!isValidEnumValue(value, allowedValues)) {
            "$fieldName must be one of: ${allowedValues.joinToString(", ")}"
        } else {
            null
        }
    }
    
    /**
     * Validate that required fields are filled when parent boolean is true
     * This is a generic validation pattern for conditional required fields
     * @param parentPresent True if parent condition is present
     * @param requiredFieldValue The value of the required field
     * @param fieldName Name of the required field
     * @return null if valid, error message if invalid
     */
    fun validateRequiredWhenPresent(
        parentPresent: Boolean,
        requiredFieldValue: String?,
        fieldName: String
    ): String? {
        if (parentPresent && (requiredFieldValue == null || requiredFieldValue.isBlank())) {
            return "$fieldName is required when condition is present"
        }
        return null
    }
    
    /**
     * Validate that required fields are filled when parent boolean is true (for numeric fields)
     * @param parentPresent True if parent condition is present
     * @param requiredFieldValue The numeric value of the required field
     * @param fieldName Name of the required field
     * @return null if valid, error message if invalid
     */
    fun validateRequiredWhenPresent(
        parentPresent: Boolean,
        requiredFieldValue: Int,
        fieldName: String
    ): String? {
        if (parentPresent && requiredFieldValue == 0) {
            return "$fieldName is required when condition is present"
        }
        return null
    }
    
    /**
     * Common enum values for frequency
     */
    val FREQUENCY_VALUES = listOf("raro", "occasionale", "frequente", "costante")
    
    /**
     * Common enum values for intensity
     */
    val INTENSITY_VALUES = listOf("lieve", "moderata", "grave")
    
    /**
     * Common enum values for side/laterality
     */
    val LATO_VALUES = listOf("destro", "sinistro", "bilaterale")
    
    /**
     * Common enum values for sleep quality
     */
    val QUALITA_SONNO_VALUES = listOf("ottima", "buona", "scarsa", "pessima")
    
    /**
     * Common enum values for duration
     */
    val DURATA_VALUES = listOf("acuto", "subacuto", "cronico")
}

