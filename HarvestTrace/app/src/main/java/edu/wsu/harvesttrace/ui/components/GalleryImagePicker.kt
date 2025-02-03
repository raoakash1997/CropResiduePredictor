package edu.wsu.harvesttrace.ui.components

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import edu.wsu.harvesttrace.HarvestTraceRoutes
import edu.wsu.harvesttrace.LocalSharedState
import edu.wsu.harvesttrace.R
import edu.wsu.harvesttrace.services.StorageService
import edu.wsu.harvesttrace.ui.viewmodels.CameraViewModel

@Composable
fun GalleryImagePicker(modifier: Modifier = Modifier) {

    val navController = LocalSharedState.current.navController
    val context = LocalContext.current
    val cameraViewModel = hiltViewModel<CameraViewModel>(context as ComponentActivity)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            cameraViewModel.setCapturedImageURI(uri)
            val bitmap = StorageService.INSTANCE.readImageFromStorage(context, uri)
            cameraViewModel.setCapturedImage(bitmap)
            navController?.navigate(HarvestTraceRoutes.CAPTURE_PREVIEW_PAGE)
        }
    }

    Icon(
        painter = painterResource(id = R.drawable.gallery),
        contentDescription = "Select image from gallery",
        modifier = modifier
            .clickable(onClick = {
                galleryLauncher.launch("image/*")
            })
    )
}