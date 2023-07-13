package com.ola.recoverunsold.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

val ColorScheme.warning: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) themeDarkWarning else themeLightWarning

val ColorScheme.onWarning: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) themeDarkOnWarning else themeLightOnWarning

val ColorScheme.success: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) themeDarkSuccess else themeLightSuccess

val ColorScheme.onSuccess: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) themeDarkOnSuccess else themeLightOnSuccess