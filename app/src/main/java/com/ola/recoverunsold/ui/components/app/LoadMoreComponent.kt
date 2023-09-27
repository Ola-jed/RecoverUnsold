package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Page

@Composable
fun LoadMoreComponent(
    modifier: Modifier = Modifier,
    page: Page<*>,
    onLoadMore: () -> Unit
) {
    if (page.hasNext) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = onLoadMore) {
                Text(stringResource(id = R.string.view_more))
            }
        }
    }
}