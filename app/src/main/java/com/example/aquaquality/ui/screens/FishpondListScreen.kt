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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.aquaquality.presentation.database.RealtimeDbClient
import com.example.aquaquality.ui.components.IndicatorStatus
import com.example.aquaquality.ui.components.ParameterMonitor
import com.example.aquaquality.ui.theme.AquaqualityTheme
import com.example.aquaquality.ui.viewmodels.FishpondListViewModel
import com.example.aquaquality.ui.components.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FishpondListScreen(
    fishpondListViewModel: FishpondListViewModel = viewModel(),
    uiState: FishpondListUiState
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
    var isAddSuccess by rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler(!uiState.isShowingHomepage) {
        fishpondListViewModel.resetHomeScreenStates()
    }

    LaunchedEffect(key1 = isAddSuccess) {
        if (isAddSuccess) {
            Toast.makeText(
                context,
                "New Fishpond Added",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    if (isNewDialogVisible) {
        NewFishpondCardDialog(
            value = uiState.newFishpondName,
            onValueChange = { fishpondListViewModel.setNewFishpondNameInput(it) },
            onConfirmClick = {
                fishpondListViewModel.addNewFishpond(uiState.newFishpondName)
                isNewDialogVisible = false
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
                    uiState.fishpondInfoToModify!!
                )
                isEditDialogVisible = false
            },
            onDismissRequest = {
                isEditDialogVisible = false
            }
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
        FishpondScreen(uiState.currentSelectedFishpondInfo)
    } else {
        FishpondCardList(
            isEmpty = uiState.fishpondList.isEmpty(),
            fishpondList = uiState.fishpondList,
            onFabClick = { isNewDialogVisible = true },
            onCardClick = { fishpondInfo: FishpondInfo ->
                fishpondListViewModel.updateDetailsScreenStates(fishpondInfo)
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
) {
    Box(modifier = modifier) {
        if (isEmpty) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
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
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                items(fishpondList) { fishpond ->
                    FishpondCard(
                        fishpond,
                        modifier = Modifier.padding(
                            dimensionResource(id = R.dimen.padding_small)
                        ),
                        isConnected = true,
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
    isConnected: Boolean = false,
    onCardClick: (FishpondInfo) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick(fishpondInfo) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        var isCardMenuVisible by rememberSaveable {
            mutableStateOf(false)
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.padding_medium),
                        top = dimensionResource(id = R.dimen.padding_small)
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                fishpondInfo.name?.let {
                    Text(
                        text = it,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium
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
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
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
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    ParameterMonitor(
                        Icons.Default.Thermostat,
                        R.string.label_temperature,
                        fishpondInfo.tempValue.toString(),
                        parameterValueFormat = R.string.parameter_temperature,
                        indicatorStatus = IndicatorStatus.OVER_RANGE
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.height_xs)))
                    ParameterMonitor(
                        Icons.Default.Science,
                        R.string.label_pH,
                        fishpondInfo.phValue.toString(),
                        parameterValueFormat = R.string.parameter_pH
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.height_xs)))
                    ParameterMonitor(
                        Icons.Default.Water,
                        R.string.label_turbidity,
                        fishpondInfo.turbidityValue.toString(),
                        parameterValueFormat = R.string.parameter_turbidity,
                        indicatorStatus = IndicatorStatus.UNDER_RANGE
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
                        text = "Connect a AquaQuality Monitoring Device to see water quality parameter measurements",
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun FishpondsPreview() {
    val fishpondListViewModel: FishpondListViewModel = viewModel()
    val fishpondListUiState = fishpondListViewModel.uiState.collectAsState().value
    AquaqualityTheme {
        FishpondListScreen(
            fishpondListViewModel = fishpondListViewModel,
            uiState = fishpondListUiState,
        )
    }
}