package com.example.learnready.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = CardWhite,
    primaryContainer = SoftBlue,
    onPrimaryContainer = DeepBlue,
    secondary = DeepBlue,
    onSecondary = CardWhite,
    secondaryContainer = SoftBlue.copy(alpha = 0.5f),
    onSecondaryContainer = DeepBlue,
    tertiary = AccentGold,
    onTertiary = CardWhite,
    tertiaryContainer = SoftGold,
    onTertiaryContainer = NavyBlue,
    background = BackgroundWhite,
    onBackground = TextPrimary,
    surface = CardWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundWhite,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = CardWhite,
    errorContainer = SoftRed,
    onErrorContainer = TextPrimary,
    outline = SurfaceBorder,
    outlineVariant = SurfaceBorder.copy(alpha = 0.5f),
    surfaceContainerHighest = BackgroundWhite,
    surfaceContainerHigh = BackgroundWhite,
    surfaceContainer = CardWhite,
    surfaceContainerLow = CardWhite,
    surfaceContainerLowest = CardWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = SoftBlue,
    onPrimary = DeepBlue,
    primaryContainer = DeepBlue,
    onPrimaryContainer = SoftBlue,
    secondary = PrimaryBlue,
    onSecondary = CardWhite,
    tertiary = AccentGold,
    onTertiary = NavyBlue,
    tertiaryContainer = SoftGold.copy(alpha = 0.3f),
    onTertiaryContainer = SoftGold,
    background = NavyBlue,
    onBackground = BackgroundWhite,
    surface = NavyBlue.copy(alpha = 0.8f),
    onSurface = BackgroundWhite,
    surfaceVariant = NavyBlue.copy(alpha = 0.6f),
    onSurfaceVariant = TextSecondary,
    error = SoftRed,
    onError = NavyBlue,
    outline = TextSecondary.copy(alpha = 0.3f),
    outlineVariant = TextSecondary.copy(alpha = 0.15f)
)

@Composable
fun LearnReadyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
