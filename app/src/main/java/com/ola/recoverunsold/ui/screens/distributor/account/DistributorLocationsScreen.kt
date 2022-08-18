package com.ola.recoverunsold.ui.screens.distributor.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.PaginationComponent
import com.ola.recoverunsold.ui.components.location.LocationItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.LocationsSectionViewModel
import com.ola.recoverunsold.utils.misc.jsonSerialize
import com.ola.recoverunsold.utils.misc.remove
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@Composable
fun DistributorLocationsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    locationsSectionViewModel: LocationsSectionViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier) {
        when (locationsSectionViewModel.locationsGetResponse.status) {
            ApiStatus.LOADING -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            ApiStatus.ERROR -> {
                LaunchedEffect(snackbarHostState) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = locationsSectionViewModel.errorMessage()
                                ?: Strings.get(R.string.unknown_error_occured)
                        )
                    }
                }
            }
            else -> {
                val locations = locationsSectionViewModel.locationsGetResponse.data!!

                Scaffold(floatingActionButton = {
                    FloatingActionButton(onClick = {
                        navController.navigate(
                            Routes.LocationCreateOrUpdate.path.remove("{location}")
                        )
                    }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }) {
                    if (locations.items.isEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                stringResource(R.string.no_location_create_one),
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(items = locations.items) { item ->
                                LocationItem(
                                    modifier = Modifier
                                        .fillParentMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 10.dp),
                                    location = item,
                                    isModifiable = true,
                                    onEdit = {
                                        navController.navigate(
                                            Routes.LocationCreateOrUpdate.path.replace(
                                                "{location}",
                                                item.jsonSerialize()
                                            )
                                        )
                                    },
                                    onDelete = {
                                        locationsSectionViewModel.deleteLocation(
                                            location = item,
                                            onSuccess = {
                                                coroutineScope.launch {
                                                    snackbarHostState.show(
                                                        message = Strings.get(R.string.location_deleted_successfully)
                                                    )
                                                }
                                                locationsSectionViewModel.getLocations()
                                            },
                                            onFailure = {
                                                coroutineScope.launch {
                                                    snackbarHostState.show(
                                                        message = Strings.get(R.string.location_deletion_failed)
                                                    )
                                                }
                                            }
                                        )
                                    }
                                )
                            }
                            item {
                                PaginationComponent(
                                    modifier = Modifier.fillMaxWidth(),
                                    page = locations,
                                    onPrevious = { locationsSectionViewModel.getPrevious() },
                                    onNext = { locationsSectionViewModel.getNext() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}