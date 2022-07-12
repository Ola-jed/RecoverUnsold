package com.ola.recoverunsold.ui.components.offer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.LatLong
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.OfferWithRelativeDistance
import com.ola.recoverunsold.ui.components.app.ConfirmDialog
import com.ola.recoverunsold.ui.components.app.ImageSlider
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.formatDateTime
import java.util.Date

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

    Surface(
        modifier = modifier.clickable { onMoreInformationRequest() },
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            if (!productsImagesUris.isNullOrEmpty()) {
                ImageSlider(
                    modifier = Modifier.fillMaxWidth(),
                    imageUris = productsImagesUris
                )
            }

            Text(
                text = stringResource(R.string.total_amount, offer.price.toString()),
                modifier = Modifier.padding(top = 10.dp)
            )

            if (offer.beneficiaries != null) {
                Text(
                    text = stringResource(
                        R.string.offer_beneficiaries_data,
                        offer.beneficiaries
                    )
                )
            }

            Text(
                text = stringResource(
                    R.string.start_date_time,
                    startDate.formatDateTime()
                )
            )

            Text(
                text = stringResource(
                    R.string.published_the,
                    offer.createdAt.formatDate()
                ),
                modifier = Modifier.padding(top = 5.dp)
            )

            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = onMoreInformationRequest) {
                    Text(text = stringResource(id = R.string.view_more))
                }

                Button(onClick = onMapShowRequest) {
                    Text(text = stringResource(id = R.string.view_location_on_maps))
                }
            }
        }
    }
}

@Preview
@Composable
fun preview() {
    val offerWithDistance = OfferWithRelativeDistance(
        offer = Offer(
            id = "XXX",
            startDate = Date(),
            duration = 1500UL,
            price = 50.5,
            beneficiaries = 5,
            createdAt = Date(),
            distributorId = "XXX",
            location = Location(
                id = "XXX",
                name = "name",
                coordinates = LatLong.zero(),
                indication = "Ayaaa",
                image = "",
                createdAt = Date()
            ),
            products = listOf()
        ),
        distance = 54.0
    )
}