package com.ola.recoverunsold.ui.components.app

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R

/**
 * A custom image picker with a preview
 */
@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    imageUri: Uri? = null,
    onImagePicked: (Uri) -> Unit
) {
    val context = LocalContext.current
    val imageData: MutableState<Uri?> =
        rememberSaveable { mutableStateOf(imageUri) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { imageData.value = it }


    Column {
        Text(
            stringResource(id = R.string.pick_an_image),
            modifier = Modifier.padding(start = 10.dp)
        )
        Surface(
            modifier = modifier
                .clip(RoundedCornerShape(5.dp))
                .clickable {
                    launcher.launch(
                        "image/*"
                    )
                },
            color = Color.Black.copy(alpha = 0.3F)
        ) {
            imageData.let {
                val bitmap: MutableState<Bitmap?> =
                    rememberSaveable { mutableStateOf(null) }
                val uri = it.value
                if (uri != null) {
                    onImagePicked(uri)
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value = MediaStore.Images
                            .Media
                            .getBitmap(context.contentResolver, uri)
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
                } else {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }
}