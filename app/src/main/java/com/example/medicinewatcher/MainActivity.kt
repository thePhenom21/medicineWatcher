package com.example.medicinewatcher

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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

    var homePage : HomePage? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var googleSignIn : GoogleSignInClient = getGoogleLoginAuth()

        var loginPage : LoginPage = LoginPage()

        var alarmMgr = this?.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        var homePage : HomePage = HomePage(this,alarmMgr)

        var signedIn : MutableState<Boolean> = mutableStateOf(false)

        var name : String = ""


        setContent {
            if(signedIn.value){
                homePage!!.createDB()
                homePage!!.mainPage(name)
            }
            else{
                MedicineWatcherTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        loginPage.ButtonGoogleSignIn(
                            onGoogleSignInCompleted = {
                                signedIn.value = true
                                name = it
                            },
                            onError = { println("ERROR") },
                            googleSignInClient = googleSignIn
                        )
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









