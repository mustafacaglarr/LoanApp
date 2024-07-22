@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.loanapp.ui.theme.LoanAppTheme

@Composable
fun SignUpScreen(signUpViewModel: SignUpViewModel, navController: NavHostController) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNum by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isSubmitClicked by remember { mutableStateOf(false) }
    val registrationStatus by signUpViewModel.registrationStatus.observeAsState()
    val isRegistrationSuccessful by signUpViewModel.isRegistrationSuccessful.observeAsState()


    // Şifre doğrulama işlevi
    fun validatePassword(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val isLengthValid = password.length in 7..15
        return hasUpperCase && isLengthValid
    }

    // Şifre doğrulama hatalarını döndüren işlev
    fun getPasswordValidationError(password: String): String? {
        return when {
            password.length < 7 -> "Şifre en az 7 karakter uzunluğunda olmalıdır."
            password.length > 15 -> "Şifre en fazla 15 karakter uzunluğunda olmalıdır."
            !password.any { it.isUpperCase() } -> "Şifre en az bir büyük harf içermelidir."
            else -> null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Adı") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Soyadı") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-posta") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        // Kayıt durumu mesajını göster
        registrationStatus?.let {
            Text(
                text = it,
                color = if (isRegistrationSuccessful == true) Color.Green else Color.Red,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        OutlinedTextField(
            value = phoneNum,
            onValueChange = { if (it.length <= 11) phoneNum = it },
            placeholder = { Text("0") },
            label = { Text("Telefon Numarası") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (isSubmitClicked) {
                    passwordError = getPasswordValidationError(it)
                }
            },
            label = { Text("Şifre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        // Hata mesajını göster
        passwordError?.let {
            Text(
                text = it,
                color = Color.Red,
                style = TextStyle(fontSize = 14.sp)
            )
        }
        Button(
            onClick = {
                isSubmitClicked = true
                if (validatePassword(password)) {
                    signUpViewModel.registerUser(email, password, phoneNum, context)
                } else {
                    passwordError = getPasswordValidationError(password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Kaydol")
        }
        // Kayıt işlemi başarılı olduğunda yönlendirme
        if (isRegistrationSuccessful == true) {
            LaunchedEffect(Unit) {
                navController.navigate("signin")
            }
        }
        Spacer(modifier = Modifier.height(13.dp))
        Row {
            Text(
                text = "Hesabın var mı? ",
                style = TextStyle(color = Color.Gray, fontSize = 18.sp)
            )
            Text(
                text = "Giriş yap",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                modifier = Modifier.clickable {
                    navController.navigate("signin")
                }
            )
        }
    }
}