package com.narde.gestionaleosteopatabetto.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.narde.gestionaleosteopatabetto.utils.DateUtils

/**
 * Improved date input component for Italian DD/MM/AAAA format
 * Features:
 * - Automatic formatting as user types
 * - Input validation with visual feedback
 * - Age calculation display
 * - Better UX with numeric keyboard
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItalianDateInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "15/03/1985",
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    showAge: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    // Convert input value to display format
    val initialDisplayValue = remember(value) {
        if (value.isNotEmpty() && !value.contains("/")) {
            // Convert ISO format to Italian format if needed
            DateUtils.convertIsoToItalianFormat(value).takeIf { it.isNotEmpty() } ?: value
        } else {
            value
        }
    }
    
    // State for managing the formatted input with cursor position
    var textFieldValue by remember(initialDisplayValue) { 
        mutableStateOf(TextFieldValue(initialDisplayValue, TextRange(initialDisplayValue.length)))
    }
    
    // Calculate age if date is valid
    val age = remember(textFieldValue.text) {
        if (textFieldValue.text.isNotEmpty()) {
            DateUtils.calculateAgeFromItalianDate(textFieldValue.text)
        } else null
    }
    
    // Determine if current input has errors
    val hasError = isError || (textFieldValue.text.isNotEmpty() && !DateUtils.isValidItalianDate(textFieldValue.text))
    
    Column(modifier = modifier) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                val oldText = textFieldValue.text
                val newText = newValue.text
                
                // Calculate the formatted text
                val formattedText = when {
                    newText.isEmpty() -> ""
                    newText.length > oldText.length -> {
                        // User is adding characters - apply formatting
                        DateUtils.formatDateInput(newText)
                    }
                    else -> {
                        // User is deleting - allow direct editing but remove invalid characters
                        newText.filter { it.isDigit() || it == '/' }
                    }
                }
                
                // Calculate cursor position after formatting
                val newCursorPosition = when {
                    formattedText.isEmpty() -> 0
                    formattedText.length > oldText.length -> {
                        // Text grew - place cursor at end
                        formattedText.length
                    }
                    else -> {
                        // Text shrunk or stayed same - maintain relative position
                        minOf(newValue.selection.end, formattedText.length)
                    }
                }
                
                textFieldValue = TextFieldValue(
                    text = formattedText,
                    selection = TextRange(newCursorPosition)
                )
                onValueChange(formattedText)
            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            isError = hasError,
            supportingText = {
                when {
                    // Show error message if provided or validation fails
                    hasError && errorMessage != null -> {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    hasError && textFieldValue.text.isNotEmpty() -> {
                        Text(
                            text = "Formato non valido. Utilizzare DD/MM/AAAA",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    // Show age if valid date and showAge is true
                    showAge && age != null -> {
                        Text(
                            text = "Età: $age ${if (age == 1) "anno" else "anni"}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = keyboardActions,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Composable function for date input with age calculation display
 * Simplified version for basic use cases
 */
@Composable
fun DateInputWithAge(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    ItalianDateInput(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        showAge = true,
        keyboardActions = keyboardActions
    )
}

/**
 * Extension function for handling tab key navigation
 * Maintains the previous tab navigation functionality
 */
@Composable
fun Modifier.handleTabKeyNavigation(): Modifier {
    return this // Placeholder for future tab key handling if needed
}