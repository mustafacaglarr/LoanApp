package com.example.loanapp.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loanapp.NotificationService
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtsScreen(debtsViewModel: DebtsViewModel, navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var debtAmount by remember { mutableStateOf("") }
    var creditAmount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isDebtSelected by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var pin1 by remember { mutableStateOf("") }
    val debtsandCreditsState by debtsViewModel.debtsandCredits.observeAsState()
    val debtsandCredits = debtsandCreditsState ?: emptyList()

    // Alınan Context
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Borç Bilgileri",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = { isDebtSelected = true },
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDebtSelected) Color.Red else Color.White,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .absolutePadding(right = 2.dp)
                        .shadow(elevation = 10.dp)
                ) {
                    Text(
                        text = "Borç",
                        style = TextStyle(color = Color.Black)
                    )
                }

                Button(
                    onClick = { isDebtSelected = false },
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isDebtSelected) Color.Green else Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .shadow(elevation = 10.dp)
                ) {
                    Text(
                        text = "Alacak",
                        style = TextStyle(color = Color.Black)
                    )
                }
            }
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("İsim") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(16.dp),
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 11) phoneNumber = it },
                label = { Text("Telefon Numarası") },
                placeholder = { Text("0") },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(16.dp),
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            OutlinedTextField(
                value = if (isDebtSelected) debtAmount else creditAmount,
                onValueChange = { text ->
                    if (isDebtSelected) {
                        debtAmount = text
                    } else {
                        creditAmount = text
                    }
                },
                label = { Text(if (isDebtSelected) "Borç Miktarı" else "Alacak Miktarı") },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(16.dp),
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Açıklama") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(16.dp),
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Button(
                onClick = {
                    debtsViewModel.getPinByPhoneNumber(phoneNumber) { pin ->
                        pin?.let {
                            showDialog = true // PIN bulundu, dialog göster
                        } ?: run {
                            // PIN bulunamadı, doğrudan kaydet
                            if (isDebtSelected) {
                                creditAmount = "0.0"
                                debtsViewModel.saveCreditandDebt(
                                    name,
                                    phoneNumber,
                                    debtAmount.toDouble(),
                                    creditAmount.toDouble(),
                                    description
                                )
                                navController.navigate("home")
                            } else {
                                debtAmount = "0.0"
                                debtsViewModel.saveCreditandDebt(
                                    name,
                                    phoneNumber,
                                    debtAmount.toDouble(),
                                    creditAmount.toDouble(),
                                    description
                                )
                                navController.navigate("home")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Tamamla")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("PIN Girişi") },
            text = {
                Column {
                    Text(if (isDebtSelected) "Lütfen Borç Eklenecek Kişinin Pin Numarasını Giriniz" else "Lütfen Alacak Eklenecek Kişinin Pin Numarasını Giriniz")
                    OutlinedTextField(
                        value = pin1,
                        onValueChange = { pin1 = it },
                        label = { Text("PIN") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(16.dp),
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Firebase.messaging.subscribeToTopic("Tutorial")
                        debtsViewModel.getPinByPhoneNumber(phoneNumber) { pin ->
                            pin?.let {
                                if (pin == pin1) {
                                    val notificationService = NotificationService(context)
                                    notificationService.showNotification(
                                        title = "Borç-Alacak Ekleme Başarılı ",
                                        message = "$name isimli kullanıcıya ekleme işlemi tamamlandı."
                                    )
                                    showDialog = false
                                    if (isDebtSelected) {
                                        creditAmount = "0.0"
                                        debtsViewModel.saveCreditandDebt(
                                            name,
                                            phoneNumber,
                                            debtAmount.toDouble(),
                                            creditAmount.toDouble(),
                                            description
                                        )

                                        navController.navigate("home")
                                    } else {
                                        debtAmount = "0.0"
                                        debtsViewModel.saveCreditandDebt(
                                            name,
                                            phoneNumber,
                                            debtAmount.toDouble(),
                                            creditAmount.toDouble(),
                                            description
                                        )
                                        navController.navigate("home")
                                    }
                                } else {
                                    val notificationService = NotificationService(context)
                                    notificationService.showNotification(
                                        title = "Borç-Alacak Ekle Başarısız ",
                                        message = "$name isimli kullanıcıya ekleme işlemi tamamlanmadı!!"
                                    )
                                    // Handle incorrect PIN case
                                }
                            } ?: run {
                                // Handle case where PIN is null (user not found)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Onayla")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("İptal")
                }
            }
        )
    }
}