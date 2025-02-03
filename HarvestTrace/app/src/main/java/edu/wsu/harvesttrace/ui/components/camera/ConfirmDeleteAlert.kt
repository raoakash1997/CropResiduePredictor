package edu.wsu.harvesttrace.ui.components.camera

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon

@Composable
fun ConfirmDeleteAlertDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    val maxHeight = LocalConfiguration.current.screenHeightDp.dp / 2
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Are you sure?") },
        icon = { Icon(Icons.Filled.Warning, contentDescription = "Confirm Delete Warning Icon") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        modifier = Modifier.heightIn(max = maxHeight)
    )
}

