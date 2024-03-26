@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.loanapp.signinsignup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.loanapp.ui.theme.LoanAppTheme

@Composable
fun SignUpScreen(signUpViewModel: SignUpViewModel,navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SignUpFooter()
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Ad") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        )
        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Soyad") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Telefon Numarası") },
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
                signUpViewModel.registerUser(email, password)
                navController.navigate("signin")

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Black)

        ) {
            Text("Register")
        }
    }
}
@Composable
fun SignUpFooter() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Sign up",
            color = androidx.compose.ui.graphics.Color.Black,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold

        )
        Text(
            text = "Create your new account.",
            color = androidx.compose.ui.graphics.Color.Gray,

        )
    }
}


@Preview(showBackground = true)
@Composable
fun SignUpScreeenPreview() {
    val signUpViewModel = SignUpViewModel()
    LoanAppTheme {

    }
}