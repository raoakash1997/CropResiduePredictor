package edu.wsu.harvesttrace.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.wsu.harvesttrace.HarvestTraceRoutes
import edu.wsu.harvesttrace.LocalSharedState
import edu.wsu.harvesttrace.R

@Composable
fun AppBar(){
    var navController = LocalSharedState.current.navController
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        IconButton(onClick = { navController!!.navigateUp() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_button_description)
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            Icons.Outlined.Settings,
            contentDescription = stringResource(R.string.go_to_preferences),
            modifier = Modifier
                .clickable(
                    onClick = { navController!!.navigate(HarvestTraceRoutes.PREFERENCES_PAGE) }
                )
        )
    }
}