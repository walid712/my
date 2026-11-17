package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = EmeraldOnPrimary,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = EmeraldOnContainer,
    secondary = TerracottaSecondary,
    onSecondary = Color.White,
    secondaryContainer = TerracottaContainer,
    onSecondaryContainer = TerracottaOnContainer,
    tertiary = SaffronTertiary,
    onTertiary = Color.White,
    tertiaryContainer = SaffronContainer,
    onTertiaryContainer = SaffronOnContainer,
    background = LimestoneBackground,
    onBackground = ObsidianOnSurface,
    surface = LimestoneSurface,
    onSurface = ObsidianOnSurface,
    surfaceVariant = LimestoneSurfaceVariant,
    onSurfaceVariant = ObsidianOnSurfaceVariant,
    surfaceContainerLow = LimestoneCard,
    outline = WarmOutline,
    error = ErrorCrimson
)

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimaryDark,
    onPrimary = EmeraldOnPrimaryDark,
    primaryContainer = EmeraldContainerDark,
    onPrimaryContainer = EmeraldOnContainerDark,
    secondary = TerracottaSecondaryDark,
    onSecondary = Color(0xFF4F1A06),
    secondaryContainer = TerracottaContainerDark,
    onSecondaryContainer = TerracottaOnContainerDark,
    tertiary = SaffronTertiaryDark,
    onTertiary = Color(0xFF452B00),
    tertiaryContainer = SaffronContainerDark,
    onTertiaryContainer = SaffronOnContainerDark,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainerLow = DarkCard,
    outline = DarkOutline,
    error = ErrorCrimsonDark
)

val MizanShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun MizanMarocTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = MizanShapes,
        content = content
    )
}
