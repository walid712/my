package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = EmeraldOnPrimary,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = OnEmeraldContainer,
    secondary = TerracottaSecondary,
    onSecondary = TerracottaOnSecondary,
    secondaryContainer = TerracottaContainer,
    onSecondaryContainer = OnTerracottaContainer,
    tertiary = BrassTertiary,
    onTertiary = BrassOnTertiary,
    tertiaryContainer = BrassContainer,
    onTertiaryContainer = OnBrassContainer,
    background = AlabasterBackground,
    onBackground = InkOnBackground,
    surface = PureCardSurface,
    onSurface = InkOnBackground,
    surfaceVariant = SoftLinenSurfaceVariant,
    onSurfaceVariant = MutedSlateText,
    outline = WarmOutline,
    error = ErrorCrimson,
    errorContainer = ErrorContainerLight
)

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldDarkPrimary,
    onPrimary = EmeraldDarkOnPrimary,
    primaryContainer = EmeraldDarkContainer,
    onPrimaryContainer = OnEmeraldDarkContainer,
    secondary = TerracottaDarkSecondary,
    onSecondary = TerracottaDarkOnSecondary,
    secondaryContainer = TerracottaDarkContainer,
    onSecondaryContainer = OnTerracottaDarkContainer,
    tertiary = BrassDarkTertiary,
    onTertiary = BrassDarkOnTertiary,
    tertiaryContainer = BrassDarkContainer,
    onTertiaryContainer = OnBrassDarkContainer,
    background = ObsidianBackground,
    onBackground = CreamOnDark,
    surface = ElevatedMossSurface,
    onSurface = CreamOnDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = SoftSageMuted,
    outline = DarkOutline
)

private val TrustCraftShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun TrustCraftTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isArabic: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = trustCraftTypography(isArabic = isArabic),
        shapes = TrustCraftShapes,
        content = content
    )
}
