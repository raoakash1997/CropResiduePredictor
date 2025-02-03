package edu.wsu.harvesttrace.ui.components.camera

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import edu.wsu.harvesttrace.ui.extensions.ignoreHorizontalParentPadding
import edu.wsu.harvesttrace.ui.theme.SCREEN_SIDE_PADDING

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    Box(modifier = modifier) {
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            },
            modifier = modifier
        )
    }
}


@Composable
fun ImagePreview(cameraFrame: Bitmap?) {

    Box(modifier = Modifier.ignoreHorizontalParentPadding(SCREEN_SIDE_PADDING)) {
        if (cameraFrame != null) {
            // TODO: Figure out a way to remove this hardcoded scaling.
            val scaledCameraFrame = Bitmap.createScaledBitmap(cameraFrame, 1080, 1440, true)
            Image(
                bitmap = scaledCameraFrame.asImageBitmap(),
                contentDescription = "Image preview",
            )
        }
    }
}
