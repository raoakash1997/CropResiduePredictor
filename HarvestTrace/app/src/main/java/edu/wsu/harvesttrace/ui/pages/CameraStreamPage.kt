package edu.wsu.harvesttrace.ui.pages

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import edu.wsu.harvesttrace.HarvestTraceRoutes
import edu.wsu.harvesttrace.LocalSharedState
import edu.wsu.harvesttrace.R
import edu.wsu.harvesttrace.services.StorageService
import edu.wsu.harvesttrace.ui.components.AppBar
import edu.wsu.harvesttrace.ui.components.camera.CameraPreview
import edu.wsu.harvesttrace.ui.components.GalleryImagePicker
import edu.wsu.harvesttrace.ui.viewmodels.CameraViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview
@Composable
fun CameraStreamPage() {

    val navController = LocalSharedState.current.navController
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val cameraViewModel: CameraViewModel = hiltViewModel(LocalContext.current as ComponentActivity)

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
        }
    }

    val onImageCapture = { image: Bitmap ->
        coroutineScope.launch(Dispatchers.IO) {
           val uri =  StorageService.INSTANCE.saveImageToStorage(context, image)
            cameraViewModel.setCapturedImageURI(uri)
        }

        val activity = context as? Activity
        if (activity != null) {
            activity.runOnUiThread {
                cameraViewModel.setCapturedImage(image)
                navController?.navigate(HarvestTraceRoutes.CAPTURE_PREVIEW_PAGE)
            }
        }

    }

    fun captureImage(
        controller: LifecycleCameraController,
        onImageCapture: (Bitmap) -> Unit
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )

                    onImageCapture(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }


    Scaffold(
        topBar = {
            AppBar()
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            CameraPreview(
                controller,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                GalleryImagePicker(modifier = Modifier.size(55.dp))
                Button(
                    onClick = {
                        captureImage(
                            controller = controller,
                            onImageCapture = onImageCapture
                        )
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(90.dp)
                ) {}
                Icon(
                    painter = painterResource(id = R.drawable.change_mode),
                    contentDescription = "Select FST mode",
                    modifier = Modifier
                        .size(55.dp)
                        .clickable(onClick = {
                            controller.cameraSelector =
                                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else CameraSelector.DEFAULT_BACK_CAMERA
                        })
                )
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }

}

