package edu.wsu.harvesttrace.ui.pages

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.wsu.harvesttrace.HarvestTraceRoutes
import edu.wsu.harvesttrace.models.SharedState
import edu.wsu.harvesttrace.services.StorageService
import edu.wsu.harvesttrace.ui.components.AppBar
import edu.wsu.harvesttrace.ui.components.camera.ConfirmDeleteAlertDialog
import edu.wsu.harvesttrace.ui.components.camera.ImagePreview
import edu.wsu.harvesttrace.ui.components.ErrorWidget
import edu.wsu.harvesttrace.ui.components.GalleryImagePicker
import edu.wsu.harvesttrace.ui.viewmodels.CameraViewModel
import edu.wsu.harvesttrace.LocalSharedState

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CapturePreviewPage() {
    val navController = LocalSharedState.current.navController
    val context = LocalContext.current

    val cameraViewModel = hiltViewModel<CameraViewModel>(LocalContext.current as ComponentActivity)
    val capturedImage by cameraViewModel.capturedImage.collectAsState()
    val capturedImageUri by cameraViewModel.capturedImageUri.collectAsState()


    var (showDeleteConfirmation, setShowDeleteConfirmation) = remember { mutableStateOf(false) }
    val handleImageDeletion = {
        if (capturedImageUri != null) {
            StorageService.INSTANCE.deleteImageInStorage(context, capturedImageUri)
        }
        navController?.navigate(HarvestTraceRoutes.CAMERA_STREAM_PAGE)
    }

    Scaffold(
        topBar = { AppBar() },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (capturedImage == null) {
                ErrorWidget("Image not found")
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    ImagePreview(capturedImage)
                    Spacer(modifier = Modifier.weight(1f))
                    ButtonsRow(onDeletePressed = { setShowDeleteConfirmation(true) })
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            navController?.navigate(HarvestTraceRoutes.RESULTS_PAGE)
                        }) {
                            Text(text = "Crop Residue")
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    if (showDeleteConfirmation) {
                        ConfirmDeleteAlertDialog(
                            onConfirm = { handleImageDeletion() },
                            onDismiss = { setShowDeleteConfirmation(false) })
                    }
                }

            }
        }
    }
}


@Composable
fun ButtonsRow(
    onDeletePressed: () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(2f))
        GalleryImagePicker(
            modifier = Modifier
                .height(50.dp)
                .width(40.dp)
        )
        Spacer(modifier = Modifier.weight(0.5f))
        Icon(
            Icons.Outlined.Delete,
            contentDescription = "Delete",
            modifier = Modifier
                .size(50.dp)
                .clickable(
                    onClick = onDeletePressed
                )
        )
        Spacer(modifier = Modifier.weight(2f))
    }
}
