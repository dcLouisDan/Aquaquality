package com.example.aquaquality.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aquaquality.R
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondListUiState
import com.example.aquaquality.data.FishpondScreenUiState
import com.example.aquaquality.ui.components.*
import com.example.aquaquality.ui.theme.AquaqualityTheme
import com.example.aquaquality.ui.viewmodels.FishpondListViewModel
import com.example.aquaquality.ui.viewmodels.FishpondScreenViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FishpondListScreen(
    fishpondListViewModel: FishpondListViewModel = viewModel(),
    uiState: FishpondListUiState,
    exitApp: () -> Unit,
    fishpondScreenViewModel: FishpondScreenViewModel,
    fishpondScreenUiState: FishpondScreenUiState
) {
    val context = LocalContext.current

    var isNewDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isEditDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isDeleteDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    //FishpondScreen ViewModel

    BackHandler {
        if (!uiState.isShowingHomepage) {
            fishpondListViewModel.resetHomeScreenStates()
        } else {
            exitApp()
        }
    }

    LaunchedEffect(key1 = uiState.isAdditionSuccess) {
        if (uiState.isAdditionSuccess) {
            Toast.makeText(
                context,
                "New Fishpond Added",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(key1 = fishpondScreenUiState.isConnectionSuccess) {
        if (fishpondScreenUiState.isConnectionSuccess) {
            Toast.makeText(
                context,
                "Device successfully connected",
                Toast.LENGTH_LONG
            ).show()
            fishpondScreenViewModel.resetConnectionMessages()
        }
    }

    LaunchedEffect(key1 = fishpondScreenUiState.isDisconnectionSuccess) {
        if (fishpondScreenUiState.isDisconnectionSuccess) {
            Toast.makeText(
                context,
                "Device successfully disconnected",
                Toast.LENGTH_LONG
            ).show()
            fishpondScreenViewModel.resetConnectionMessages()
        }
    }


    if (isNewDialogVisible) {
        NewFishpondCardDialog(
            value = uiState.newFishpondName,
            onValueChange = { fishpondListViewModel.setNewFishpondNameInput(it) },
            onConfirmClick = {
                fishpondListViewModel.addNewFishpond(uiState.newFishpondName, uiState.newFishpondDesc)
                isNewDialogVisible = false
            },
            valueDesc = uiState.newFishpondDesc,
            onValueDescChange = {
                fishpondListViewModel.setNewFishpondDescInput(it)
            },
            onDismissRequest = { isNewDialogVisible = false }
        )
    }

    if (isEditDialogVisible) {
        EditFishpondCardDialog(
            value = uiState.editFishpondName,
            onValueChange = { fishpondListViewModel.setEditFishpondNameInput(it) },
            onConfirmClick = {
                fishpondListViewModel.updateFishpondName(
                    uiState.editFishpondName,
                    uiState.fishpondInfoToModify!!,
                    uiState.editFishpondDesc
                )
                isEditDialogVisible = false
            },
            onDismissRequest = {
                isEditDialogVisible = false
            },
            valueDesc = uiState.editFishpondDesc,
            onValueDescChange = { fishpondListViewModel.setEditFishpondDescInput(it)}
        )
    }

    if (isDeleteDialogVisible) {
        DeleteFishpondCardDialog(
            name = uiState.fishpondInfoToModify?.name,
            onConfirmClick = {
                fishpondListViewModel.deleteFishpondInfo(uiState.fishpondInfoToModify!!)
                isDeleteDialogVisible = false
            },
            onDismissRequest = { isDeleteDialogVisible = false }
        )
    }
    if (!uiState.isShowingHomepage) {
        FishpondScreen(
            fishpondScreenViewModel = fishpondScreenViewModel,
            uiState = fishpondScreenUiState
        )
    } else {
        FishpondCardList(
            isEmpty = uiState.fishpondMap.isEmpty(),
            fishpondList = uiState.fishpondMap.values.toList(),
            isLoading = uiState.isLoading,
            onFabClick = { isNewDialogVisible = true },
            onCardClick = { fishpondInfo: FishpondInfo ->
                fishpondListViewModel.updateDetailsScreenStates(fishpondInfo)
                fishpondScreenViewModel.setFishpondInfo(
                    fishpondInfo = fishpondInfo,
                    key = fishpondInfo.id!!
                )
            },
            onDeleteClick = {
                fishpondListViewModel.setFishpondInfoToModify(it)
                isDeleteDialogVisible = true
            },
            onEditClick = {
                fishpondListViewModel.setFishpondInfoToModify(it)
                fishpondListViewModel.setEditFishpondNameInput(it.name)
                isEditDialogVisible = true
            }
        )
    }
}


@Composable
private fun FishpondCardList(
    isEmpty: Boolean,
    fishpondList: List<FishpondInfo>,
    onFabClick: () -> Unit,
    onCardClick: (FishpondInfo) -> Unit,
    modifier: Modifier = Modifier,
    onEditClick: (FishpondInfo) -> Unit,
    onDeleteClick: (FishpondInfo) -> Unit,
    isLoading: Boolean
) {
    Box(modifier = modifier) {
        if (isLoading) {
            FishpondLoadingScreen(modifier.background(MaterialTheme.colorScheme.surfaceVariant))
        } else if (isEmpty) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.home_press_plus_message),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .width(250.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                items(fishpondList) { fishpond ->
                    FishpondCard(
                        fishpond,
                        modifier = Modifier.padding(
                            dimensionResource(id = R.dimen.padding_small)
                        ),
                        onCardClick = onCardClick,
                        onEditClick = { onEditClick(fishpond) },
                        onDeleteClick = { onDeleteClick(fishpond) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onFabClick,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(
                        id = R.dimen.padding_xl
                    )
                )
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun FishpondCard(
    fishpondInfo: FishpondInfo,
    modifier: Modifier = Modifier,
    onCardClick: (FishpondInfo) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick(fishpondInfo) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        var isCardMenuVisible by rememberSaveable {
            mutableStateOf(false)
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary), verticalAlignment = Alignment.CenterVertically
            ) {
                fishpondInfo.name?.let {
                    Text(
                        text = it,
                        modifier = Modifier.weight(1f).padding(
                            start = dimensionResource(id = R.dimen.padding_medium),
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                IconButton(
                    onClick = {
                        isCardMenuVisible = !isCardMenuVisible
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    )
                ) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                    DropdownMenu(
                        expanded = isCardMenuVisible,
                        onDismissRequest = { isCardMenuVisible = false },

                        ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.label_edit)) },
                            onClick = onEditClick
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.label_delete)) },
                            onClick = onDeleteClick
                        )
                    }
                }

            }

            if (fishpondInfo.connectedDeviceId != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.padding_small))
                ) {
                    val isOffline = fishpondInfo.isOffline!!
                    ParameterMonitorLarge(
                        Icons.Default.Thermostat,
                        R.string.label_temperature,
                        if (isOffline) "--" else fishpondInfo.tempValue.toString(),
                        parameterValueFormat = R.string.parameter_temperature,
                        stringIndicatorStatus = if (isOffline) IndicatorStatus.OFFLINE.name else fishpondInfo.tempStatus!!
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                    ParameterMonitorLarge(
                        Icons.Default.Science,
                        R.string.label_pH,
                        if (isOffline) "--" else fishpondInfo.phValue.toString(),
                        parameterValueFormat = R.string.parameter_pH,
                        stringIndicatorStatus = if (isOffline) IndicatorStatus.OFFLINE.name else fishpondInfo.phStatus!!
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                    ParameterMonitorLarge(
                        Icons.Default.Water,
                        R.string.label_turbidity,
                        if (isOffline) "--" else fishpondInfo.turbidityValue.toString(),
                        parameterValueFormat = R.string.parameter_turbidity,
                        stringIndicatorStatus = if (isOffline) IndicatorStatus.OFFLINE.name else fishpondInfo.turbStatus!!
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = dimensionResource(id = R.dimen.padding_xl),
                            horizontal = dimensionResource(id = R.dimen.padding_large)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.message_connect_to_view),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Composable
fun FishpondLoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun FishpondsPreview() {
    AquaqualityTheme {
        FishpondLoadingScreen()
    }
}