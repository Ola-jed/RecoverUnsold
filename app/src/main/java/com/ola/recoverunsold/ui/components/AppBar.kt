package com.ola.recoverunsold.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    navController: NavController? = null
) {
    if (canGoBack) {
        require(navController != null)
    }

    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = {
            if (!canGoBack) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier.clickable(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    })
                )
            } else {
                IconButton(onClick = {
                    navController?.navigateUp()
                }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        }
    )
}