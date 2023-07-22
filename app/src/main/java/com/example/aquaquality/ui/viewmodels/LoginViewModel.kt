package com.example.aquaquality.ui.viewmodels


import androidx.lifecycle.ViewModel
import com.example.aquaquality.data.InputError
import com.example.aquaquality.data.InputField
import com.example.aquaquality.data.LoginUiState
import com.example.aquaquality.presentation.sign_in.SignInResult
import com.example.aquaquality.presentation.sign_in.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException


fun String.isLongEnough() = length >= 8
fun String.hasEnoughDigits() = count(Char::isDigit) > 0
fun String.isMixedCase() = any(Char::isLowerCase) && any(Char::isUpperCase)

// you can decide which requirements need to be included (or make separate lists
// of different priority requirements, and check that enough of each have been met)
val requirements = listOf(String::isLongEnough, String::hasEnoughDigits, String::isMixedCase)
val String.meetsRequirements get() = requirements.all { check -> check(this) }


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

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]+$")
        return emailRegex.matches(email)
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

        if (!password.meetsRequirements && password.isNotEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    inputError = InputError(
                        InputField.SIGNUP_PASSWORD,
                        errorMessage = "The password must be at least 8 characters long and at least one uppercase letter, one lowercase letter, and one number"
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

    fun setSignUpRepeatPasswordInput(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                signup_repeat_password = password
            )
        }

        if (uiState.value.signup_password != password) {
            _uiState.update { currentState ->
                currentState.copy(
                    inputError = InputError(
                        InputField.SIGNUP_REPEAT_PASSWORD,
                        errorMessage = "Passwords do not match."
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

    fun createNewUser(email: String, password: String) {
        if (!isEmailValid(email) && email.isNotEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    inputError = InputError(
                        InputField.SIGNUP_EMAIL,
                        errorMessage = "Invalid email address."
                    )
                )
            }
            return
        }

        if (!password.meetsRequirements && password.isNotEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    inputError = InputError(
                        InputField.SIGNUP_PASSWORD,
                        errorMessage = "The password must be at least 8 characters long and at least one uppercase letter, one lowercase letter, and one number"
                    )
                )
            }
            return
        }

        if (uiState.value.inputError != null) return

        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isSignUpSuccessful = true
                        )
                    }

                } else {
                    _uiState.update { currentState ->
                        currentState.copy(
                            signInError = "Sign-up failed"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    suspend fun signInWithEmailPassword(email: String, password: String): SignInResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password)
                .await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        email = email,
                        profilePictureUrl = photoUrl.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }
}