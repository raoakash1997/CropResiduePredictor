package edu.wsu.harvesttrace.models

import androidx.navigation.NavController

data class SharedState(
    var navController: NavController? = null,
)
