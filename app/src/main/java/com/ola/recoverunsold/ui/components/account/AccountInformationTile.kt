package com.ola.recoverunsold.ui.components.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccountInformationTile(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    label: String,
    data: String
) {
    Surface(
        elevation = 10.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(top = 5.dp, start = 5.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.Start),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                }
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = label
                )
            }
            Text(
                data,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}