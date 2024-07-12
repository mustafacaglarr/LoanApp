package com.example.loanapp

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loanapp.home.CustomAlertDialog
import com.example.loanapp.home.DebtsScreen
import com.example.loanapp.home.DebtsViewModel
import com.example.loanapp.home.DefineDebtsScreen
import com.example.loanapp.home.HomeScreen
import com.example.loanapp.home.PersonScreen
import com.example.loanapp.signinsignup.SignInScreen
import com.example.loanapp.signinsignup.SignInViewModel
import com.example.loanapp.signinsignup.SignUpScreen
import com.example.loanapp.signinsignup.SignUpViewModel
sealed class BottomBarScreen(
    val route:String,
    val title:String,
    val icon:ImageVector
){
    object Home:BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Debts:BottomBarScreen(
        route = "debts",
        title = "Debts",
        icon = Icons.Default.Add
    )
    object Person:BottomBarScreen(
        route = "person",
        title = "Person",
        icon = Icons.Default.Person
    )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navbar(){
    val navController: NavHostController = rememberNavController()
    Scaffold(
        bottomBar = {
            if (!isSignInOrSignUp(navController)){
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
fun isSignInOrSignUp(navController: NavController): Boolean {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    return currentRoute == "signin" || currentRoute == "signup"
}

@Composable
fun NavHostt(navController: NavHostController) {
    val signUpViewModel = SignUpViewModel()
    val signInViewModel = SignInViewModel()
    val debtsViewModel = DebtsViewModel()
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = "signin"
    ) {
        composable("signup"){
            SignUpScreen(signUpViewModel =signUpViewModel , navController = navController)
        }
        composable("signin"){
            SignInScreen(signInViewModel =signInViewModel , navController = navController)
        }
        composable(BottomBarScreen.Home.route) {
            HomeScreen()
        }
        composable(
            BottomBarScreen.Debts.route

        ) { backStackEntry ->
            DebtsScreen(debtsViewModel = debtsViewModel, navController = navController)
        }
        composable(BottomBarScreen.Person.route) {
            PersonScreen(debtsViewModel = DebtsViewModel())
        }
        composable("defineDebts") {
            DefineDebtsScreen(debtsViewModel = debtsViewModel,navController = navController)
        }


    }
}