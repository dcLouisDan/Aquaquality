package com.example.aquaquality

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.aquaquality.ui.LoginViewModel
import com.example.aquaquality.ui.StartLoginScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import com.example.aquaquality.ui.AquaQualityHomeScreen

@Composable
fun AquaqualityApp(
    loginViewModel: LoginViewModel = viewModel(),
//    navController: NavHostController = rememberNavController()
) {
    val loginUiState by loginViewModel.uiState.collectAsState()
    val isLoggedIn = true
    if (isLoggedIn){
        AquaQualityHomeScreen()
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
            onSignupRepeatPasswordChange = { loginViewModel.setSignUpRepeatPasswordInput(it) }
        )
    }
}