package com.example.aquaquality

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.aquaquality.ui.LoginViewModel
import com.example.aquaquality.ui.StartLoginScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import com.example.aquaquality.ui.AquaQualityHomeScreen

@Composable
fun AquaqualityApp(
    viewModel: LoginViewModel = viewModel(),
//    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoggedIn = true
    if (isLoggedIn){
        AquaQualityHomeScreen()
    } else {
        StartLoginScreen(
            email = uiState.email,
            password = uiState.password,
            onEmailChange = { viewModel.setEmailInput(it) },
            onPasswordChange = { viewModel.setPasswordInput(it) },
            signupEmail = uiState.signup_email,
            signupPassword = uiState.signup_password,
            signupRepeatPassword = uiState.signup_repeat_password,
            onSignupEmailChange = { viewModel.setSignupEmailInput(it) },
            onSignupPasswordChange = { viewModel.setSignUpPasswordInput(it) },
            onSignupRepeatPasswordChange = { viewModel.setSignUpRepeatPasswordInput(it) }
        )
    }
}