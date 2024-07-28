package com.example.loanapp

import android.annotation.SuppressLint

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.loanapp.home.CustomAlertDialog
import com.example.loanapp.home.DebtsScreen
import com.example.loanapp.home.DebtsViewModel
import com.example.loanapp.home.DefineDebtsScreen
import com.example.loanapp.home.ExpenseScreen
import com.example.loanapp.home.HomeScreen
import com.example.loanapp.home.IncomeScreen
import com.example.loanapp.home.PersonScreen
import com.example.loanapp.signinsignup.SignInScreen
import com.example.loanapp.signinsignup.SignInViewModel
import com.example.loanapp.signinsignup.SignUpScreen
import com.example.loanapp.signinsignup.SignUpViewModel
import com.example.loanapp.signinsignup.SplashScreen
import com.example.loanapp.signinsignup.SplashScreenViewModel

sealed class BottomBarScreen(
    val route:String,
    val title:String,
    val icon:ImageVector
){
    object Home:BottomBarScreen(
        route = "home",
        title = "Ana Sayfa",
        icon = Icons.Default.Home
    )
    object Debts:BottomBarScreen(
        route = "debts",
        title = "Ekle - Sil",
        icon = Icons.Default.Add
    )
    object Person:BottomBarScreen(
        route = "person",
        title = "Kişiler",
        icon = Icons.Default.Person
    )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navbar() {
    val navController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            // SplashScreen veya diğer bottom bar göstermemek istenen ekranlarda bottom bar'ı göstermeyin
            if (!shouldHideBottomBar(navController)) {
                BottomBar(navController = navController)
            }
        }
    ) {
        NavHostt(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController){
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Debts,
        BottomBarScreen.Person,

    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = Color.White
    ) {
        screens.forEach { screen ->
            AddItem(screen = screen, currentDestination = currentDestination, navController = navController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
){
    val showDialog = remember { mutableStateOf(false) }
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(imageVector = screen.icon, contentDescription = "Navigation icon")
        },
        selected = currentDestination?.hierarchy?.any {
            it.route ==screen.route
        } == true,

        onClick = {
            if (screen.route ==BottomBarScreen.Debts.route){
                showDialog.value = true
            }
            else{
                navController.navigate(screen.route)
            }

        }
    )
    if (showDialog.value) {
        CustomAlertDialog(
            showDialog = showDialog.value,
            onDismiss = { showDialog.value = false },
            onItemClick = {
                showDialog.value = false
            },
            navController = navController
        )
    }
}




@Composable
fun shouldHideBottomBar(navController: NavController): Boolean {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    return currentRoute == "splash" || currentRoute == "signin" || currentRoute == "signup"
}
@Composable
fun NavHostt(navController: NavHostController) {
    val splashScreenViewModel = SplashScreenViewModel()
    val signUpViewModel = SignUpViewModel()
    val signInViewModel = SignInViewModel()
    val debtsViewModel = DebtsViewModel()

    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(viewModel = splashScreenViewModel, navController = navController)
        }
        composable("signup") {
            SignUpScreen(signUpViewModel = signUpViewModel, navController = navController)
        }
        composable("signin") {
            SignInScreen(signInViewModel = signInViewModel, navController = navController)
        }
        composable(BottomBarScreen.Home.route) {
            HomeScreen()
        }
        composable(BottomBarScreen.Debts.route) {
            DebtsScreen(debtsViewModel = debtsViewModel, navController = navController)
        }
        composable(BottomBarScreen.Person.route) {
            PersonScreen(debtsViewModel = DebtsViewModel(), navController = navController)
        }
        composable("defineDebts") {
            DefineDebtsScreen(debtsViewModel = debtsViewModel, navController = navController)
        }
        composable("income") {
            IncomeScreen(navController = navController)
        }
        composable("expense") {
            ExpenseScreen(navController = navController)
        }
    }
}