package com.example.aquaquality.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aquaquality.R
import com.example.aquaquality.ui.theme.AquaqualityTheme

@Composable
fun StartLoginScreen(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    signupEmail: String,
    signupPassword: String,
    signupRepeatPassword: String,
    onSignupEmailChange: (String) -> Unit,
    onSignupPasswordChange: (String) -> Unit,
    onSignupRepeatPasswordChange: (String) -> Unit,
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.aquaquality_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                MaterialTheme.colorScheme.primary,
                blendMode = BlendMode.Hardlight
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.weight(1f)) {
                val logo = ImageVector.vectorResource(id = R.drawable.aquaqualitylogo)
                Image(imageVector = logo , contentDescription = stringResource(
                    id = R.string.app_name
                ), modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.Center))
            }


            Box(
                modifier = Modifier
                    .weight(2f)
            ) {
                var showSignUp by remember { mutableStateOf(false) }
                Column {
                    AnimatedVisibility(
                        visible = showSignUp,
                        enter = slideInHorizontally { fullWidth -> fullWidth } + fadeIn(),
                        exit = slideOutHorizontally { fullWidth -> fullWidth } + fadeOut()
                    ) {
                        SignupCard(
                            signupEmail,
                            onSignupEmailChange,
                            signupPassword,
                            signupRepeatPassword,
                            onSignupPasswordChange,
                            onSignupRepeatPasswordChange,
                            onCancelClick = { showSignUp = !showSignUp }
                        )
                    }
                }
                Column {
                    AnimatedVisibility(
                        visible = !showSignUp,
                        enter = slideInHorizontally { fullWidth -> -fullWidth } + fadeIn(),
                        exit = slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                    ) {
                        LoginCard(
                            email,
                            onEmailChange,
                            password,
                            onPasswordChange,
                            onCreateAccountClick = { showSignUp = !showSignUp })
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginCard(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onCreateAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
    ) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))) {
            PrimaryOutlinedTextInput(
                value = email,
                labelResourceId = R.string.label_email,
                onValueChange = onEmailChange,
                leadingIcon = Icons.Rounded.Email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            PrimaryOutlinedTextInput(
                value = password,
                labelResourceId = R.string.label_password,
                onValueChange = onPasswordChange,
                isPassword = true,
                leadingIcon = Icons.Rounded.Lock,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.label_login))
            }
            //Create Account Button
            Button(
                onClick = onCreateAccountClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(text = stringResource(R.string.label_create_account))
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Divider(thickness = 1.dp)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
                    Text(text = stringResource(R.string.label_google_signin))
                }
            }
        }
    }
}

@Composable
private fun SignupCard(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    repeatPassword: String,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
    ) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))) {
            PrimaryOutlinedTextInput(
                value = email,
                labelResourceId = R.string.label_email,
                onValueChange = onEmailChange,
                leadingIcon = Icons.Rounded.Email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            PrimaryOutlinedTextInput(
                value = password,
                labelResourceId = R.string.label_password,
                onValueChange = onPasswordChange,
                isPassword = true,
                leadingIcon = Icons.Rounded.Lock,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            PrimaryOutlinedTextInput(
                value = repeatPassword,
                labelResourceId = R.string.label_repeat_password,
                onValueChange = onRepeatPasswordChange,
                isPassword = true,
                leadingIcon = Icons.Rounded.Lock,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.label_create_account))
            }
            //Cancel Button
            Button(
                onClick = onCancelClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(text = stringResource(R.string.label_cancel))
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Divider(thickness = 1.dp)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
                    Text(text = stringResource(R.string.label_google_signin))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrimaryOutlinedTextInput(
    value: String = "",
    @StringRes labelResourceId: Int,
    onValueChange: (String) -> Unit = {},
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val textTransform = when (isPassword) {
        true -> PasswordVisualTransformation()
        false -> VisualTransformation.None
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = if (leadingIcon != null) {
            { Icon(imageVector = leadingIcon, contentDescription = null) }
        } else null,
        label = {
            Text(
                text = stringResource(labelResourceId), modifier = Modifier
                    .background(Color.White)
                    .padding(
                        horizontal = 5.dp
                    )
            )
        },
        visualTransformation = textTransform,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary
        ),
        keyboardOptions = keyboardOptions
    )
}

@Preview
@Composable
fun DefaultPreview() {
    AquaqualityTheme() {
        StartLoginScreen(
            modifier = Modifier.fillMaxSize(),
            email = "",
            password = "",
            onEmailChange = {},
            onPasswordChange = {},
            signupEmail = "",
            signupPassword = "",
            onSignupEmailChange = {},
            onSignupPasswordChange = {},
            signupRepeatPassword = "",
            onSignupRepeatPasswordChange = {}
        )
    }
}