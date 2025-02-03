package edu.wsu.harvesttrace

import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

            val snackbarHostState = remember { SnackbarHostState() }
            var hasPermission by remember { mutableStateOf(false) }
            var permissionDenied by remember { mutableStateOf(false) }
            val requiredPermissions = getRequiredPermissions()
            val requestPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { result: Map<String, Boolean> ->
                hasPermission = result.all { it.value }
                permissionDenied = !hasPermission
            }

            LaunchedEffect(Unit) { requestPermissionLauncher.launch(requiredPermissions) }

            enableEdgeToEdge()
            if (hasPermission) {
                initializeApp()
                val navController = rememberNavController()
                HarvestTraceNavGraph(navController)
            } else if (permissionDenied) {
                // TODO: Handle denied permissions
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        "Please provide the required permissions!",
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }

    private fun getRequiredPermissions(): Array<String> {
        val permissions = mutableSetOf<String>()
        permissions.add(android.Manifest.permission.CAMERA)
        return permissions.toTypedArray()
    }


    private fun initializeApp() {
        createStorageDirectory()
    }

    private fun createStorageDirectory() {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/HarvestTrace")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        val resolver = this.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            values.clear()
            values.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
        }
    }


}
