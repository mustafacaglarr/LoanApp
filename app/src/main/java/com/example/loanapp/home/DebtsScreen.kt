package com.example.loanapp.home

import android.widget.ToggleButton
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.loanapp.ui.theme.LoanAppTheme

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
                onValueChange = { phoneNumber = it },
                label = { Text("Telefon Numarası") },
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
                    Text("Lütfen PIN girin")
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
                        debtsViewModel.getPinByPhoneNumber(phoneNumber) { pin ->
                            pin?.let {
                                if (pin == pin1) {
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

@Preview(showBackground = true)
@Composable
fun DebtsScreenPreview() {
    LoanAppTheme {

    }
}
