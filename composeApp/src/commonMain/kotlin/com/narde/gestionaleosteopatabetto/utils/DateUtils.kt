package com.narde.gestionaleosteopatabetto.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Utility functions for date handling and formatting
 * Supports Italian DD/MM/AAAA format conversion
 */
object DateUtils {
    
    /**
     * Converts a date string from DD/MM/AAAA format to LocalDate
     * @param dateString Date in DD/MM/AAAA format (e.g., "15/03/1985")
     * @return LocalDate object or null if parsing fails
     */
    fun parseItalianDate(dateString: String): LocalDate? {
        return try {
            if (dateString.isBlank()) return null
            
            val parts = dateString.split("/")
            if (parts.size != 3) return null
            
            val day = parts[0].toIntOrNull() ?: return null
            val month = parts[1].toIntOrNull() ?: return null
            val year = parts[2].toIntOrNull() ?: return null
            
            // Validate ranges
            if (day < 1 || day > 31) return null
            if (month < 1 || month > 12) return null
            if (year < 1900 || year > 2100) return null
            
            LocalDate(year, month, day)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Converts a LocalDate to Italian DD/MM/AAAA format
     * @param date LocalDate object
     * @return Date string in DD/MM/AAAA format (e.g., "15/03/1985")
     */
    fun formatToItalianDate(date: LocalDate): String {
        return "${date.dayOfMonth.toString().padStart(2, '0')}/${date.monthNumber.toString().padStart(2, '0')}/${date.year}"
    }
    
    /**
     * Converts ISO date string (YYYY-MM-DD) to Italian format (DD/MM/AAAA)
     * Used for migrating existing data
     * @param isoDateString Date in ISO format (e.g., "1985-03-15")
     * @return Date string in DD/MM/AAAA format or empty string if parsing fails
     */
    fun convertIsoToItalianFormat(isoDateString: String): String {
        return try {
            if (isoDateString.isBlank()) return ""
            val localDate = LocalDate.parse(isoDateString)
            formatToItalianDate(localDate)
        } catch (e: Exception) {
            ""
        }
    }
    
    /**
     * Converts Italian date format (DD/MM/AAAA) to ISO format (YYYY-MM-DD)
     * Used for database storage compatibility
     * @param italianDateString Date in DD/MM/AAAA format (e.g., "15/03/1985")
     * @return Date string in ISO format or empty string if parsing fails
     */
    fun convertItalianToIsoFormat(italianDateString: String): String {
        return try {
            if (italianDateString.isBlank()) return ""
            val localDate = parseItalianDate(italianDateString) ?: return ""
            localDate.toString()
        } catch (e: Exception) {
            ""
        }
    }
    
    /**
     * Calculates age from Italian date format
     * @param birthDateString Date in DD/MM/AAAA format
     * @return Age in years or null if parsing fails
     */
    fun calculateAgeFromItalianDate(birthDateString: String): Int? {
        return try {
            val birthDate = parseItalianDate(birthDateString) ?: return null
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            
            var age = today.year - birthDate.year
            if (today.monthNumber < birthDate.monthNumber || 
                (today.monthNumber == birthDate.monthNumber && today.dayOfMonth < birthDate.dayOfMonth)) {
                age--
            }
            age
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Validates Italian date format string
     * @param dateString Date string to validate
     * @return true if valid DD/MM/AAAA format, false otherwise
     */
    fun isValidItalianDate(dateString: String): Boolean {
        return parseItalianDate(dateString) != null
    }
    
    /**
     * Formats a date input string as user types (adds slashes automatically)
     * @param input Current input string
     * @return Formatted string with slashes
     */
    fun formatDateInput(input: String): String {
        // Remove all non-digits and limit to 8 digits max
        val digitsOnly = input.filter { it.isDigit() }.take(8)
        
        return when (digitsOnly.length) {
            0 -> ""
            1, 2 -> digitsOnly
            3, 4 -> "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2)}"
            5, 6 -> "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2, 4)}/${digitsOnly.substring(4)}"
            7 -> "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2, 4)}/${digitsOnly.substring(4)}"
            8 -> "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2, 4)}/${digitsOnly.substring(4, 8)}"
            else -> "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2, 4)}/${digitsOnly.substring(4, 8)}"
        }
    }
    
    /**
     * Gets current date in Italian format
     * @return Today's date in DD/MM/AAAA format
     */
    fun getCurrentDateInItalianFormat(): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return formatToItalianDate(today)
    }
}