package com.ola.recoverunsold.ui.components.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.components.account.UserAccountHeader
import com.ola.recoverunsold.ui.navigation.Routes
import kotlin.random.Random

@Composable
fun VisitorDrawer(navController: NavController): @Composable ColumnScope.() -> Unit {
    val configuration = LocalConfiguration.current

    return {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.primary
                        )
                    )
                )
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 10.dp, start = 16.dp)
        ) {
            Column {
                UserAccountHeader(
                    email = "email${VisitorDrawerUtils.randomInteger}@email.com",
                    size = configuration.screenHeightDp.dp / 10
                )

                Text(
                    stringResource(id = R.string.guest),
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
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

object VisitorDrawerUtils {
    val randomInteger = Random.nextInt()
}