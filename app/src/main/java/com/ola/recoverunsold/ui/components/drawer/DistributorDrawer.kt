package com.ola.recoverunsold.ui.components.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.ui.components.account.UserAccountHeader
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.misc.logout
import kotlinx.coroutines.launch

@Composable
fun DistributorDrawer(
    user: Distributor,
    navController: NavController
): @Composable ColumnScope.() -> Unit {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    return {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colors.secondary,
                            MaterialTheme.colors.secondaryVariant
                        )
                    )
                )
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp)
        ) {
            Column {
                UserAccountHeader(
                    id = user.createdAt.toString(),
                    name = user.username,
                    size = configuration.screenHeightDp.dp / 10,
                    textStyle = TextStyle(fontSize = 20.sp)
                )

                Text(
                    user.username,
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Text(
                    user.email,
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            DrawerNavRow(
                navController = navController,
                route = Routes.Home.path,
                text = R.string.home,
                leadingIcon = Icons.Outlined.Home,
                modifier = Modifier.padding(bottom = 5.dp, top = 10.dp)
            )
            DrawerNavRow(
                navController = navController,
                route = Routes.DistributorOffers.path,
                text = R.string.offers_published,
                leadingIcon = Icons.Outlined.ShoppingBag,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            DrawerNavRow(
                navController = navController,
                route = Routes.DistributorOrdersReceived.path,
                text = R.string.orders_received,
                leadingIcon = Icons.Outlined.History,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            DrawerNavRow(
                navController = navController,
                route = Routes.DistributorAccount.path,
                text = R.string.account,
                leadingIcon = Icons.Outlined.Person,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            DrawerNavRow(
                navController = navController,
                route = Routes.About.path,
                text = R.string.about,
                leadingIcon = Icons.Outlined.Info,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            Spacer(Modifier.weight(4f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            coroutineScope.launch {
                                context.logout()
                                navController.navigate(Routes.Login.path) {
                                    popUpTo(Routes.Login.path) { inclusive = true }
                                }
                            }
                        }
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp, start = 15.dp)
                ) {
                    Icon(
                        Icons.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 15.dp)
                    )
                    Text(stringResource(R.string.logout_label))
                }
            }
        }
    }
}