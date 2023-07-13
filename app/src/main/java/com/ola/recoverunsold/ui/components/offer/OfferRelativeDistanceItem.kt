package com.ola.recoverunsold.ui.components.offer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.OfferWithRelativeDistance
import com.ola.recoverunsold.ui.components.app.ImageSlider
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import androidx.compose.ui.platform.LocalConfiguration as LocalConfiguration1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferRelativeDistanceItem(
    modifier: Modifier = Modifier,
    offerWithRelativeDistance: OfferWithRelativeDistance,
    onMoreInformationRequest: () -> Unit,
    onMapShowRequest: () -> Unit
) {
    val offer = offerWithRelativeDistance.offer
    val startDate = offer.startDate
    val productsImagesUris = offer.products?.flatMap { it.images }?.map { it.url }
    val height = (LocalConfiguration1.current.screenHeightDp * 0.2).dp

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = onMoreInformationRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            if (!productsImagesUris.isNullOrEmpty()) {
                ImageSlider(
                    modifier = Modifier.fillMaxWidth(),
                    imageHeight = height,
                    imageUris = productsImagesUris
                )
            }

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
                    R.string.km_away,
                    String.format("%2f", offerWithRelativeDistance.distance)
                ),
                icon = Icons.Default.Route
            )

            OfferInformationLine(
                modifier = Modifier.padding(top = 5.dp),
                text = stringResource(
                    R.string.published_the,
                    offer.createdAt.formatDate()
                ),
                icon = Icons.Default.CalendarToday
            )

            Button(onClick = onMapShowRequest, modifier = Modifier.align(Alignment.End)) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}