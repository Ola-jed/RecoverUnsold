package com.ola.recoverunsold.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.misc.logout
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

// TODO : nav

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
                .background(MaterialTheme.colors.secondary)
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Column {
                        Text(user.username, color = MaterialTheme.colors.onSecondary)
                        Text(user.email, color = MaterialTheme.colors.onSecondary)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Edit, contentDescription = null)
                    }
                }
            }
        }
        DrawerNavRow(
            navController = navController,
            route = Routes.Home.path,
            text = R.string.home,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = null)
            },
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Distributors.path,
            text = R.string.distributors,
            leadingIcon = {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
            },
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Offers.path,
            text = R.string.offers,
            leadingIcon = {
                Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null)
            },
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Login.path,
            text = R.string.orders,
            leadingIcon = {
                Icon(imageVector = Icons.Default.History, contentDescription = null)
            },
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Register.path,
            text = R.string.settings,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null)
            },
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Spacer(Modifier.weight(4f))
        Row(modifier = Modifier
            .clickable {
                coroutineScope.launch {
                    context.logout()
                    navController.navigate(Routes.Home.path) {
                        popUpTo(Routes.Home.path) {
                            inclusive = true
                        }
                    }
                    snackbarHostState.showSnackbar(
                        Strings.get(R.string.logout_successfull),
                        Strings.get(R.string.ok),
                        duration = SnackbarDuration.Long
                    )
                }
            }
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 15.dp)
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