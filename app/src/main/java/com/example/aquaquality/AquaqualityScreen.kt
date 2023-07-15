package com.example.aquaquality


import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.aquaquality.ui.viewmodels.LoginViewModel
import com.example.aquaquality.ui.screens.StartLoginScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.aquaquality.data.LoginUiState
@Composable
fun AquaqualityApp(
    loginViewModel: LoginViewModel,
    loginUiState: LoginUiState,
    onGoogleSignInClick: () -> Unit
) {
    val context = LocalContext.current
    var isLoggedIn by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = loginUiState.signInError) {
        loginUiState.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

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
        onLoginPress = { isLoggedIn = true },
        onGoogleSignClick = onGoogleSignInClick,
        inputError = loginUiState.inputError
    )
}