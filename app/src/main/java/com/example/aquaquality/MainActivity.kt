package com.example.aquaquality


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
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
import com.example.aquaquality.utilities.DataStoreManager
import com.example.aquaquality.utilities.DatabaseNotificationUtil
import com.google.accompanist.permissions.*
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import android.Manifest.permission.POST_NOTIFICATIONS
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.aquaquality.ui.components.PermissionAlertDialog


class MainActivity : AppCompatActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private lateinit var connectivityObserver: ConnectivityObserver
    private val dbNotification = DatabaseNotificationUtil()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission Request", "Granted")
            } else {
                // The user denied the permission.
                Log.i("Permission Request", "Denied")
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
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
                    var permissionGranted by rememberSaveable {
                        mutableStateOf(false)
                    }
                    var showPermissionDialog by rememberSaveable {
                        mutableStateOf(false)
                    }
                    when {
                        ContextCompat.checkSelfPermission(
                            applicationContext,
                            POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            permissionGranted = true
                            createNotificationChannel()
                            dbNotification.watch(applicationContext)
                        }

                        shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)
                        -> {
                            showPermissionDialog = true
                        }

                        else -> {
                            requestPermissionLauncher.launch(POST_NOTIFICATIONS)
                        }
                    }
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

                            LaunchedEffect(key1 = loginUiState.isSignUpSuccessful) {
                                if (loginUiState.isSignUpSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Account successfully created",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    val result = loginViewModel.signInWithEmailPassword(
                                        loginUiState.signup_email,
                                        loginUiState.signup_password
                                    )

                                    loginViewModel.onSignInResult(result)
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
                                onLoginClick = {
                                    lifecycleScope.launch {
                                        val result = loginViewModel.signInWithEmailPassword(
                                            loginUiState.email,
                                            loginUiState.password
                                        )

                                        loginViewModel.onSignInResult(result)
                                    }
                                },
                                onGoogleSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                },
                                onSignUpClick = {
                                    lifecycleScope.launch {
                                        loginViewModel.createNewUser(
                                            loginUiState.signup_email,
                                            loginUiState.signup_password
                                        )
                                    }
                                }
                            )
                        }



                        composable("home") {
                            val context = LocalContext.current
                            val scope = rememberCoroutineScope()
                            val dataStore = DataStoreManager(context)
                            val darkThemeState =
                                dataStore.getTheme().collectAsState(initial = isSystemInDarkTheme())
                            val languageCode =
                                dataStore.getLanguage().collectAsState(initial = "en")
                            Log.i("Language Locale", "${AppCompatDelegate.getApplicationLocales()}")

                            if (showPermissionDialog) {
                                PermissionAlertDialog(
                                    onConfirmClick = {
                                        requestPermissionLauncher.launch(POST_NOTIFICATIONS)
                                        showPermissionDialog = false
                                    },
                                    onDismissRequest = { showPermissionDialog = false })
                            }


                            AquaqualityTheme(darkTheme = darkThemeState.value) {
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
                                    },
                                    afterSaveAction = {
                                        restartApp()
                                    },
                                    exitApp = { finish() },
                                    darkThemeState = darkThemeState.value,
                                    darkThemeToggleAction = {
                                        scope.launch {
                                            dataStore.setTheme(it)
                                        }
                                    },
                                    isPermissionGranted = permissionGranted,
                                     onPermissionTurnOn = {
                                         val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                         intent.data = Uri.fromParts("package", packageName, null)
                                         startActivity(intent)
                                     },
                                    onLanguageChange = { code ->
                                        scope.launch {
                                            dataStore.setLanguage(code)
                                            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(code))
                                            restartApp()
                                        }
                                    },
                                    currentLanguageCode = languageCode.value
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notif_channel_name)
            val descriptionText = getString(R.string.notif_channel_desc)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(name, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun restartApp() {
        val ctx = applicationContext
        val pm = ctx.packageManager
        val intent = pm.getLaunchIntentForPackage(ctx.packageName)
        val mainIntent = Intent.makeRestartActivityTask(intent!!.component)
        ctx.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}

