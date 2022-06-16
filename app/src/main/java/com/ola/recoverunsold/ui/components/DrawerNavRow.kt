package com.ola.recoverunsold.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DrawerNavRow(
    navController: NavController,
    route: String,
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val isCurrent = route == navController.currentBackStackEntry?.destination?.route

    val bgColor: Color
    val textColor: Color
    val modifierToApply: Modifier
    if (isCurrent) {
        bgColor = MaterialTheme.colors.primary.copy(alpha = 0.5f)
        textColor = MaterialTheme.colors.onPrimary
        modifierToApply = modifier
            .background(bgColor)
            .fillMaxWidth()
            .padding(start = 5.dp, top = 15.dp, bottom = 15.dp)
    } else {
        bgColor = Color.Transparent
        textColor = MaterialTheme.colors.onBackground
        modifierToApply = modifier
            .background(bgColor)
            .fillMaxWidth()
            .clickable { navController.navigate(route) }
            .padding(start = 5.dp, top = 15.dp, bottom = 15.dp)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifierToApply
    ) {
        if (leadingIcon != null) {
            leadingIcon()
        }

        Text(
            stringResource(id = text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp),
            textAlign = textAlign,
            color = textColor
        )
    }
}