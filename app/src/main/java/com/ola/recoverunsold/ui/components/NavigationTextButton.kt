package com.ola.recoverunsold.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController

@Composable
fun NavigationTextButton(
    navController: NavController,
    route: String,
    @StringRes text: Int? = null,
    content: String? = null,
) {
    require((text == null) xor (content == null))

    TextButton(onClick = {
        navController.navigate(route)
    }) {
        (content ?: text?.let { stringResource(it) })?.let { Text(it) }
    }
}