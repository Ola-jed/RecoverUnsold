package com.ola.recoverunsold.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.ola.recoverunsold.utils.resources.googleFontProvider

@OptIn(ExperimentalTextApi::class)
private val appFontFamily = FontFamily(
    Font(googleFont = GoogleFont("Open Sans"), fontProvider = googleFontProvider)
)

val Typography = Typography(
    defaultFontFamily = appFontFamily,
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)