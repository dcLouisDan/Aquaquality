package com.example.aquaquality.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.LoginUiState
import com.example.aquaquality.presentation.sign_in.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _uiState.update { currentState ->
            currentState.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _uiState.update {
            LoginUiState()
        }
    }

    fun setEmailInput(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = email
            )
        }
    }

    fun setPasswordInput(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password
            )
        }
    }

    fun setSignupEmailInput(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                signup_email = email
            )
        }
    }

    fun setSignUpPasswordInput(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                signup_password = password
            )
        }
    }

    fun setSignUpRepeatPasswordInput(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                signup_repeat_password = password
            )
        }
    }
}