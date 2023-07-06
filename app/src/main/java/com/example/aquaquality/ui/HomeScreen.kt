@file:OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationApi::class)

package com.example.aquaquality.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aquaquality.R
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.ui.theme.AquaqualityTheme
import com.example.aquaquality.ui.components.ParameterMonitor
import com.example.aquaquality.ui.components.IndicatorStatus


@Composable
fun AquaQualityHomeScreen() {
    Scaffold(
        topBar = { AquaQualityAppBar(canNavigateBack = false, navigateUp = { /*TODO*/ }) }
    ) { innerPadding ->
        val isEmpty = false
        val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        var isNewDialogVisible by rememberSaveable {
            mutableStateOf(false)
        }

        val navigationItemContentList = listOf(
            NavigationItemContent(
                index = 0,
                icon = Icons.Default.Home,
                text = "Home"
            ),
            NavigationItemContent(
                index = 1,
                icon = Icons.Filled.Book,
                text = "Name"
            ),
            NavigationItemContent(
                index = 2,
                icon = Icons.Default.Settings,
                text = "Name"
            ),
            NavigationItemContent(
                index = 3,
                icon = Icons.Default.Person,
                text = "Account"
            ),
        )

        val fishpondList: List<FishpondInfo> = listOf(
            FishpondInfo(
                id = 1,
                name = "Fishpond 1",
                tempValue = 30F,
                phValue = 6.7F,
                turbidityValue = 160
            ),
            FishpondInfo(
                id = 2,
                name = "Fishpond 2",
                tempValue = 27.5F,
                phValue = 7.0F,
                turbidityValue = 154
            )
        )

        var currentScreenIndex by rememberSaveable { mutableStateOf(0) }

        Column(modifier = Modifier.padding(innerPadding)) {
//            val screensNumber = 4
//            var offsetX by remember { mutableStateOf(0f) }

            val swipeableModifier =
                Modifier
                    .weight(1f)

            AnimatedContent(
                targetState = currentScreenIndex,
                modifier = swipeableModifier,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) with slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth })
                }
            ) { targetState ->
                when (targetState) {
                    0 -> FishpondCardList(
                        isEmpty,
                        backgroundColor,
                        fishpondList,
                        onFabClick = { isNewDialogVisible = true },
                    )

                    1 -> ReferencesScreen()
                    2 -> SettingsScreen(
                        minTemp = "28",
                        maxTemp = "32",
                        minPh = "6.5",
                        maxPh = "8.5",
                        minTurb = "0",
                        maxTurb = "180",
                        onMinTempChange = {},
                        onMaxTempChange = {},
                        onMinPhChange = {},
                        onMaxPhChange = {},
                        onMinTurbChange = {},
                        onMaxTurbChange = {},
                        onSaveButtonClick = {},
                    )

                    3 -> AccountScreen()
                }
            }

            AquaQualityBottomNavigationBar(
                navigationItemContentList = navigationItemContentList,
                onTabClick = { currentScreenIndex = it })

            if (isNewDialogVisible) {
                AlertDialog(
                    onDismissRequest = { isNewDialogVisible = false },
                    title = {
                        Text(
                            text = "Add new fishpond",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = "Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isNewDialogVisible = false }) {
                            Text(text = "Cancel")
                        }
                    },
                    text = {
                        TextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text(text = "Enter fishpond name") })
                    }
                )
            }
        }
    }
}

@Composable
private fun FishpondCardList(
    isEmpty: Boolean,
    backgroundColor: Color,
    fishpondList: List<FishpondInfo>,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (isEmpty) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
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
                    .background(backgroundColor)
            ) {
                items(fishpondList) { fishpond ->
                    FishpondCard(
                        fishpond,
                        modifier = Modifier.padding(
                            dimensionResource(id = R.dimen.padding_small)
                        ),
                        isConnected = true,
                        onCardClick = {}
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
    onCardClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick },
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
                Text(
                    text = fishpondInfo.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
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
                        DropdownMenuItem(text = { Text(text = "Edit") }, onClick = { /*TODO*/ })
                        DropdownMenuItem(text = { Text(text = "Delete") }, onClick = { /*TODO*/ })
                    }
                }

            }

            if (isConnected) {
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


@Composable
fun AquaQualityAppBar(
//    currentScreen: CupcakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
private fun AquaQualityBottomNavigationBar(
    navigationItemContentList: List<NavigationItemContent>,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier, containerColor = MaterialTheme.colorScheme.background) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = false,
                onClick = { onTabClick(navItem.index) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

private data class NavigationItemContent(
    val index: Int,
    val icon: ImageVector,
    val text: String
)

@Preview
@Composable
fun HomePreview() {
    AquaqualityTheme {
        AquaQualityHomeScreen()
    }
}


