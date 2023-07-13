package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    coroutineScope: CoroutineScope,
    canGoBack: Boolean = false,
    drawerState: DrawerState,
    navController: NavController? = null,
    title: String? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    if (canGoBack) {
        require(navController != null) { "You are able to go back, so the navController should be passed" }
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                title ?: stringResource(id = R.string.app_name),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        navigationIcon = {
            if (!canGoBack) {
                IconButton(onClick = {
                    coroutineScope.launch { drawerState.open() }
                }) {
                    Icon(Icons.Default.Menu, contentDescription = null)
                }
            } else {
                IconButton(onClick = { navController?.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        },
        actions = actions
    )
}