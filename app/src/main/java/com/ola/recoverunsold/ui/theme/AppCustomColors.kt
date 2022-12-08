package com.ola.recoverunsold.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

val Colors.warning: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) themeDarkWarning else themeLightWarning

val Colors.onWarning: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) themeDarkOnWarning else themeLightOnWarning

val Colors.success: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) themeDarkSuccess else themeLightSuccess

val Colors.onSuccess: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) themeDarkOnSuccess else themeLightOnSuccess