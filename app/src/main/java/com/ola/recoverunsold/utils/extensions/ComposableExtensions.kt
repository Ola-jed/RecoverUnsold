package com.ola.recoverunsold.utils.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ola.recoverunsold.R
import com.ola.recoverunsold.utils.resources.Strings

/**
 * Shortcut for snackbar showing
 * Because the duration is always long and the dismiss message "OK"
 */
suspend fun SnackbarHostState.show(message: String): SnackbarResult = showSnackbar(
    message = message,
    actionLabel = Strings.get(R.string.ok),
    duration = SnackbarDuration.Long
)

/**
 * Check whether a lazy list is being scrolled up
 */
@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }

    return remember(this) {
        derivedStateOf {
            ((previousIndex != firstVisibleItemIndex && previousIndex > firstVisibleItemIndex)
                    || previousScrollOffset >= firstVisibleItemScrollOffset).also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

