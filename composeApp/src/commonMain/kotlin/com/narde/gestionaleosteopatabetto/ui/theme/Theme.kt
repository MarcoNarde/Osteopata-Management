package com.narde.gestionaleosteopatabetto.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * Light color scheme using the custom teal palette
 * This creates a professional, calming theme suitable for healthcare applications
 */
private val LightTealColorScheme = lightColorScheme(
    // Primary colors - main teal theme
    primary = TealPrimary,
    onPrimary = OnTealPrimary,
    primaryContainer = TealPrimaryLight,
    onPrimaryContainer = TealPrimaryDark,
    
    // Secondary colors - supporting teal shades
    secondary = TealSecondary,
    onSecondary = OnTealSecondary,
    secondaryContainer = TealSecondaryLight,
    onSecondaryContainer = TealSecondaryDark,
    
    // Tertiary colors - using primary variants
    tertiary = TealPrimaryDark,
    onTertiary = OnTealPrimary,
    tertiaryContainer = TealSurfaceVariant,
    onTertiaryContainer = TealPrimaryDark,
    
    // Error colors
    error = TealError,
    onError = TealOnError,
    errorContainer = TealErrorContainer,
    onErrorContainer = TealOnErrorContainer,
    
    // Background and surface colors
    background = TealBackground,
    onBackground = OnTealBackground,
    surface = TealSurface,
    onSurface = OnTealSurface,
    surfaceVariant = TealSurfaceVariant,
    onSurfaceVariant = OnTealSurface,
    
    // Outline colors
    outline = TealOutline,
    outlineVariant = TealOutlineVariant,
    scrim = TealScrim,
    
    // Inverse colors for dark elements
    inverseSurface = TealInverseSurface,
    inverseOnSurface = TealInverseOnSurface,
    inversePrimary = TealInversePrimary
)

/**
 * Dark color scheme using the custom teal palette
 * Currently uses the same colors as light theme - can be customized later
 */
private val DarkTealColorScheme = darkColorScheme(
    // For now, using similar colors to light theme
    // This can be customized later with darker variants
    primary = TealInversePrimary,
    onPrimary = TealPrimaryDark,
    primaryContainer = TealPrimaryDark,
    onPrimaryContainer = TealInversePrimary,
    
    secondary = TealSecondaryLight,
    onSecondary = TealSecondaryDark,
    secondaryContainer = TealSecondaryDark,
    onSecondaryContainer = TealSecondaryLight,
    
    tertiary = TealInversePrimary,
    onTertiary = TealPrimaryDark,
    tertiaryContainer = TealPrimaryDark,
    onTertiaryContainer = TealInversePrimary,
    
    error = TealError,
    onError = TealOnError,
    errorContainer = TealErrorContainer,
    onErrorContainer = TealOnErrorContainer,
    
    background = TealInverseSurface,
    onBackground = TealInverseOnSurface,
    surface = TealInverseSurface,
    onSurface = TealInverseOnSurface,
    surfaceVariant = TealPrimaryDark,
    onSurfaceVariant = TealInverseOnSurface,
    
    outline = TealOutline,
    outlineVariant = TealOutlineVariant,
    scrim = TealScrim,
    
    inverseSurface = TealSurface,
    inverseOnSurface = OnTealSurface,
    inversePrimary = TealPrimary
)

/**
 * Custom Material Theme using the teal color palette
 * 
 * @param darkTheme Whether to use dark theme colors
 * @param content The composable content to apply the theme to
 */
@Composable
fun OsteopathTealTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkTealColorScheme
    } else {
        LightTealColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography, // Using default typography
        content = content
    )
}