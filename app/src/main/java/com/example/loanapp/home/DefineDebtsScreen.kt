package com.example.loanapp.home

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefineDebtsScreen(debtsViewModel: DebtsViewModel, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<CreditAndDebt?>(null) }
    var debtAmount by remember { mutableStateOf("") }
    var creditAmount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var creditId by remember { mutableStateOf("") }
    var isDebtSelected by remember { mutableStateOf(false) }
    var pin1 by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val debtsAndCreditsState by debtsViewModel.debtsandCredits.observeAsState()
    val debtsAndCredits = debtsAndCreditsState ?: emptyList()
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
            Text(text = "Tanımlı Borç - Alacak Bilgileri", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(5.dp))

            Box {
                Text(
                    text = selectedOption?.name ?: "Seçiniz",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = true })
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(5.dp),
                    style = MaterialTheme.typography.bodyLarge

                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    debtsAndCredits.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.name) },
                            onClick = {
                                selectedOption = item
                                expanded = false
                                debtAmount = item.debtAmount.toString()
                                creditAmount = item.creditAmount.toString()
                                description = item.description
                                creditId = item.creditId
                            }
                        )
                    }
                }
            }

            selectedOption?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Seçilen Kişinin Adı: ${it.name}", style = MaterialTheme.typography.bodyLarge)
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { isDebtSelected = true },
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDebtSelected) Color.Red else Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .shadow(1.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Borç",
                        color = if (isDebtSelected) Color.White else Color.Black,
                        style = MaterialTheme.typography.bodyLarge
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
                        .shadow(1.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Alacak",
                        color = if (!isDebtSelected) Color.White else Color.Black,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Açıklama") },
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    selectedOption?.let { item ->
                        debtsViewModel.getPinByPhoneNumber(item.phoneNumber) { pin ->
                            pin?.let {
                                showDialog = true // PIN bulundu, dialog göster
                            } ?: run {
                                // PIN bulunamadı, doğrudan kaydet
                                if (isDebtSelected) {

                                    debtsViewModel.updateCreditAndDebt(
                                        creditId = item.creditId,
                                        phoneNumber=item.phoneNumber,
                                        newDebtAmount = debtAmount.toDoubleOrNull() ?: 0.0,
                                        newCreditAmount = creditAmount.toDoubleOrNull() ?: 0.0,
                                        newDescription = description
                                    )
                                    navController.navigate("home")
                                } else {

                                    debtsViewModel.updateCreditAndDebt(
                                        creditId = item.creditId,
                                        phoneNumber=item.phoneNumber,
                                        newDebtAmount = debtAmount.toDoubleOrNull() ?: 0.0,
                                        newCreditAmount = creditAmount.toDoubleOrNull() ?: 0.0,
                                        newDescription = description
                                    )
                                    navController.navigate("home")
                                }
                            }
                        }


                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Güncelle")
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
                                selectedOption?.let { item ->
                                debtsViewModel.getPinByPhoneNumber(item.phoneNumber) { pin ->
                                    pin?.let {
                                        if (pin == pin1) {
                                            showDialog = false
                                            if (isDebtSelected) {

                                                debtsViewModel.updateCreditAndDebt(
                                                    creditId = item.creditId,
                                                    phoneNumber=item.phoneNumber,
                                                    newDebtAmount = debtAmount.toDoubleOrNull() ?: 0.0,
                                                    newCreditAmount = creditAmount.toDoubleOrNull() ?: 0.0,
                                                    newDescription = description
                                                )

                                                navController.navigate("home")
                                            } else {

                                                debtsViewModel.updateCreditAndDebt(
                                                    creditId = item.creditId,
                                                    phoneNumber=item.phoneNumber,
                                                    newDebtAmount = debtAmount.toDoubleOrNull() ?: 0.0,
                                                    newCreditAmount = creditAmount.toDoubleOrNull() ?: 0.0,
                                                    newDescription = description
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
    }
}
