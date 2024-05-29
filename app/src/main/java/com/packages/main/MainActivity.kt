package com.packages.main

import UserRegistrationForm
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.android.gms.auth.api.identity.Identity
import com.packages.data.model.AccountType
import com.packages.data.model.restaurant.RestaurantOwner
import com.packages.data.repositories.RestaurantOwnerRepository
import com.packages.data.repositories.UserRepository
import com.packages.main.sign_in.GoogleAuthUiClient
import com.packages.main.sign_in.SignInScreen
import com.packages.main.sign_in.SignInState
import com.packages.main.sign_in.SignInViewModel
import com.packages.main.ui.theme.VirtualDoctorTheme
import com.packages.main.utils.HttpClient
import com.packages.ui.owner_profile.OwnerViewModel
import com.packages.ui.owner_profile.RestaurantOwnerScreen
import com.packages.ui.registration.UserRegistrationViewModel
import com.packages.ui.registration.UserRegistrationViewModelFactory
import com.packages.ui.user_profile.HomeScreen
import com.packages.ui.user_profile.HomeViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy{
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    private val httpClient = HttpClient()
    private var accountType: AccountType? = null
    private lateinit var dependencyContainer: DependencyContainer
    private lateinit var userRepository: UserRepository
    private lateinit var restaurantOwnerRepository: RestaurantOwnerRepository

    /**
     * Launches the sign in flow. It first requests google sign in intent sender and then launches it.
     * @param launcher The launcher for the intent sender request
     */
    private fun onSignInClick(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>){
        lifecycleScope.launch {
            val signInIntentSender = googleAuthUiClient.signIn()
            launcher.launch(
                IntentSenderRequest.Builder(
                    signInIntentSender ?: return@launch
                ).build()
            )
        }
    }

    private fun checkIfUserIsRegisteredOnSignIn(email: String, navController: NavController, state: SignInState){
        runBlocking{
            launch{
                try{
                    if(accountType == AccountType.USER && !userRepository.exists(email)){
                        navController.navigate("user_registration")
                    }
                    else if(accountType == AccountType.RESTAURANT_OWNER && !restaurantOwnerRepository.exists(email))
                        state.sheetOpened = true
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dependencyContainer = DependencyContainer()
        userRepository = dependencyContainer.userRepository
        restaurantOwnerRepository = dependencyContainer.restaurantOwnerRepository

        enableEdgeToEdge()
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
//        insetsController.apply {
//            hide(WindowInsetsCompat.Type.statusBars())
//            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }

        runBlocking {
            launch {
                try {
                    if (googleAuthUiClient.getSignedInUser() == null)
                        return@launch

                    accountType =
                        if (userRepository.exists(googleAuthUiClient.getSignedInUser()?.email)) {
                            AccountType.USER
                        } else if (restaurantOwnerRepository.exists(googleAuthUiClient.getSignedInUser()?.email)) {
                            AccountType.RESTAURANT_OWNER
                        } else {
                            null
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            setContent {
                VirtualDoctorTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = "auth") {
                            navigation(route = "auth", startDestination = "sign_in") {
                                composable("sign_in") {
                                    val viewModel = viewModel<SignInViewModel>()
                                    val state by viewModel.state.collectAsStateWithLifecycle()
                                    LaunchedEffect(key1 = Unit) {
                                        if (googleAuthUiClient.getSignedInUser() != null) {
                                            if (accountType == AccountType.USER)
                                                navController.navigate("home")
                                            else if (accountType == AccountType.RESTAURANT_OWNER)
                                                navController.navigate("owner_home")

                                        }
                                    }
                                    val launcher = rememberLauncherForActivityResult(
                                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                                        onResult = { result ->
                                            if (result.resultCode == RESULT_OK) {
                                                lifecycleScope.launch {

                                                    val signInResult =
                                                        googleAuthUiClient.signInWithIntent(
                                                            intent = result.data ?: return@launch,
                                                            onSignInComplete = { email ->
                                                                if (email != null) {
                                                                    checkIfUserIsRegisteredOnSignIn(
                                                                        email,
                                                                        navController,
                                                                        state
                                                                    )
                                                                }
                                                            }
                                                        )
                                                    viewModel.onSignInResult(signInResult)
                                                }
                                            }
                                        }
                                    )

                                    LaunchedEffect(key1 = state.isSignInSuccessful) {
                                        if (state.isSignInSuccessful) {
                                            Toast.makeText(
                                                applicationContext,
                                                "Sign in successful",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            if (accountType == AccountType.USER)
                                                navController.navigate("home")
                                            else if (accountType == AccountType.RESTAURANT_OWNER && restaurantOwnerRepository.exists(
                                                    googleAuthUiClient.getSignedInUser()?.email!!
                                                )
                                            )
                                                navController.navigate("owner_home")
                                            viewModel.resetState(isSheetOpened = state.sheetOpened)
                                        }
                                    }
                                    SignInScreen(
                                        state = state,
                                        onUserSignInClick = {
                                            onSignInClick(launcher)
                                            accountType = AccountType.USER
                                        },
                                        onRestaurantOwnerSignInClick = {
                                            onSignInClick(launcher)
                                            accountType = AccountType.RESTAURANT_OWNER
                                        },
                                        onRestaurantOwnerRegistrationClick = { name ->

                                            lifecycleScope.launch {
                                                restaurantOwnerRepository.createRestaurantOwner(
                                                    RestaurantOwner(
                                                        username = name,
                                                        email = googleAuthUiClient.getSignedInUser()?.email!!
                                                    )
                                                )
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Registration successful",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                navController.navigate("owner_home")
                                            }
                                        }
                                    )
                                }
                                composable("user_registration") {
                                    val userRegistrationViewModel =
                                        viewModel<UserRegistrationViewModel>(
                                            factory = UserRegistrationViewModelFactory(
                                                email = googleAuthUiClient.getSignedInUser()?.email!!,
                                                userRepository = userRepository,
                                                nutritionRepository = dependencyContainer.nutritionRepository,
                                                redirect = { navController.navigate("home") }
                                            )
                                        )
                                    UserRegistrationForm(userRegistrationViewModel)
                                }
                                composable("home") {
                                    val loggedInEmail = googleAuthUiClient.getSignedInUser()?.email
                                    HomeScreen(
                                        onSignOut = {
                                            lifecycleScope.launch {
                                                googleAuthUiClient.signOut()
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Signed out",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                navController.navigate("auth")
                                            }
                                        },
                                        homeViewModel = HomeViewModel(
                                            loggedInEmail,
                                            dependencyContainer.userRepository,
                                            dependencyContainer.restaurantRepository,
                                            dependencyContainer.itemRepository,
                                            onNullUser = {
                                                navController.navigate("auth")
                                                Toast.makeText(
                                                    applicationContext,
                                                    "An error occurred. Please sign in again.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        ),
                                    )
                                }


                                composable("owner_home") {
                                    val email = googleAuthUiClient.getSignedInUser()?.email
                                    if (email == null) {
                                        navController.navigate("auth")
                                        return@composable
                                    }
                                    val viewModel = OwnerViewModel(email,
                                        restaurantOwnerRepository,
                                        dependencyContainer.restaurantRepository,
                                        dependencyContainer.itemRepository)
                                    RestaurantOwnerScreen(ownerViewModel = viewModel, onSignOut = {
                                        lifecycleScope.launch {
                                            googleAuthUiClient.signOut()

                                            Toast.makeText(
                                                applicationContext,
                                                "Signed out",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            navController.navigate("auth")
                                        }
                                    })
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}





