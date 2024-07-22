package com.example.loanapp.signinsignup

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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.loanapp.ui.theme.LoanAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(signInViewModel: SignInViewModel,navController: NavHostController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val signInError by signInViewModel.signInError.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SignInFooter()
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-posta") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Şifre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(
            onClick = {
                signInViewModel.signInUser(email, password,navController,context)


            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)

        ) {
            Text("Giriş yap")
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
        Row {
            Text(
                text = "Hesabın yok mu? ",
                style = TextStyle(color = Color.Gray, fontSize = 18.sp)

            )
            Text(
                text = "Kaydol",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                modifier = Modifier.clickable {
                    navController.navigate("signup")
                }
                )
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
            color = Color.Black,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold

        )
        Text(
            text = "Hesabınıza giriş yapın",
            color = Color.Gray,
            )
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreeenPreview() {
    val signInViewModel = SignInViewModel()
    LoanAppTheme {

    }
}