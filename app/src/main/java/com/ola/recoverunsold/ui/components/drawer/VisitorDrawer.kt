package com.ola.recoverunsold.ui.components.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AppRegistration
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
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
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(configuration.screenHeightDp.dp / 5))
            DrawerNavRow(
                navController = navController,
                route = Routes.Home.path,
                text = R.string.home,
                leadingIcon = Icons.Outlined.Home,
                modifier = Modifier.padding(vertical = 5.dp)
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
                route = Routes.Login.path,
                text = R.string.login_action,
                leadingIcon = Icons.Outlined.Login,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            DrawerNavRow(
                navController = navController,
                route = Routes.Register.path,
                text = R.string.register_action,
                leadingIcon = Icons.Outlined.AppRegistration,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            DrawerNavRow(
                navController = navController,
                route = Routes.About.path,
                text = R.string.about,
                leadingIcon = Icons.Outlined.Info,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }
}