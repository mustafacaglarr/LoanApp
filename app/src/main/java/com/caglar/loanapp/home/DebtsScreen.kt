package com.caglar.loanapp.home


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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
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
import com.caglar.loanapp.NotificationService
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtsScreen(debtsViewModel: DebtsViewModel, navController: NavHostController) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var debtAmount by remember { mutableStateOf("") }
    var creditAmount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isDebtSelected by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var pin1 by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val debtsandCreditsState by debtsViewModel.debtsandCredits.observeAsState()
    val debtsandCredits = debtsandCreditsState ?: emptyList()

    Surface(
        color = Color.White, // Arka plan her zaman beyaz
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Borç Bilgileri",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
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
                            .padding(end = 2.dp)
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
                    label = { Text("İsim", color = Color.Black) },
                    textStyle = TextStyle(color = Color.Black),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() } && it.length <= 11) {
                            phoneNumber = it
                        }
                    },
                    label = { Text("Telefon Numarası", color = Color.Black) },
                    placeholder = { Text("0", color = Color.Black) },
                    textStyle = TextStyle(color = Color.Black),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
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
                    label = { Text(if (isDebtSelected) "Borç Miktarı" else "Alacak Miktarı", color = Color.Black) },
                    textStyle = TextStyle(color = Color.Black),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Açıklama", color = Color.Black) },
                    textStyle = TextStyle(color = Color.Black),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Button(
                    onClick = {
                        if (name.isNotBlank() && phoneNumber.isNotBlank() &&
                            (if (isDebtSelected) debtAmount.isNotBlank() else creditAmount.isNotBlank()) &&
                            description.isNotBlank()) {

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
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Tamamla", color = Color.White)
                }

                if (showError) {
                    Text(
                        text = "Lütfen tüm alanları doldurduğunuzdan emin olun.",
                        color = Color.Red,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "PIN Girişi",
                    style = TextStyle(color = Color.Black) // Başlık rengini siyah yap
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isDebtSelected) "Lütfen Borç Eklenecek Kişinin Pin Numarasını Giriniz" else "Lütfen Alacak Eklenecek Kişinin Pin Numarasını Giriniz",
                        style = TextStyle(color = Color.Black)
                    )
                    OutlinedTextField(
                        value = pin1,
                        onValueChange = { pin1 = it },
                        label = { Text("PIN", color = Color.Black) },
                        textStyle = TextStyle(color = Color.Black),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
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
                                    } else {
                                        debtAmount = "0.0"
                                        debtsViewModel.saveCreditandDebt(
                                            name,
                                            phoneNumber,
                                            debtAmount.toDouble(),
                                            creditAmount.toDouble(),
                                            description
                                        )
                                    }
                                    navController.navigate("home")
                                } else {
                                    // PIN yanlış, hata mesajı göster
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Tamamla", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("İptal", color = Color.White)
                }
            },
                    containerColor = Color.White // AlertDialog'un arka plan rengini beyaz yap
        )
    }
}