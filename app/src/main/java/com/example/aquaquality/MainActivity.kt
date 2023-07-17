package com.example.aquaquality


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aquaquality.presentation.sign_in.GoogleAuthUiClient
import com.example.aquaquality.ui.components.ConnectionAlertDialog
import com.example.aquaquality.ui.screens.AquaQualityHomeScreen
import com.example.aquaquality.ui.screens.LoadingScreen
import com.example.aquaquality.ui.theme.AquaqualityTheme
import com.example.aquaquality.ui.viewmodels.LoginViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private lateinit var connectivityObserver: ConnectivityObserver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        setContent {
            AquaqualityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val status by connectivityObserver.observe().collectAsState(
                        initial = ConnectivityObserver.Status.Unavailable
                    )
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "loading") {
                        composable("loading") {
                            LoadingScreen()
                            if (status == ConnectivityObserver.Status.Available) {
                                LaunchedEffect(key1 = Unit) {
                                    if (googleAuthUiClient.getSignedInUser() != null) {
                                        navController.navigate("home")
                                    } else {
                                        navController.navigate("sign_in")
                                    }
                                }
                            } else {
                                ConnectionAlertDialog(
                                    onConfirmClick = {
                                        recreate()
                                    },
                                    onDismissRequest = { /*TODO*/ })
                            }
                        }

                        composable("sign_in") {
                            val loginViewModel: LoginViewModel = viewModel()
                            val loginUiState by loginViewModel.uiState.collectAsStateWithLifecycle()

                            if (status != ConnectivityObserver.Status.Available) {
                                ConnectionAlertDialog(
                                    onConfirmClick = {
                                        recreate()
                                    },
                                    onDismissRequest = { /*TODO*/ })
                            }

                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("home")
                                }
                            }

                            LaunchedEffect(key1 = loginUiState.isSignInSuccessful) {
                                if (loginUiState.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("home")
                                    loginViewModel.resetState()
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            loginViewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            AquaqualityApp(
                                loginViewModel = loginViewModel,
                                loginUiState = loginUiState,
                                onGoogleSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                })
                        }

                        composable("home") {
                            if (status != ConnectivityObserver.Status.Available) {
                                ConnectionAlertDialog(
                                    onConfirmClick = {
                                        recreate()
                                    },
                                    onDismissRequest = { /*TODO*/ })
                            }

                            AquaQualityHomeScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onLogoutClick = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.popBackStack()
                                    }
                                }
                            )

                        }
                    }
                }
            }
        }
    }

}

