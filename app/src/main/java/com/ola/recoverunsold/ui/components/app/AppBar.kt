package com.ola.recoverunsold.ui.components.app

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppBar(
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    canGoBack: Boolean = false,
    navController: NavController? = null,
    title: String? = null
) {
    if (canGoBack) {
        require(navController != null) { "You are able to go back, so the navController should be passed" }
    }

    TopAppBar(
        title = { Text(title ?: stringResource(id = R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = {
            if (!canGoBack) {
                IconButton(onClick = {
                    coroutineScope.launch { scaffoldState.drawerState.open() }
                }) {
                    Icon(Icons.Default.Menu, contentDescription = null)
                }
            } else {
                IconButton(onClick = { navController?.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        }
    )
}