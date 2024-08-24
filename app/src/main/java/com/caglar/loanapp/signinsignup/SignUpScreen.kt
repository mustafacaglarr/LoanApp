@file:OptIn(ExperimentalMaterial3Api::class)

package com.caglar.loanapp.signinsignup

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

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
    var nameError by remember { mutableStateOf<String?>(null) }
    var surnameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneNumError by remember { mutableStateOf<String?>(null) }
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

    // Genel validasyon işlevi
    fun validateFields(): Boolean {
        var isValid = true

        if (name.isBlank()) {
            nameError = "Ad boş olamaz."
            isValid = false
        } else {
            nameError = null
        }

        if (surname.isBlank()) {
            surnameError = "Soyad boş olamaz."
            isValid = false
        } else {
            surnameError = null
        }

        if (email.isBlank()) {
            emailError = "E-posta boş olamaz."
            isValid = false
        } else {
            emailError = null
        }

        if (phoneNum.isBlank()) {
            phoneNumError = "Telefon numarası boş olamaz."
            isValid = false
        } else {
            phoneNumError = null
        }

        return isValid
    }

    Surface(
        color = Color.White, // Dark modda bile beyaz arka plan
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(15.dp))
            val textColor = Color.Black // Dark modda bile siyah yazı rengi

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Adı", color = textColor) },
                textStyle = TextStyle(color = textColor),
                isError = nameError != null,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            nameError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Soyadı", color = textColor) },
                textStyle = TextStyle(color = textColor),
                isError = surnameError != null,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            surnameError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-posta", color = textColor) },
                textStyle = TextStyle(color = textColor),
                isError = emailError != null,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            emailError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = TextStyle(fontSize = 14.sp)
                )
            }

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
                onValueChange = {
                    phoneNum = it.filter { char -> char.isDigit() }
                },
                placeholder = { Text("0", color = textColor) },
                label = { Text("Telefon Numarası", color = textColor) },
                textStyle = TextStyle(color = textColor),
                isError = phoneNumError != null,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            phoneNumError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (isSubmitClicked) {
                        passwordError = getPasswordValidationError(it)
                    }
                },
                label = { Text("Şifre", color = textColor) },
                textStyle = TextStyle(color = textColor),
                isError = passwordError != null,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
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
                    if (validateFields() && validatePassword(password)) {
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
                Text("Kaydol", color = Color.White)
            }

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
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black),
                    modifier = Modifier.clickable {
                        navController.navigate("signin")
                    }
                )
            }
        }
    }
}