package com.packages.main

import UserRegistrationForm
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.packages.client.AccountType
import com.packages.client.restaurant.RestaurantOwner
import com.packages.client.user.User
import com.packages.main.services.RestaurantOwnerService
import com.packages.main.services.RestaurantService
import com.packages.main.services.UserService
import com.packages.main.sign_in.GoogleAuthUiClient
import com.packages.main.sign_in.SignInScreen
import com.packages.main.sign_in.SignInViewModel
import com.packages.main.ui.theme.VirtualDoctorTheme
import com.packages.profile.HomeScreen
import com.packages.profile.RestaurantOwnerScreen
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val googleAuthUiClient by lazy{
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    private lateinit var accountType: AccountType

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

    private suspend fun redirectToRegistrationPageIfNoAccount(navController: NavController): Boolean{
        var duplicate = false
        val email = googleAuthUiClient.getSignedInUser()?.email
        if(accountType == AccountType.USER && !UserService.exists(email!!)){
            if(!RestaurantOwnerService.exists(email)) {
                Toast.makeText(
                    applicationContext,
                    "Please complete your registration",
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate("user_registration")
                return true;
            }
            else {
                Toast.makeText(
                    applicationContext,
                    "You are a restaurant owner. Please sign in as a restaurant owner",
                    Toast.LENGTH_LONG
                ).show()
                duplicate = true
            }
        }

        else if(accountType == AccountType.RESTAURANT_OWNER && !RestaurantOwnerService.exists(email!!)){
            if(!UserService.exists(email)) {
                Toast.makeText(
                    applicationContext,
                    "Please complete your registration",
                    Toast.LENGTH_LONG
                ).show()
                return true;
            }
            else {
                Toast.makeText(
                    applicationContext,
                    "You are a user. Please sign in as a user",
                    Toast.LENGTH_LONG
                ).show()
                duplicate = true
            }
        }

        if(duplicate){
            lifecycleScope.launch {
                googleAuthUiClient.signOut()
                navController.navigate("sign_in")
            }
        }
        return duplicate
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(googleAuthUiClient.getSignedInUser() != null)
            runBlocking {
                accountType =
                    if (UserService.exists(googleAuthUiClient.getSignedInUser()?.email!!)) AccountType.USER
                    else AccountType.RESTAURANT_OWNER
            }
        else
            accountType = AccountType.USER
        auth = FirebaseAuth.getInstance()
        runBlocking {
            if(!UserService.exists(auth.currentUser?.email))
                auth.signOut()
        }
        setContent {
            VirtualDoctorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
                        composable("sign_in") {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            LaunchedEffect(key1 = Unit ){
                                if(googleAuthUiClient.getSignedInUser() != null){
                                    if(accountType == AccountType.USER)
                                        navController.navigate("home")

                                    else if(accountType == AccountType.RESTAURANT_OWNER )
                                        navController.navigate("owner_home")

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
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccessful){
                                if(state.isSignInSuccessful){
                                    if(redirectToRegistrationPageIfNoAccount(navController)) return@LaunchedEffect
                                    Toast.makeText(
                                       applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    if(accountType == AccountType.USER)
                                        navController.navigate("home")
                                    else if(accountType == AccountType.RESTAURANT_OWNER)
                                        navController.navigate("owner_home")
                                    viewModel.resetState()
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
                                        RestaurantOwnerService.createRestaurantOwner(
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
                        composable("home") {
                            val userData: User? = runBlocking {
                                if(googleAuthUiClient.getSignedInUser() != null) {
                                    UserService.getByEmail(googleAuthUiClient.getSignedInUser()!!.email!!)
                                }
                                else null
                            }
                            val restaurants = runBlocking {
                                RestaurantService.getAll()
                            }
                            HomeScreen(
                                userData = userData,
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.navigate("sign_in")
                                    }
                                },
                                restaurants = restaurants
                            )
                        }
                        composable("user_registration") {
                            UserRegistrationForm(auth.currentUser?.email, onRegistered = {
                                navController.navigate("home")
                                Toast.makeText(
                                    applicationContext,
                                    "Registration successful",
                                    Toast.LENGTH_LONG
                                ).show()
                            })
                        }

                        composable("owner_home"){
                            val userData: RestaurantOwner? = runBlocking {
                                if(googleAuthUiClient.getSignedInUser() != null) {
                                    RestaurantOwnerService.getRestaurantOwner(googleAuthUiClient.getSignedInUser()!!.email!!)
                                }
                                else null
                            }
                            val restaurants = runBlocking {
                                RestaurantService.getAll()
                            }
                            RestaurantOwnerScreen(ownerData = userData!!)
                        }

                    }
                }
            }
        }
    }

}





