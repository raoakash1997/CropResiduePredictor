package edu.wsu.harvesttrace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.wsu.harvesttrace.models.SharedState
import edu.wsu.harvesttrace.ui.pages.CameraStreamPage
import edu.wsu.harvesttrace.ui.pages.CapturePreviewPage
import edu.wsu.harvesttrace.ui.pages.LandingPage
import edu.wsu.harvesttrace.ui.pages.PreferencesPage
import edu.wsu.harvesttrace.ui.pages.ResultsPage
import edu.wsu.harvesttrace.ui.theme.AppTheme


val LocalSharedState = compositionLocalOf {
    SharedState()
}

@Composable
fun HarvestTraceNavGraph(navController: NavHostController) {

    val sharedState = LocalSharedState.current
    sharedState.navController = navController

    CompositionLocalProvider(LocalSharedState provides sharedState) {
        AppTheme {
            Surface {
                Column {
                    Spacer(
                        modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars)
                    )
                    NavHost(
                        navController = navController,
                        startDestination = HarvestTraceRoutes.LANDING_PAGE
                    ) {
                        composable(route = HarvestTraceRoutes.LANDING_PAGE) {
                            LandingPage()
                        }

                        composable(route = HarvestTraceRoutes.CAMERA_STREAM_PAGE) {
                            CameraStreamPage()
                        }

                        composable(
                            route = HarvestTraceRoutes.CAPTURE_PREVIEW_PAGE,
                        ) {
                            CapturePreviewPage()
                        }
                        composable(
                            route = HarvestTraceRoutes.RESULTS_PAGE,
                        ) {
                            ResultsPage()
                        }
                        composable(
                            route = HarvestTraceRoutes.PREFERENCES_PAGE,
                        ) {
                            PreferencesPage()
                        }
                    }
                    Spacer(
                        modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars)
                    )
                }
            }
        }
    }
}


object HarvestTraceRoutes {
    const val LANDING_PAGE = "landing-page"
    const val CAMERA_STREAM_PAGE = "camera-view-page"
    const val CAPTURE_PREVIEW_PAGE = "capture-preview-page"
    const val RESULTS_PAGE = "results-page"
    const val PREFERENCES_PAGE = "preferences-page"
}
