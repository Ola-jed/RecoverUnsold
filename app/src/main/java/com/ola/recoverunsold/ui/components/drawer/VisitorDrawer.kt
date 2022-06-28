package com.ola.recoverunsold.ui.components.drawer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.navigation.Routes

@Composable
fun VisitorDrawer(navController: NavController): @Composable ColumnScope.() -> Unit {
    val configuration = LocalConfiguration.current
    return {
        Spacer(modifier = Modifier.height(configuration.screenHeightDp.dp / 5))
        DrawerNavRow(
            navController = navController,
            route = Routes.Home.path,
            text = R.string.home,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Home, contentDescription = null)
            },
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Distributors.path,
            text = R.string.distributors,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = null)
            },
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Offers.path,
            text = R.string.offers,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.ShoppingBag, contentDescription = null)
            },
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Login.path,
            text = R.string.login_action,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Login, contentDescription = null)
            },
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.Register.path,
            text = R.string.register_action,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.AppRegistration, contentDescription = null)
            },
            modifier = Modifier.padding(vertical = 5.dp)
        )
        DrawerNavRow(
            navController = navController,
            route = Routes.About.path,
            text = R.string.about,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
            },
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }
}