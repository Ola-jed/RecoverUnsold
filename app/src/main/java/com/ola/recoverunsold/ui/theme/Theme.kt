package com.ola.recoverunsold.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ola.recoverunsold.utils.store.ThemeObserver

const val THEME_PREFERENCES = "theme"
const val THEME_MODE = "theme-mode"

private val LightColors = lightColors(
    primary = themeLightPrimary,
    onPrimary = themeLightOnPrimary,
    secondary = themeLightSecondary,
    secondaryVariant = themeLightSecondaryVariant,
    onSecondary = themeLightOnSecondary,
    error = themeLightError,
    onError = themeLightOnError,
    background = themeLightBackground,
    onBackground = themeLightOnBackground,
    surface = themeLightSurface,
    onSurface = themeLightOnSurface,
)

private val DarkColors = darkColors(
    primary = themeDarkPrimary,
    onPrimary = themeDarkOnPrimary,
    secondary = themeDarkSecondary,
    secondaryVariant = themeDarkSecondaryVariant,
    onSecondary = themeDarkOnSecondary,
    error = themeDarkError,
    onError = themeDarkOnError,
    background = themeDarkBackground,
    onBackground = themeDarkOnBackground,
    surface = themeDarkSurface,
    onSurface = themeDarkOnSurface,
)

@Composable
fun RecoverUnsoldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val themeMode by ThemeObserver.themeMode.collectAsState()

    val colors = when (themeMode) {
        ThemeMode.Dark -> DarkColors
        ThemeMode.Light -> LightColors
        else -> if (darkTheme) DarkColors else LightColors
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = colors.primaryVariant)

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
