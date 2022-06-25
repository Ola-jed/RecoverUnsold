package com.ola.recoverunsold.ui.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R

/**
 * A custom image picker with a preview
 */
@Composable
fun ImagePicker(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageData: MutableState<Uri?> = remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { imageData.value = it }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            launcher.launch(
                "image/*"
            )
        }) {
            Text(stringResource(R.string.pick_an_image))
            Icon(Icons.Default.CameraAlt, contentDescription = null)
        }
        imageData.let {
            val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
            val uri = it.value
            if (uri != null) {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, uri)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

                bitmap.value?.let { btm ->
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    )
                }
            }
        }
    }
}