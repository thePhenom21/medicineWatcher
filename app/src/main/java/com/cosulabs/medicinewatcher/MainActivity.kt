package com.cosulabs.medicinewatcher

import android.app.AlarmManager
import android.content.Context
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cosulabs.medicinewatcher.ui.theme.MedicineWatcherTheme
import com.cosulabs.medicinewatcher.views.HomePage
import com.cosulabs.medicinewatcher.views.LoginPage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.*


class MainActivity : ComponentActivity() {

    var homePage : HomePage? = null

    var alarmMgr: AlarmManager? = null

    var googleSignIn : GoogleSignInClient? = null

    var context : Context? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarmMgr = this?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        googleSignIn  = getGoogleLoginAuth()

        context = this




        setContent {
                MedicineWatcherTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        NavContoller()
                    }
                }
            }
        }



    private fun getGoogleLoginAuth(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(client_id)
            .requestId()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun NavContoller(){

        val homePage : HomePage = HomePage(context!!, alarmMgr!!)


        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginPage().ButtonGoogleSignIn(
                    onGoogleSignInCompleted = {
                        navController.navigate("home/$it")
                        homePage.createDB()
                    },
                    onError = { println("ERROR") },
                    googleSignInClient = googleSignIn!!,
                    navController
                )
            }
            composable("home/{name}") {
                backStackEntry -> homePage.mainPage(userName = backStackEntry.arguments?.getString("name")!!, navContoller = navController) }
            }
        }
}










