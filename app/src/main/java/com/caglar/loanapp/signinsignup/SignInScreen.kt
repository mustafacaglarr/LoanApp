package com.caglar.loanapp.signinsignup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(signInViewModel: SignInViewModel, navController: NavHostController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val signInResult by signInViewModel.signInResult.observeAsState()
    val signInError by signInViewModel.signInError.observeAsState()
    val currentUserEmail by signInViewModel.currentUserEmail.observeAsState()

    Surface(
        color = Color.White, // Arka plan her zaman beyaz
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignInFooter()
            Spacer(modifier = Modifier.height(15.dp))

            val textColor = Color.Black // Yazı rengini her zaman siyah yapıyoruz

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-posta", color = textColor) },
                textStyle = TextStyle(color = textColor),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Şifre", color = textColor) },
                textStyle = TextStyle(color = textColor),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        signInViewModel.setSignInError("E-posta ve şifre boş geçilemez")
                    } else {
                        signInViewModel.signInUser(email, password, navController, context)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Giriş yap", color = Color.White)
            }

            Spacer(modifier = Modifier.height(13.dp))

            signInError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(8.dp)
                )
            }

            signInResult?.let {
                if (it) {
                    Text(
                        text = "Giriş başarılı!",
                        color = Color.Green,
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(8.dp)
                    )
                    currentUserEmail?.let { email ->
                        Text(
                            text = "Hoş geldiniz, $email",
                            style = TextStyle(fontSize = 16.sp, color = textColor),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            Row {
                Text(
                    text = "Hesabın yok mu? ",
                    style = TextStyle(color = textColor, fontSize = 18.sp)
                )
                Text(
                    text = "Kaydol",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black),
                    modifier = Modifier.clickable {
                        navController.navigate("signup")
                    }
                )
            }
        }
    }
}

@Composable
fun SignInFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Merhaba,",
            color = Color.Black, // Her zaman siyah
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Hesabınıza giriş yapın",
            color = Color.Gray // Her zaman gri
        )
    }
}