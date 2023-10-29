package com.example.aquaquality.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.aquaquality.R
import com.example.aquaquality.data.SuggestionInfo
import com.example.aquaquality.data.SuggestionType
import com.example.aquaquality.data.local.LocalInfoProvider
import com.example.aquaquality.ui.theme.AquaqualityTheme


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



val highTempSuggestionInfo = SuggestionInfo(
    suggestionType = SuggestionType.HIGH,
    headline = R.string.high_temp_headline,
    solutionList = LocalInfoProvider.highTempTreatments,
    consequenceList = LocalInfoProvider.highTempConsequences
)

val lowPhSuggestionInfo = SuggestionInfo(
    suggestionType = SuggestionType.LOW,
    headline = R.string.low_ph_headline,
    solutionList = LocalInfoProvider.highTempTreatments,
    consequenceList = LocalInfoProvider.highTempConsequences
)

@Composable
fun ParameterWarningDialog(
    suggestionInfo: SuggestionInfo,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val contentColor = when(suggestionInfo.suggestionType) {
        SuggestionType.LOW -> MaterialTheme.colorScheme.onTertiary
        SuggestionType.HIGH -> MaterialTheme.colorScheme.onError
    }
    val containerColor = when(suggestionInfo.suggestionType) {
        SuggestionType.LOW -> MaterialTheme.colorScheme.tertiary
        SuggestionType.HIGH -> MaterialTheme.colorScheme.error
    }
    AlertDialog(
        containerColor = containerColor,
        textContentColor = contentColor,
        titleContentColor = contentColor,
        onDismissRequest = onDismissRequest,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Filled.Error, contentDescription = null)
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                Text(
                    text = "Warning!",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = "View Possible Solutions", color = contentColor)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Dismiss", color = contentColor)
            }
        },
        text = {
            Text(text = stringResource(suggestionInfo.headline), textAlign = TextAlign.Center)
        }
    )
}


@Preview
@Composable
fun WarningPreview() {
    AquaqualityTheme {
        ParameterWarningDialog(
            suggestionInfo = highTempSuggestionInfo,
            onConfirmClick = {}) {
        }
    }
}

@Preview
@Composable
fun WarningPreview2() {
    AquaqualityTheme {
        ParameterWarningDialog(
            suggestionInfo = lowPhSuggestionInfo,
            onConfirmClick = {}) {
        }
    }
}