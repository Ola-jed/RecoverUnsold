package com.ola.recoverunsold.ui.components.offer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.ui.components.app.ConfirmDialog
import com.ola.recoverunsold.ui.components.app.ImageSlider
import com.ola.recoverunsold.utils.extensions.formatDate
import com.ola.recoverunsold.utils.extensions.formatDateTime
import com.ola.recoverunsold.utils.extensions.formatWithoutTrailingZeros

/**
 * A component to show information about an offer
 * Can have edit and delete buttons if they are showed to their owner
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferItem(
    modifier: Modifier = Modifier,
    offer: Offer,
    isEditable: Boolean = false,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onTap: () -> Unit
) {
    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    val startDate = offer.startDate
    val productsImagesUris = offer.products?.flatMap { it.images }?.map { it.url }
    val height = (LocalConfiguration.current.screenHeightDp * 0.2).dp

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = onTap
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!productsImagesUris.isNullOrEmpty()) {
                ImageSlider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    imageUris = productsImagesUris,
                    imageHeight = height,
                    rounded = false
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                OfferInformationLine(
                    modifier = Modifier.padding(top = 7.dp),
                    text = stringResource(
                        R.string.total_amount,
                        offer.price.formatWithoutTrailingZeros()
                    ),
                    icon = Icons.Default.Payments
                )

                if (offer.beneficiaries != null) {
                    OfferInformationLine(
                        modifier = Modifier.padding(top = 5.dp),
                        text = stringResource(
                            R.string.offer_beneficiaries_data,
                            offer.beneficiaries
                        ),
                        icon = Icons.Default.Group
                    )
                }

                OfferInformationLine(
                    modifier = Modifier.padding(top = 5.dp),
                    text = stringResource(
                        R.string.start_date_time,
                        startDate.formatDateTime()
                    ),
                    icon = Icons.Default.EventAvailable
                )

                OfferInformationLine(
                    modifier = Modifier.padding(top = 5.dp),
                    text = stringResource(
                        R.string.published_the,
                        offer.createdAt.formatDate()
                    ),
                    icon = Icons.Default.CalendarToday
                )

                if (isEditable) {
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
                                tint = MaterialTheme.colorScheme.error,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            if (showDeleteConfirmationDialog) {
                ConfirmDialog(
                    title = stringResource(R.string.delete_offer_label),
                    content = stringResource(R.string.delete_offer_question),
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
}