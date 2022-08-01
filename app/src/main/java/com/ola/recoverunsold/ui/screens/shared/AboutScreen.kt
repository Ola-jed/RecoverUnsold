package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.AppHero
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.screens.viewmodels.AboutViewModel
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AboutScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    aboutViewModel: AboutViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed),
        snackbarHostState = snackbarHostState
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = bottomSheetScaffoldState,
                title = stringResource(id = R.string.about)
            )
        },
        drawerContent = DrawerContent(navController, snackbarHostState),
        sheetContent = {
            val focusManager = LocalFocusManager.current

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTextInput(
                    modifier = Modifier.fillMaxWidth(0.8F),
                    label = { Text(text = stringResource(id = R.string.your_message)) },
                    value = aboutViewModel.message,
                    onValueChange = { aboutViewModel.message = it },
                    maxLines = 5,
                    singleLine = false,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Button(
                    modifier = Modifier.fillMaxWidth(0.8F),
                    onClick = {
                        if (aboutViewModel.message.isBlank()) {
                            coroutineScope.launch {
                                snackbarHostState.show(Strings.get(R.string.the_message_cannot_be_empty))
                            }
                        } else {
                            aboutViewModel.publishMessage()
                        }
                    }, enabled = aboutViewModel.apiCallResult.status != ApiStatus.LOADING
                ) {
                    if (aboutViewModel.apiCallResult.status == ApiStatus.LOADING) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = stringResource(id = R.string.send_message))
                    }
                }
            }

            if (aboutViewModel.apiCallResult.status == ApiStatus.SUCCESS) {
                coroutineScope.launch {
                    snackbarHostState.show(Strings.get(R.string.message_sent_successfully))
                }
            }

            if (aboutViewModel.errorMessage() != null) {
                coroutineScope.launch {
                    snackbarHostState.show(aboutViewModel.errorMessage()!!)
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetElevation = 25.dp,
        sheetGesturesEnabled = true,
        sheetShape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            AppHero(
                text = stringResource(id = R.string.about_us),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                text = stringResource(id = R.string.about_us_text),
                textAlign = TextAlign.Justify,
                fontSize = 15.sp
            )

            Button(
                onClick = {
                    coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
                }, modifier = Modifier
                    .padding(top = 25.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.send_us_a_message))

                Icon(imageVector = Icons.Default.Send, contentDescription = null)
            }
        }
    }
}