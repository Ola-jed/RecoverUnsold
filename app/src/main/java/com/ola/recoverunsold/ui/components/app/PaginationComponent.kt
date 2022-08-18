package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Page

@Composable
fun PaginationComponent(
    modifier: Modifier = Modifier,
    page: Page<*>,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    if (page.pageNumber > 1 && page.hasNext) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(modifier = Modifier.fillMaxWidth(0.40F), onClick = onPrevious) {
                Text(stringResource(id = R.string.previous))
            }

            Button(modifier = Modifier.fillMaxWidth(0.40F), onClick = onNext) {
                Text(stringResource(id = R.string.next))
            }
        }
    } else if (page.pageNumber > 1) {
        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClick = onPrevious
        ) {
            Text(stringResource(id = R.string.previous))
        }
    } else if (page.hasNext) {
        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClick = onNext
        ) {
            Text(stringResource(id = R.string.next))
        }
    }
}