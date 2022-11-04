package com.ola.recoverunsold.ui.components.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.ui.components.app.ConfirmDialog

/**
 * The component to show information about a distributor's location
 * Can have edit and delete buttons if they are showed to their owner
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocationItem(
    modifier: Modifier = Modifier,
    location: Location,
    isModifiable: Boolean = false,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    Card(modifier = modifier, elevation = 15.dp, onClick = onClick) {
        Column {
            if (location.image != null) {
                val height = (LocalConfiguration.current.screenHeightDp * 0.2).dp
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(location.image)
                        .placeholder(R.mipmap.placeholder)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .height(height)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Text(
                location.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp, bottom = 10.dp)
            )

            if (location.indication != null) {
                Text(
                    location.indication,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 5.dp, bottom = 10.dp)
                )
            }

            if (isModifiable) {
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                    }
                    IconButton(onClick = { showDeleteConfirmationDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            tint = MaterialTheme.colors.error,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        if (showDeleteConfirmationDialog) {
            ConfirmDialog(
                title = stringResource(R.string.delete_location_label),
                content = stringResource(R.string.delete_location_question),
                onDismiss = { showDeleteConfirmationDialog = false },
                onConfirm = {
                    showDeleteConfirmationDialog = false
                    onDelete()
                },
                isDanger = true
            )
        }
    }
}