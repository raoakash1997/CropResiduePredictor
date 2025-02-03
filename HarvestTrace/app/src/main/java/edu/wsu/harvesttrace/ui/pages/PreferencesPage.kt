package edu.wsu.harvesttrace.ui.pages

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import edu.wsu.harvesttrace.LocalSharedState
import edu.wsu.harvesttrace.R
import edu.wsu.harvesttrace.ui.components.preferences.ClickablePreferenceComponent
import edu.wsu.harvesttrace.ui.components.preferences.PreferenceGroup
import edu.wsu.harvesttrace.ui.components.preferences.PreferenceSegmentedButton
import edu.wsu.harvesttrace.ui.viewmodels.PreferencesViewModel

private const val IMPERIAL = "Imperial"
private const val METRIC = "Metric"

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreferencesPage() {

    val preferencesViewModel = hiltViewModel<PreferencesViewModel>(LocalContext.current as ComponentActivity)
    val isMetric by preferencesViewModel.isMetric.collectAsState(false)
    val availableUnitOptions = setOf(IMPERIAL, METRIC)
    val handleUnitSelection = { selection: String ->
        if (selection == IMPERIAL) {
            preferencesViewModel.setIsMetric(false)
        } else {
            preferencesViewModel.setIsMetric(true)
        }
    }

    val navController = LocalSharedState.current.navController
    var showAboutUs by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController!!.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                },
            )
        },

        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PreferenceGroup(name = R.string.general_preference_group) {
                PreferenceSegmentedButton(
                    icon = R.drawable.ruler,
                    iconDesc = R.string.units_icon_description,
                    name = R.string.units_text,
                    onClick = handleUnitSelection,
                    selectedOption = if (isMetric) METRIC else IMPERIAL,
                    options = availableUnitOptions
                )
            }

            PreferenceGroup(name = R.string.info_preference_group) {
                ClickablePreferenceComponent(
                    icon = R.drawable.info,
                    iconDesc = R.string.units_icon_description,
                    name = R.string.about_us,
                    onClick = { showAboutUs = false },
                )
            }

        }
    }
}
