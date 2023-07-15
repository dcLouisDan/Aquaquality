@file:OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationApi::class,
    ExperimentalAnimationApi::class, ExperimentalAnimationApi::class,
    ExperimentalAnimationApi::class, ExperimentalAnimationApi::class
)

package com.example.aquaquality.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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


@Composable
fun AquaQualityHomeScreen(userData: UserData?,onLogoutClick: () -> Unit) {
    val fishpondListViewModel: FishpondListViewModel = viewModel()
    val fishpondListUiState = fishpondListViewModel.uiState.collectAsState().value


    Scaffold(
        topBar = {
            AquaQualityAppBar(
                canNavigateBack = !fishpondListUiState.isShowingHomepage,
                navigateUp = { fishpondListViewModel.resetHomeScreenStates() },
            )
        }
    ) { innerPadding ->
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
                    0 -> FishpondListScreen(
                        fishpondListViewModel = fishpondListViewModel,
                        uiState =fishpondListUiState,
                        onFabClick = {isNewDialogVisible = true}
                    )
                    1 -> ReferencesScreen()
                    2 -> SettingsScreen(
                        onSaveButtonClick = {},
                    )

                    3 -> AccountScreen(userData = userData, onLogoutClick = onLogoutClick)
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
fun AquaQualityAppBar(
//    currentScreen: CupcakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
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
        AquaQualityHomeScreen(userData = UserData("", "", "", null),onLogoutClick = {})
    }
}


