package com.example.aquaquality

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.aquaquality.ui.viewmodels.LoginViewModel
import com.example.aquaquality.ui.screens.StartLoginScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.aquaquality.ui.screens.AquaQualityHomeScreen

@Composable
fun AquaqualityApp(
    loginViewModel: LoginViewModel = viewModel(),
) {
    val loginUiState by loginViewModel.uiState.collectAsState()
    var isLoggedIn by remember {
        mutableStateOf(false)
    }
    if (isLoggedIn){
        AquaQualityHomeScreen(onLogoutClick = { isLoggedIn = false })
    } else {
        StartLoginScreen(
            email = loginUiState.email,
            password = loginUiState.password,
            onEmailChange = { loginViewModel.setEmailInput(it) },
            onPasswordChange = { loginViewModel.setPasswordInput(it) },
            signupEmail = loginUiState.signup_email,
            signupPassword = loginUiState.signup_password,
            signupRepeatPassword = loginUiState.signup_repeat_password,
            onSignupEmailChange = { loginViewModel.setSignupEmailInput(it) },
            onSignupPasswordChange = { loginViewModel.setSignUpPasswordInput(it) },
            onSignupRepeatPasswordChange = { loginViewModel.setSignUpRepeatPasswordInput(it) },
            onLoginPress = {isLoggedIn = true}
        )
    }
}