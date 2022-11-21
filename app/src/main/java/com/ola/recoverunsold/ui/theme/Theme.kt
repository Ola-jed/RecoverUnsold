package com.ola.recoverunsold.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        systemUiController.setSystemBarsColor(color = DarkColors.primaryVariant)
    } else {
        systemUiController.setSystemBarsColor(color = LightColors.primaryVariant)
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
