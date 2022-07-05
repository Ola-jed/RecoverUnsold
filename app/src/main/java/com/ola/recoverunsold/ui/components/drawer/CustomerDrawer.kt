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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.ui.components.account.UserAccountHeader
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.misc.logout
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@Composable
fun CustomerDrawer(
    user: Customer,
    navController: NavController,
    snackbarHostState: SnackbarHostState
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
                .padding(top = 16.dp, bottom = 10.dp, start = 16.dp)
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
        DrawerNavRow(
            navController = navController,
            route = Routes.Home.path,
            text = R.string.home,
            leadingIcon = Icons.Outlined.Home,
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Distributors.path,
            text = R.string.distributors,
            leadingIcon = Icons.Outlined.ShoppingCart,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Offers.path,
            text = R.string.offers,
            leadingIcon = Icons.Outlined.ShoppingBag,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Orders.path,
            text = R.string.orders,
            leadingIcon = Icons.Outlined.History,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.CustomerAccount.path,
            text = R.string.settings,
            leadingIcon = Icons.Outlined.Settings,
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
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    coroutineScope.launch {
                        context.logout()
                        navController.navigate(Routes.Home.path) {
                            popUpTo(Routes.Home.path) {
                                inclusive = true
                            }
                        }
                        snackbarHostState.show(
                            message = Strings.get(R.string.logout_successfull)
                        )
                    }
                }
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp, start = 24.dp)
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