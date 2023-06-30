package com.example.aquaquality.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aquaquality.R
import com.example.aquaquality.ui.theme.AquaqualityTheme


@Composable
fun AquaQualityHomeScreen() {
    Scaffold(
        topBar = { AquaQualityAppBar(canNavigateBack = false, navigateUp = { /*TODO*/ }) }
    ) { innerPadding ->
        val isEmpty = false
        val backgroundColor = MaterialTheme.colorScheme.surfaceVariant

        val navigationItemContentList = listOf(
            NavigationItemContent(
                icon = Icons.Default.Home,
                text = "Home"
            ),
            NavigationItemContent(
                icon = Icons.Filled.Analytics,
                text = "Name"
            ),
            NavigationItemContent(
                icon = Icons.Default.Settings,
                text = "Name"
            ),
            NavigationItemContent(
                icon = Icons.Default.Person,
                text = "Account"
            ),
        )

        Column(modifier = Modifier.padding(innerPadding)) {
            Box(modifier = Modifier.weight(1f)) {
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
                        items(3) {
                            FishpondCard(
                                modifier = Modifier.padding(
                                    vertical = dimensionResource(id = R.dimen.padding_small),
                                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                                )
                            )
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { /* Do something */ },
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

            AquaQualityBottomNavigationBar(navigationItemContentList = navigationItemContentList)
        }
    }
}

@Composable
fun FishpondCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        val isConnected = false
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
                    text = "Name",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    onClick = { /*TODO*/ }, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    )
                ) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }
            }

            if (isConnected) {
                Column(modifier = Modifier.fillMaxWidth()) {

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
                modifier = Modifier.fillMaxWidth()
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
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier, containerColor = MaterialTheme.colorScheme.background) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = false,
                onClick = { },
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

@Preview
@Composable
fun CardPreview() {
    AquaqualityTheme {
        FishpondCard()
    }
}

