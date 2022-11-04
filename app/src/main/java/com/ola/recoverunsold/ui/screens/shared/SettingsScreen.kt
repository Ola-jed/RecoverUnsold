package com.ola.recoverunsold.ui.screens.shared

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.ola.recoverunsold.BuildConfig
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                title = stringResource(id = R.string.settings)
            )
        },
        drawerContent = DrawerContent(navController)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.general),
                modifier = Modifier.padding(start = 15.dp, top = 10.dp),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colors.primary
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.theme),
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 18.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.language),
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 18.sp
                )
            }
            Divider()
            Text(
                text = stringResource(id = R.string.about),
                modifier = Modifier.padding(start = 15.dp, top = 10.dp),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colors.primary
            )
            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Routes.About.path) }
                .padding(vertical = 15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.about_us),
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 18.sp
                )
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                }
                .padding(vertical = 15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.open_source_licenses),
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 18.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
            ) {
                Text(
                    text = "${stringResource(id = R.string.build_version)} : ${BuildConfig.VERSION_CODE}",
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 18.sp
                )
            }
        }
    }
}