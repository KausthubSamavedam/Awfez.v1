package com.example.myapplicationoh.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val PrimaryBlue = Color(0xFF3B5BDB)
val PrimaryBlueDark = Color(0xFF2F4AC0)
val LightBlueBackground = Color(0xFFEEF2FF)
val BlueGradientStart = Color(0xFF4C6EF5)
val BlueGradientEnd = Color(0xFF3B5BDB)
val AdminDark = Color(0xFF1A1F36)
val AdminCard = Color(0xFF252A42)

val TextPrimary = Color(0xFF1A1F36)
val TextSecondary = Color(0xFF6B7280)
val TextHint = Color(0xFFADB5BD)

val BackgroundLight = Color(0xFFF8F9FA)
val CardWhite = Color(0xFFFFFFFF)
val DividerColor = Color(0xFFE9ECEF)

val StatusPending = Color(0xFFFF9500)
val StatusInProgress = Color(0xFF3B5BDB)
val StatusAssigned = Color(0xFF339AF0)
val StatusResolved = Color(0xFF40C057)
val StatusToday = Color(0xFF3B5BDB)
val StatusTomorrow = Color(0xFFFF9500)

val ErrorRed = Color(0xFFFA5252)
val ErrorRedLight = Color(0xFFFFF5F5)
val SuccessGreen = Color(0xFF40C057)
val SuccessGreenLight = Color(0xFFF0FFF4)

val TimeSlotSelected = Color(0xFF3B5BDB)
val TimeSlotDisabled = Color(0xFFE9ECEF)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = LightBlueBackground,
    onPrimaryContainer = PrimaryBlue,
    secondary = Color(0xFF748FFC),
    background = BackgroundLight,
    surface = CardWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ErrorRed,
    outline = DividerColor
)

@Composable
fun OfficeHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = OfficeHubTypography,
        content = content
    )
}