package edu.wsu.harvesttrace.ui.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

@Composable
fun ErrorWidget(
    message: String,
    actionButton: @Composable () -> Unit = { RelaunchAppActionButton() },
    modifier: Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            Icons.Outlined.Warning,
            contentDescription = "Warning Sign",
            modifier = Modifier
                .size(40.dp)
        )
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = message,
                modifier = Modifier
                    .zIndex(10f)
            )
        }
        Spacer(modifier= Modifier.height(5.dp))
        actionButton()
    }
}

@Composable
fun RelaunchAppActionButton() {
    val context = LocalContext.current
    Button(onClick = {
        relaunchApp(context)
    }) {
        Text("Relaunch")
    }
}

fun relaunchApp(context: Context) {
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(it)
        (context as? Activity)?.finish()
    }
}