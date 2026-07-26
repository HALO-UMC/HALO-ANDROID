package com.umc.halo.presentation.storybook.chapter.component

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberSelectedImageBitmap(
    imageUri: String?
): ImageBitmap? {
    val context = LocalContext.current
    var imageBitmap by remember(imageUri) {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(imageUri) {
        imageBitmap = if (imageUri.isNullOrBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(Uri.parse(imageUri))?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                    }
                }.getOrNull()
            }
        }
    }

    return imageBitmap
}
