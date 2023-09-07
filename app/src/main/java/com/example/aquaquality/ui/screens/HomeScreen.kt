package com.example.aquaquality.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aquaquality.R
import com.example.aquaquality.data.local.LocalInfoProvider
import com.example.aquaquality.presentation.sign_in.UserData
import com.example.aquaquality.ui.components.ParameterWarningDialog
import com.example.aquaquality.ui.theme.AquaqualityTheme
import com.example.aquaquality.ui.viewmodels.FishpondListViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AquaQualityHomeScreen(
    userData: UserData?,
    onLogoutClick: () -> Unit,
    afterSaveAction: (() -> Unit)? = null,
    exitApp: () -> Unit
) {
    val fishpondListViewModel: FishpondListViewModel = viewModel()
    val fishpondListUiState = fishpondListViewModel.uiState.collectAsState().value

    var isLowTempSuggestionScreenVisible by rememberSaveable { mutableStateOf(false) }
    var isHighTempSuggestionScreenVisible by rememberSaveable { mutableStateOf(false) }
    var isLowPhSuggestionScreenVisible by rememberSaveable { mutableStateOf(false) }
    var isHighPhSuggestionScreenVisible by rememberSaveable { mutableStateOf(false) }
    var isLowTurbSuggestionScreenVisible by rememberSaveable { mutableStateOf(false) }
    var isHighTurbSuggestionScreenVisible by rememberSaveable { mutableStateOf(false) }

    if (fishpondListUiState.isLowTempAlertVisible) {
        ParameterWarningDialog(suggestionInfo = LocalInfoProvider.lowTempSuggestionInfo,
            onConfirmClick = {
                fishpondListViewModel.toggleLowTempAlert(false)
                isLowTempSuggestionScreenVisible = true
            },
            onDismissRequest = {
                fishpondListViewModel.toggleLowTempAlert(false)
            })
    }
    if (fishpondListUiState.isHighTempAlertVisible) {
        ParameterWarningDialog(suggestionInfo = LocalInfoProvider.highTempSuggestionInfo,
            onConfirmClick = {
                fishpondListViewModel.toggleHighTempAlert(false)
                isHighTempSuggestionScreenVisible = true
            },
            onDismissRequest = {
                fishpondListViewModel.toggleHighTempAlert(false)
            })
    }

    if (fishpondListUiState.isLowPhAlertVisible) {
        ParameterWarningDialog(suggestionInfo = LocalInfoProvider.lowPhSuggestionInfo,
            onConfirmClick = {
                fishpondListViewModel.toggleLowPhAlert(false)
                isLowPhSuggestionScreenVisible = true
            },
            onDismissRequest = {
                fishpondListViewModel.toggleLowPhAlert(false)
            })
    }
    if (fishpondListUiState.isHighPhAlertVisible) {
        ParameterWarningDialog(suggestionInfo = LocalInfoProvider.highPhSuggestionInfo,
            onConfirmClick = {
                fishpondListViewModel.toggleHighPhAlert(false)
                isHighPhSuggestionScreenVisible = true
            },
            onDismissRequest = {
                fishpondListViewModel.toggleHighPhAlert(false)
            })
    }

    if (fishpondListUiState.isLowTurbAlertVisible) {
        ParameterWarningDialog(suggestionInfo = LocalInfoProvider.lowTurbSuggestionInfo,
            onConfirmClick = {
                fishpondListViewModel.toggleLowTurbAlert(false)
                isLowTurbSuggestionScreenVisible = true
            },
            onDismissRequest = {
                fishpondListViewModel.toggleLowTurbAlert(false)
            })
    }
    if (fishpondListUiState.isHighTurbAlertVisible) {
        ParameterWarningDialog(suggestionInfo = LocalInfoProvider.highTurbSuggestionInfo,
            onConfirmClick = {
                fishpondListViewModel.toggleHighTurbAlert(false)
                isHighTurbSuggestionScreenVisible = true
            },
            onDismissRequest = {
                fishpondListViewModel.toggleHighTurbAlert(false)
            })
    }

    Scaffold(
        topBar = {
            AquaQualityAppBar(
                canNavigateBack = !fishpondListUiState.isShowingHomepage,
                navigateUp = { fishpondListViewModel.resetHomeScreenStates() },
                onRefreshClick = { fishpondListViewModel.refreshData() }
            )
        }
    ) { innerPadding ->

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


        var currentScreenIndex by rememberSaveable { mutableStateOf(0) }
        var previousScreenIndex by rememberSaveable { mutableStateOf(0) }

        Column(modifier = Modifier.padding(innerPadding)) {

            val transitionToRight =
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) with slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth })
            val transitionToLeft =
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) with slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth })

            val swipeableModifier =
                Modifier
                    .weight(1f)

            AnimatedContent(
                targetState = currentScreenIndex,
                modifier = swipeableModifier,
                transitionSpec = {
                    if (previousScreenIndex < currentScreenIndex) {
                        transitionToRight
                    } else {
                        transitionToLeft
                    }
                }
            ) { targetState ->
                when (targetState) {
                    0 -> FishpondListScreen(
                        fishpondListViewModel = fishpondListViewModel,
                        uiState = fishpondListUiState,
                        exitApp = exitApp
                    )

                    1 -> ReferencesScreen()
                    2 -> SettingsScreen(afterSaveAction = afterSaveAction)

                    3 -> AccountScreen(userData = userData, onLogoutClick = onLogoutClick)
                }
            }

            AquaQualityBottomNavigationBar(
                navigationItemContentList = navigationItemContentList,
                onTabClick = {
                    previousScreenIndex = currentScreenIndex
                    currentScreenIndex = it
                })
        }
    }

    AnimatedVisibility(
        visible = isLowTempSuggestionScreenVisible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        SuggestionScreen(
            suggestionInfo = LocalInfoProvider.lowTempSuggestionInfo,
            onCloseButtonClick = {
                isLowTempSuggestionScreenVisible = false
            })
    }
    AnimatedVisibility(
        visible = isHighTempSuggestionScreenVisible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        SuggestionScreen(
            suggestionInfo = LocalInfoProvider.highTempSuggestionInfo,
            onCloseButtonClick = {
                isHighTempSuggestionScreenVisible = false
            })
    }

    AnimatedVisibility(
        visible = isLowPhSuggestionScreenVisible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        SuggestionScreen(
            suggestionInfo = LocalInfoProvider.lowPhSuggestionInfo,
            onCloseButtonClick = {
                isLowPhSuggestionScreenVisible = false
            })
    }
    AnimatedVisibility(
        visible = isHighPhSuggestionScreenVisible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        SuggestionScreen(
            suggestionInfo = LocalInfoProvider.highPhSuggestionInfo,
            onCloseButtonClick = {
                isHighPhSuggestionScreenVisible = false
            })
    }

    AnimatedVisibility(
        visible = isLowTurbSuggestionScreenVisible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        SuggestionScreen(
            suggestionInfo = LocalInfoProvider.lowTurbSuggestionInfo,
            onCloseButtonClick = {
                isLowTurbSuggestionScreenVisible = false
            })
    }
    AnimatedVisibility(
        visible = isHighTurbSuggestionScreenVisible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        SuggestionScreen(
            suggestionInfo = LocalInfoProvider.highTurbSuggestionInfo,
            onCloseButtonClick = {
                isHighTurbSuggestionScreenVisible = false
            })
    }
}


@Composable
fun AquaQualityAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appBarColor =
        if (canNavigateBack) {
            MaterialTheme.colorScheme.inversePrimary
        } else {
            MaterialTheme.colorScheme.surface
        }

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
            containerColor = appBarColor
        ),
        actions = {
            IconButton(onClick = onRefreshClick) {
                Icon(Icons.Default.Refresh, contentDescription = null)
            }
        },
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomePreview() {
    AquaqualityTheme {
        AquaQualityHomeScreen(
            userData = UserData("", "", "", null),
            onLogoutClick = {},
            exitApp = {},
        )
    }
}


