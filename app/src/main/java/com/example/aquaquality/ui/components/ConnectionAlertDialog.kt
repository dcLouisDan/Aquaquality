package com.example.aquaquality.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ConnectionAlertDialog(
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Connection Error",
                style = MaterialTheme.typography.titleLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = "Retry")
            }
        },
        text = {
            Text(text = "You are currently not connected to the internet.")
        },
        modifier = modifier
    )
}