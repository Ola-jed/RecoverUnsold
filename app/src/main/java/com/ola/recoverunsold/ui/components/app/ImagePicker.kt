package com.ola.recoverunsold.ui.components.app

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ola.recoverunsold.R

/**
 * A custom image picker with a preview
 */
@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    imageUri: Uri? = null,
    onImagePicked: (Uri) -> Unit,
    maxImageSize: Long = Long.MAX_VALUE
) {
    val context = LocalContext.current
    var imageUriData by rememberSaveable { mutableStateOf(imageUri) }
    var isChosenFileSizeInvalid by remember { mutableStateOf(false) }
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) {
        it?.let { uri ->
            val cursor = context.contentResolver.query(uri, null, null, null, null, null)
            cursor.use { crsr ->
                if (crsr != null && crsr.moveToFirst()) {
                    val sizeIndex: Int = crsr.getColumnIndex(OpenableColumns.SIZE)
                    if (!crsr.isNull(sizeIndex)) {
                        val imageSize = crsr.getString(sizeIndex).toLong()
                        android.util.Log.e("IMAGE PICKER", imageSize.toString())
                        isChosenFileSizeInvalid = imageSize > maxImageSize
                    }
                }
            }
        }
        if (it != null && !isChosenFileSizeInvalid) {
            imageUriData = it
            onImagePicked(it)
        }
    }

    Column {
        Text(
            stringResource(id = R.string.pick_an_image),
            modifier = Modifier.padding(start = 20.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Surface(
            modifier = modifier
                .clip(RoundedCornerShape(5.dp))
                .clickable { launcher.launch("image/*") },
            color = Color.Black.copy(alpha = 0.3F)
        ) {
            imageUriData.let {
                val uri = it
                if (uri != null) {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value = MediaStore.Images
                            .Media
                            .getBitmap(context.contentResolver, uri)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, uri)
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

    if (isChosenFileSizeInvalid) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
        val progress by animateLottieCompositionAsState(composition)

        AlertDialog(
            onDismissRequest = { isChosenFileSizeInvalid = false },
            confirmButton = {},
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.chosen_image_too_big),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(165.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(150.dp),
                        composition = composition,
                        progress = { progress }
                    )
                }
            },
        )
    }
}