package com.caglar.loanapp.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(navController: NavHostController, viewModel: FinanceViewModel = viewModel()) {
    val expenseCategories = listOf("Kira", "Market", "Alışveriş", "Ulaşım", "Eğlence", "Diğer")
    var selectedExpenseCategory by remember { mutableStateOf(expenseCategories[0]) }
    var expanded by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }



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
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Gelir Bilgileri",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            )

            Box {
                Text(
                    text = selectedExpenseCategory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = true })
                        .background(Color.White)
                        .padding(5.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White) // Menü arka plan rengini beyaz yapın
                ) {
                    expenseCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category, color = Color.Black) },
                            onClick = {
                                selectedExpenseCategory = category
                                expanded = false
                            },
                            modifier = Modifier
                                .background(Color.White) // Menü öğeleri arka plan rengini beyaz yapın
                        )
                    }
                }
            }

            OutlinedTextField(
                value = amount,
                onValueChange = { newValue ->
                    // Sadece rakam karakterlerini kabul et
                    if (newValue.all { it.isDigit() }) {
                        amount = newValue
                    }
                },
                label = { Text("Tutar", color = Color.Black) },
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
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Button(
                onClick = {
                    val category = selectedExpenseCategory
                    val amountValue = amount.toDoubleOrNull() ?: 0.0
                    val desc = description

                    // Kaydetme işlemini burada çağırıyoruz
                    viewModel.saveFinance(category, amountValue, desc, "expense")
                    navController.navigate("home")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Tamamla",color = Color.White)
            }
        }
    }
}
}
