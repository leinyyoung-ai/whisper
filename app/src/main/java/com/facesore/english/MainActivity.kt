package com.facesore.english

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.facesore.english.auth.AuthState
import com.facesore.english.auth.LoginViewModel
import com.facesore.english.ui.screens.LoginScreen
import com.facesore.english.ui.screens.VocabularyTestScreen
import com.facesore.english.ui.theme.FaceSoreEnglishTheme

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FaceSoreEnglishTheme {
                val authState by loginViewModel.authState.collectAsState()

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    when (authState) {
                        is AuthState.LoggedIn -> {
                            VocabularyTestScreen()
                        }
                        is AuthState.LoggedOut, is AuthState.Loading, is AuthState.Error -> {
                            LoginScreen(
                                onLoginClick = { email, password ->
                                    loginViewModel.login(email, password)
                                },
                                onRegisterClick = { email, password ->
                                    loginViewModel.register(email, password)
                                }
                            )
                        }
                    }
                }

                LaunchedEffect(authState) {
                    if (authState is AuthState.Error) {
                        Toast.makeText(this@MainActivity, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
