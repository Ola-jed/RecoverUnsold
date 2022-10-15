package com.ola.recoverunsold.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

object AppCustomColors {
    val warning: Color
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) themeDarkWarning else themeLightWarning

    val onWarning: Color
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) themeDarkOnWarning else themeLightOnWarning

    val success: Color
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) themeDarkSuccess else themeLightSuccess

    val onSuccess: Color
        @Composable
        @ReadOnlyComposable
        get() = if (isSystemInDarkTheme()) themeDarkOnSuccess else themeLightOnSuccess
}