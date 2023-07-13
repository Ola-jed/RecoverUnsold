package com.ola.recoverunsold.ui.components.app

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * A slider to show local images
 */
@Composable
fun LocalImagesList(
    modifier: Modifier = Modifier,
    imageUris: List<Uri>,
    onDelete: (Uri) -> Unit
) {
    LazyRow(modifier = modifier) {
        items(items = imageUris) { uri ->
            LocalImageItem(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(150.dp),
                uri = uri,
                onDelete = { onDelete(uri) }
            )
        }
    }
}

/**
 * A component to show a local image
 */
@Composable
fun LocalImageItem(
    modifier: Modifier = Modifier,
    uri: Uri,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images
            .Media
            .getBitmap(context.contentResolver, uri)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }

    Surface(
        modifier = modifier.clip(RoundedCornerShape(5.dp)),
        color = Color.Black.copy(alpha = 0.3F)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
            )

            IconButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = onDelete
            ) {
                Icon(
                    Icons.Default.RemoveCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}