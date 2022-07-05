package com.ola.recoverunsold.ui.components.drawer

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    leadingIcon: ImageVector?
) {
    val isCurrent = route == navController.currentBackStackEntry?.destination?.route

    val bgColor: Color
    val textColor: Color
    val modifierToApply: Modifier
    if (isCurrent) {
        bgColor = MaterialTheme.colors.primary.copy(alpha = 0.7f)
        textColor = MaterialTheme.colors.onPrimary
        modifierToApply = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 15.dp, start = 15.dp)
    } else {
        bgColor = Color.Transparent
        textColor = MaterialTheme.colors.onBackground
        modifierToApply = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .fillMaxWidth()
            .clickable { navController.navigate(route) }
            .padding(top = 15.dp, bottom = 15.dp, start = 15.dp)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 7.dp)
    ) {
        Row(modifier = modifierToApply) {
            if (leadingIcon != null) {
                Icon(leadingIcon, contentDescription = null, tint = textColor)
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
}