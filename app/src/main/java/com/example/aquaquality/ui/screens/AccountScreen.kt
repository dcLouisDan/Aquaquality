package com.example.aquaquality.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.aquaquality.R
import com.example.aquaquality.presentation.sign_in.UserData
import com.example.aquaquality.ui.theme.AquaqualityTheme

@Composable
fun AccountScreen(userData: UserData?, onLogoutClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Card(modifier = Modifier.size(175.dp), shape = CircleShape,elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) ) {
                if (userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.user_placeholder),
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.user_placeholder),
                        contentDescription = stringResource(R.string.label_user_profile_photo_placeholder),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            var isLogoutDialogVisible by remember {
                mutableStateOf(false)
            }

            userData?.username?.let {
                if (it != "") {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                dimensionResource(id = R.dimen.padding_medium)
                            ),
                    )
                    Divider()
                }
            }
            userData?.email?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            dimensionResource(id = R.dimen.padding_medium)
                        ),
                )
            }
            Divider()
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { isLogoutDialogVisible = true }) {
                Text(
                    text = "Logout",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            dimensionResource(id = R.dimen.padding_medium)
                        )
                )
            }

            if (isLogoutDialogVisible) {
                AlertDialog(
                    onDismissRequest = { isLogoutDialogVisible = false },
                    title = {
                        Text(text = "Alert")
                    },
                    text = {
                        Text(text = "Are you sure you want to logout?")
                    },
                    confirmButton = {
                        TextButton(onClick = onLogoutClick) { Text(text = "Logout") }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            isLogoutDialogVisible = false
                        }) { Text(text = "Cancel") }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AccountScreenPreview() {
    AquaqualityTheme {
        AccountScreen(
            userData = UserData("1", "Dan Dela Cruz", "dan@email.com", null),
            onLogoutClick = {})
    }
}