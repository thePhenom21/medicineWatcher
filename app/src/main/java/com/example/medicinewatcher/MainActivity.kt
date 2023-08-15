package com.example.medicinewatcher

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.example.medicinewatcher.ui.theme.MedicineWatcherTheme
import com.example.medicinewatcher.views.HomePage
import com.example.medicinewatcher.views.LoginPage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.*


class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var googleSignIn : GoogleSignInClient = getGoogleLoginAuth()

        var loginPage : LoginPage = LoginPage()

        var signedIn : MutableState<Boolean> = mutableStateOf(false)


        setContent {
            MedicineWatcherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    if(!signedIn.value) {
                        loginPage.ButtonGoogleSignIn(
                            onGoogleSignInCompleted = {
                                signedIn.value = true
                            },
                            onError = { println("ERROR") },
                            googleSignInClient = googleSignIn
                        )
                    }
                    if(signedIn.value){
                        HomePage(applicationContext).createDB()
                        HomePage(applicationContext).mainPage()
                    }
                }
            }
        }


    }

    private fun getGoogleLoginAuth(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("221882087776-si99bi0vlve6j0s96tkempaid964pbd9.apps.googleusercontent.com")
            .requestId()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }


}









