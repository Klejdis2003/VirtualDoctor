package com.packages.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.packages.main.sign_in.UserData

@Composable
fun HomeScreen(
    userData: UserData?,
    onSignOut: () -> Unit
){
    Box(){
        if (userData != null) {
            Text(
                text = "${userData.email}, ${userData.username}",
                modifier = Modifier.align(Alignment.Center)
            )
            Button(
                onClick = onSignOut,
                modifier = Modifier.align(Alignment.BottomCenter)
            ){
                Text(text = "Sign Out")
            }
        }
    }
}
