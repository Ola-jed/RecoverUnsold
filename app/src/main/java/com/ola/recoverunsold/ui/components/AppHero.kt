package com.ola.recoverunsold.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R

@Composable
fun AppHero(modifier: Modifier = Modifier, text: String) {
    val painter = painterResource(id = R.mipmap.ic_launcher_foreground)
    Surface(
        modifier = modifier,
        elevation = 50.dp,
        color = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 70.dp,
            bottomEnd = 0.dp
        )
    ) {
        Column {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .defaultMinSize(minWidth = 100.dp, minHeight = 100.dp)
                    .padding(top = 50.dp, bottom = 15.dp),
                painter = painter,
                contentDescription = null
            )

            Text(
                text,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 20.dp, bottom = 30.dp),
                style = MaterialTheme.typography.h6
            )
        }
    }
}