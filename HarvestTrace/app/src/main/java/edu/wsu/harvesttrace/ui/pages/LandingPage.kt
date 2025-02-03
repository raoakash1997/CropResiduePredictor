package edu.wsu.harvesttrace.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.wsu.harvesttrace.HarvestTraceRoutes
import edu.wsu.harvesttrace.LocalSharedState
import edu.wsu.harvesttrace.R
import edu.wsu.weather.fst.ui.components.Footer

@Composable
fun LandingPage() {

    val navController = LocalSharedState.current.navController

    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 50.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.wheat),
                contentDescription = "WSU Logo",
                Modifier
                    .size(150.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { navController?.navigate(HarvestTraceRoutes.CAMERA_STREAM_PAGE) }) {
                Row {
                    Text("Begin")
                    Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = "arrow icon")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Footer()
            Spacer(modifier = Modifier.weight(1f))
        }
    }

}

