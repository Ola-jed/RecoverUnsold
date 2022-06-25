package com.ola.recoverunsold.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Location
import java.text.DateFormat
import java.util.Locale

@Composable
fun LocationItem(
    modifier: Modifier = Modifier,
    location: Location,
    isModifiable: Boolean = false,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(modifier = modifier, elevation = 15.dp, shape = RoundedCornerShape(20.dp)) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(
                location.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (location.image != null) {
                    val size = (LocalConfiguration.current.screenHeightDp * 0.1).dp
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(location.image)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .size(size)
                            .padding(end = 5.dp)
                            .clip(CircleShape),
                    )
                }
                if (location.indication != null) {
                    Text(location.indication)
                }
            }
            Text(
                stringResource(R.string.published_the) + " " + DateFormat
                    .getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
                    .format(location.createdAt),
                modifier = Modifier.padding(top = 5.dp)
            )
            if (isModifiable) {
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            tint = MaterialTheme.colors.error,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}