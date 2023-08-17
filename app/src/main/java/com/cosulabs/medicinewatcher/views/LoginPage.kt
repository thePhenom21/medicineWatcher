package com.cosulabs.medicinewatcher.views

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cosulabs.medicinewatcher.AuthResultContract
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

open class LoginPage {


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ButtonGoogleSignIn(
        onGoogleSignInCompleted: (String) -> Unit,
        onError: () -> Unit,
        googleSignInClient: GoogleSignInClient,
        navController : NavHostController
    ) {
        val coroutineScope = rememberCoroutineScope()
        val signInRequestCode = 1


        val authResultLauncher =
            rememberLauncherForActivityResult(contract = AuthResultContract(googleSignInClient)) {
                try {
                    val account = it?.getResult(ApiException::class.java)
                    if (account == null) {
                        onError()
                    } else {
                        coroutineScope.launch {
                            onGoogleSignInCompleted(account.displayName!!)
                        }
                    }
                } catch (e: ApiException) {
                    onError()
                }
            }

        Button(
            onClick = { authResultLauncher.launch(signInRequestCode)
                },
            modifier = Modifier
                .width(300.dp)
                .height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = rememberVectorPainter(Icons.Outlined.Info),
                    contentDescription = "Google icon",
                    tint = Color.Unspecified,
                )
                Text(
                    text = "Access using Google",
                    color = Black,
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }

}