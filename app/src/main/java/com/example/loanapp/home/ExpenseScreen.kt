package com.example.loanapp.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(navController: NavHostController, viewModel: FinanceViewModel = viewModel()) {
    val expenseCategories = listOf("Kira", "Market", "Alışveriş", "Ulaşım","Eğlence","Diğer")
    var selectedExpenseCategory by remember { mutableStateOf(expenseCategories[0]) }
    var expanded by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

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
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )

            Box {
                Text(
                    text = selectedExpenseCategory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = true })
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(5.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    expenseCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedExpenseCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Tutar") },
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
                Text("Tamamla")
            }
        }
    }
}
