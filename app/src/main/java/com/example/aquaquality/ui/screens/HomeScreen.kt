package com.example.aquaquality.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.example.aquaquality.presentation.sign_in.UserData
import com.example.aquaquality.ui.theme.AquaqualityTheme
import com.example.aquaquality.ui.viewmodels.FishpondListViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AquaQualityHomeScreen(
    userData: UserData?,
    onLogoutClick: () -> Unit,
    exitApp: () -> Unit
) {
    val fishpondListViewModel: FishpondListViewModel = viewModel()
    val fishpondListUiState = fishpondListViewModel.uiState.collectAsState().value


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
                    2 -> SettingsScreen()

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
            exitApp = {},)
    }
}


