package com.ola.recoverunsold.ui.components.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.CustomerOrderStats
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomerOrderStatsComponent(
    modifier: Modifier = Modifier,
    customerOrderStats: CustomerOrderStats
) {
    val orders = customerOrderStats.totalOrders
    val amount = customerOrderStats.totalOrdersAmount

    Card(
        modifier = modifier,
        elevation = 20.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.my_orders),
                style = MaterialTheme.typography.h6
            )

            Divider(
                modifier = Modifier.padding(bottom = 15.dp),
                thickness = 2.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5F)
                        .weight(1F)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        text = pluralStringResource(
                            id = R.plurals.orders_made_number,
                            count = orders,
                            orders
                        ),
                        maxLines = 2
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5F)
                        .weight(1F)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        text = stringResource(
                            id = R.string.cumulative_order_value,
                            amount.formatWithoutTrailingZeros()
                        ),
                        maxLines = 4
                    )
                }
            }
        }
    }
}