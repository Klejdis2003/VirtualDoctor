package com.packages.main.sign_in

import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    state: SignInState,
    onUserSignInClick: () -> Unit,
    onRestaurantOwnerSignInClick: () -> Unit,
    onRestaurantOwnerRegistrationClick: (name: String) -> Unit,
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let{ error->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "Google Authentication",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            style = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            ),
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ){
            LogInBox(0.5F, onClick = onUserSignInClick) {
                LogInImage(
                    model = "https://www.recoveryranch.com/wp-content/uploads/2021/07/5-Foods-That-Will-Help-With-Addiction-Recovery.jpg",
                    contentDescription = "User"
                )
                Text(
                    text = "User",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            LogInBox(onClick =
            {
                onRestaurantOwnerSignInClick()
            }) {
                LogInImage(
                    model = "https://www.restolacuisine.com/restaurants/restaurant-la-cuisine/website/images/Lacuisine_resto.jpg",
                    contentDescription = "Restaurant Owner"
                )
                Text(
                    text = "Restaurant Owner",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if(state.sheetOpened)
                RestaurantOwnerRegistrationForm(
                    onDissmisRequest = {
                        state.sheetOpened = false
                    },
                    onSubmit = onRestaurantOwnerRegistrationClick,
                    onTextFieldUpdate = {
                        name = it
                    })
        }
    }
}

@Composable
fun LogInImage(model: String, contentDescription: String = ""){
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        contentScale = ContentScale.FillHeight,
        alpha = 0.25f,
    )
}

@Composable
fun LogInBox(@FloatRange (0.0, 1.0) fraction: Float = 1F ,
             onClick: () -> Unit = {},
             content : @Composable (BoxScope.() -> Unit)

){
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(fraction)
        .padding(0.dp, 0.dp)
        .clickable(enabled = true,) { onClick() },
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantOwnerRegistrationForm(
    onSubmit: (String) -> Unit,
    onDissmisRequest: () -> Unit,
    onTextFieldUpdate: (name: String) -> Unit){
    var name by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDissmisRequest,
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .size(300.dp, 200.dp)
        ) {
            Column(
                modifier = Modifier.wrapContentSize(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Complete registration",
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .fillMaxHeight(0.3F),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        onTextFieldUpdate(it) },
                    placeholder = { Text("Name") })
                TextButton(onClick = {
                    onSubmit(name)
                }) {
                    Text("Register")
                }

            }

        }
    }
}