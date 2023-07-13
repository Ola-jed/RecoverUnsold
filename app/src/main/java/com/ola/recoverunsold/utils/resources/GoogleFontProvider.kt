package com.ola.recoverunsold.utils.resources

import androidx.compose.ui.text.googlefonts.GoogleFont
import com.ola.recoverunsold.R

val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)