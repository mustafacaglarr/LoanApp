package com.example.loanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.loanapp.signinsignup.SignInViewModel
import com.example.loanapp.signinsignup.SignUpViewModel
import com.example.loanapp.ui.theme.LoanAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoanAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val signInViewModel = SignInViewModel()
                    val signUpViewModel = SignUpViewModel()
                    val navController = rememberNavController()
                    //NavHost(navController)
                    Navbar()


                }
            }
        }
    }
    }

