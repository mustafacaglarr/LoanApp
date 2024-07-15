package com.example.loanapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(viewModel: FinanceViewModel = viewModel()) {
    val financeCategories by viewModel.financeCategory.observeAsState(emptyList())

    // Calculate total income and expense
    val totalIncome = financeCategories.filter { it.type == "income" }.sumByDouble { it.amount }
    val totalExpense = financeCategories.filter { it.type == "expense" }.sumByDouble { it.amount }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            CardScreen(totalIncome, totalExpense)

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
            ) {
                items(financeCategories) { financeCategory ->
                    FinanceItem(financeCategory = financeCategory)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun FinanceItem(financeCategory: FinanceCategory) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .shadow(spotColor = Color.Black, elevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material.Icon(
                painter = painterResource(id = financeCategory.iconResId),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(30.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(text = "Category: ${financeCategory.category}")
                Text(text = "Amount: ${financeCategory.amount} $")
                Text(text = "Description: ${financeCategory.description}")
                Text(text = "Type: ${if (financeCategory.type == "income") "Income" else "Expense"}")
            }
        }
    }
}

@Composable
fun CardScreen(totalIncome: Double, totalExpense: Double) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
            .height(190.dp)
            .shadow(spotColor = Color.Black, elevation = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Income: ${totalIncome.toInt()-totalExpense.toInt()} $",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Income",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 22.dp),
                )
                Text(
                    text = "${totalIncome.toInt()} $",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 20.dp),
                )
            }
            Column {
                Text(
                    text = "Expense",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 30.dp),
                )
                Text(
                    text = "${totalExpense.toInt()} $",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}





