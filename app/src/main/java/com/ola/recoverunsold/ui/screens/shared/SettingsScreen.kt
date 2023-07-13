package com.ola.recoverunsold.ui.screens.shared

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import com.ola.recoverunsold.ui.components.app.LocaleDialog
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.theme.ThemeDialog
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.SettingsViewModel
import com.ola.recoverunsold.ui.theme.ThemeMode
import com.ola.recoverunsold.utils.misc.Locale

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        content = {
            Scaffold(
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        title = stringResource(id = R.string.settings)
                    )
                }
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
                        color = MaterialTheme.colorScheme.primary
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { settingsViewModel.showThemeDialog = true }
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
                            .clickable { settingsViewModel.showLocaleDialog = true }
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
                        modifier = Modifier.padding(start = 15.dp, top = 25.dp),
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { uriHandler.openUri("https://github.com/Ola-jed/RecoverUnsold/") }
                            .padding(vertical = 15.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.open_source_label),
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 18.sp
                        )
                        Text(
                            text = stringResource(id = R.string.open_source_description),
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 18.sp
                        )
                    }
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            context.startActivity(
                                Intent(
                                    context,
                                    OssLicensesMenuActivity::class.java
                                )
                            )
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

                if (settingsViewModel.showThemeDialog) {
                    ThemeDialog(
                        current = settingsViewModel.theme ?: ThemeMode.Auto,
                        onChange = {
                            settingsViewModel.updateCurrentTheme(context, it)
                            settingsViewModel.showThemeDialog = false
                        },
                        onDismiss = { settingsViewModel.showThemeDialog = false }
                    )
                }

                if (settingsViewModel.showLocaleDialog) {
                    LocaleDialog(
                        current = settingsViewModel.currentLocale ?: Locale.Auto,
                        onChange = {
                            settingsViewModel.updateCurrentLocale(context, it)
                            settingsViewModel.showLocaleDialog = false
                        },
                        onDismiss = { settingsViewModel.showLocaleDialog = false }
                    )
                }
            }
        }
    )
}