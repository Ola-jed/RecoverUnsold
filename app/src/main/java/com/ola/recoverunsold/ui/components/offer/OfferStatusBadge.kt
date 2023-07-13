package com.ola.recoverunsold.ui.components.offer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.utils.misc.statusIcon
import com.ola.recoverunsold.utils.misc.statusLabel

@Composable
fun OfferStatusBadge(offer: Offer, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.5.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = offer.statusIcon(),
                contentDescription = null,
            )

            Text(
                modifier = Modifier.padding(start = 3.dp),
                text = offer.statusLabel()
            )
        }
    }
}