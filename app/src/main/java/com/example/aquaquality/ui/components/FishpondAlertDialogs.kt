package com.example.aquaquality.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable


@Composable
fun NewFishpondCardDialog(
    value: String,
    onValueChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Add new fishpond",
                style = MaterialTheme.typography.titleLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        },
        text = {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(text = "Enter fishpond name") })
        }
    )
}

@Composable
fun EditFishpondCardDialog(
    value: String,
    onValueChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Edit Fishpond Name",
                style = MaterialTheme.typography.titleLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        },
        text = {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(text = "Enter fishpond name") })
        }
    )
}


@Composable
fun DeleteFishpondCardDialog(
    name: String?,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onError
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.error,
        textContentColor = contentColor,
        titleContentColor = contentColor,
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Delete Fishpond",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = "Delete", color = contentColor)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel", color = contentColor)
            }
        },
        text = {
            Text(text = "Are you sure you want to delete \"$name\" and all its related information? This action cannot be undone.")
        }
    )
}
@Composable
fun DisconnectDeviceDialog(
    name: String?,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onError
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.error,
        textContentColor = contentColor,
        titleContentColor = contentColor,
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Disconnect Device",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = "Confirm", color = contentColor)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel", color = contentColor)
            }
        },
        text = {
            Text(text = "Are you sure you want to disconnect from \"$name\"? Real-time monitoring will be unavailable unless a device is connected.")
        }
    )
}