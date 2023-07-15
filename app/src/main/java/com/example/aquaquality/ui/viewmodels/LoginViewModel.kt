package com.example.aquaquality.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.InputError
import com.example.aquaquality.data.InputField
import com.example.aquaquality.data.LoginUiState
import com.example.aquaquality.presentation.sign_in.SignInResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val auth = Firebase.auth

    fun onSignInResult(result: SignInResult) {
        _uiState.update { currentState ->
            currentState.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]+$")
        return emailRegex.matches(email)
    }

    fun validatePassword(password: String, confirmPassword: String): Boolean {
        // Check if the password is at least 8 characters long
        if (password.length < 8) {
            return false
        }

        // Check if the password contains at least one uppercase letter
        if (!password.contains("[A-Z]")) {
            return false
        }

        // Check if the password contains at least one lowercase letter
        if (!password.contains("[a-z]")) {
            return false
        }

        // Check if the password contains at least one number
        if (!password.contains("[0-9]")) {
            return false
        }

        // Check if the password and confirm password match
        if (password != confirmPassword) {
            return false
        }

        // The password is valid
        return true
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

        if (!isEmailValid(email) && email.isNotEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    inputError = InputError(
                        InputField.SIGNUP_EMAIL,
                        errorMessage = "Invalid email address."
                    )
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    inputError = null
                )
            }
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