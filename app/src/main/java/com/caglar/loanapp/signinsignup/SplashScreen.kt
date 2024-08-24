package com.caglar.loanapp.signinsignup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.caglar.loanapp.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(viewModel: SplashScreenViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val navigateTo by viewModel.navigateTo.observeAsState()

    // Giriş yapma kontrolünü başlat
    LaunchedEffect(Unit) {
        delay(3000)
        viewModel.checkUserAuthentication(context)

    }

    LaunchedEffect(navigateTo) {
        navigateTo?.let {
            navController.navigate(it) {
                // Geri dönecek hiçbir geçmiş kaydını bırakma
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    // Splash screen UI
    Box(
        contentAlignment = Alignment.Center,
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(Color.White) // Arka plan rengini beyaz yapın
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Splash Image",
            modifier = androidx.compose.ui.Modifier.size(200.dp) // Resmin boyutunu artırın
        )
    }
}