package com.example.loanapp
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loanapp.home.DebtsScreen
import com.example.loanapp.home.DebtsViewModel
import com.example.loanapp.home.HomeScreen
import com.example.loanapp.signinsignup.SignInScreen
import com.example.loanapp.signinsignup.SignInViewModel
import com.example.loanapp.signinsignup.SignUpScreen
import com.example.loanapp.signinsignup.SignUpViewModel


@Composable
fun NavHost(navController: NavController) {
    val navController = rememberNavController()
    val signUpViewModel = SignUpViewModel()
    val signInViewModel = SignInViewModel()
    NavHost(navController = navController, startDestination = "signin" ){

        composable("signup"){
            //SignUpScreen(signUpViewModel =signUpViewModel , navController = navController)
        }
        composable("signin"){
            //SignInScreen(signInViewModel =signInViewModel , navController = navController)
        }
        composable("home"){
            //HomeScreen(navController = navController)
        }
        composable("debts") {
           // DebtsScreen(navController = navController, debtsViewModel = DebtsViewModel())
        }
    }
}