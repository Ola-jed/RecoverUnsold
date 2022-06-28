package com.ola.recoverunsold.ui.components.app

import androidx.annotation.StringRes
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@Composable
fun NavigationTextButton(
    navController: NavController,
    route: String,
    @StringRes text: Int? = null,
    content: String? = null,
    modifier: Modifier = Modifier,
    outerModifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    color: Color? = null,
    shouldRedirect: Boolean = true
) {
    require((text == null) xor (content == null))

    TextButton(onClick = {
        if (shouldRedirect) {
            navController.navigate(route)
        }
    }, modifier = outerModifier) {
        (content ?: text?.let { stringResource(it) })?.let {
            if (color != null) {
                Text(
                    it,
                    modifier = modifier,
                    textAlign = textAlign,
                    color = color
                )
            } else {
                Text(
                    it,
                    modifier = modifier,
                    textAlign = textAlign,
                )
            }
        }
    }
}