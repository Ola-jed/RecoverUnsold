package com.ola.recoverunsold.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ola.recoverunsold.utils.store.ThemeObserver

private val LightColorScheme = lightColorScheme(
    primary = themeLightPrimary,
    onPrimary = themeLightOnPrimary,
    secondary = themeLightSecondary,
    onSecondary = themeLightOnSecondary,
    error = themeLightError,
    onError = themeLightOnError,
    background = themeLightBackground,
    onBackground = themeLightOnBackground,
    surface = themeLightSurface,
    onSurface = themeLightOnSurface,
)

private val DarkColorScheme = darkColorScheme(
    primary = themeDarkPrimary,
    onPrimary = themeDarkOnPrimary,
    secondary = themeDarkSecondary,
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

    val colorScheme = when (themeMode) {
        ThemeMode.Dark -> DarkColorScheme
        ThemeMode.Light -> LightColorScheme
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = colorScheme.primary)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
