package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Component for subtitles with an Icon
 */
@Composable
fun SubtitleWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector,
            contentDescription = null,
            modifier = Modifier.padding(end = 3.dp)
        )

        Text(
            text = text,
            fontSize = 17.sp,
            modifier = Modifier.padding(vertical = 10.dp),
            fontWeight = FontWeight.Bold
        )
    }
}