package edu.wsu.harvesttrace.ui.pages

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.wsu.harvesttrace.R
import edu.wsu.harvesttrace.ui.components.AppBar
import edu.wsu.harvesttrace.ui.components.camera.ImagePreview
import edu.wsu.harvesttrace.ui.viewmodels.CameraViewModel
import edu.wsu.harvesttrace.ui.viewmodels.PreferencesViewModel

@Preview
@Composable
fun ResultsPage() {

    val preferencesViewModel = hiltViewModel<PreferencesViewModel>(LocalContext.current as ComponentActivity)
    val segmentationService = preferencesViewModel.segmentationService
    var percentage by remember { mutableDoubleStateOf(0.0) }

    val cameraViewModel = hiltViewModel<CameraViewModel>(LocalContext.current as ComponentActivity)
    val capturedImage by cameraViewModel.capturedImage.collectAsState()
    var segmentedMask by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {

        if (capturedImage != null) {
            segmentedMask = segmentationService.runInference(bitmap = capturedImage!!)
            percentage = segmentationService.getPercentageValue()
        }
    }

    fun categorize(fst: Double): String {
        return when {
            fst < 20.0 -> "High"
            fst < 50.0 -> "Moderate"
            fst < 75.0 -> "Low"
            else -> "Minimal"
        }
    }


    fun categorizeColor(fst: Double): Color {
        return when {
            fst < 20.0 -> Color.Red
            fst < 50.0 -> Color.Yellow
            fst < 75.0 -> Color.Green
            else -> Color.Green
        }
    }

    fun round(value: Double, decimalPlaces: Int = 0): String {
        return "%.${decimalPlaces}f".format(value)
    }

    Scaffold(
        topBar = { AppBar() },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                ImagePreview(segmentedMask)
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(2f))
                    Card(
                        modifier = Modifier.width(150.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.plant),
                                    "",
                                    modifier = Modifier.size(25.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(text = "Residue", style = MaterialTheme.typography.bodyMedium)
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = round(percentage, decimalPlaces = 2).toString(),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1.5f))
                    Card(
                        modifier = Modifier.width(150.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Circle(color = categorizeColor(percentage), circleRadius = 25f)
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(text = "Erosion Risk", style = MaterialTheme.typography.bodyMedium)
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = categorize(percentage),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(2f))
                }
                Spacer(modifier = Modifier.weight(1f))
            }
    }
}

@Composable
fun Circle(
    color: Color,
    modifier: Modifier = Modifier,
    circleRadius: Float = 50f
) {
    Canvas(
        modifier = modifier
            .size((circleRadius * 1).dp)
    ) {
        drawCircle(
            color = color,
            radius = circleRadius
        )
    }
}