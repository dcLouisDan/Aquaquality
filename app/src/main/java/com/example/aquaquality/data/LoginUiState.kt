package com.example.aquaquality.data

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val signup_email: String = "",
    val signup_password: String = "",
    val signup_repeat_password: String = "",
    val isSignInSuccessful: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val signInError: String? = null,
    val inputError: InputError? = null
)

enum class InputField {
    LOGIN_EMAIL,
    LOGIN_PASSWORD,
    SIGNUP_EMAIL,
    SIGNUP_PASSWORD,
    SIGNUP_REPEAT_PASSWORD
}

data class InputError(
    val inputField: InputField,
    val errorMessage: String
)