package com.caglar.loanapp.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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

@Composable
fun HomeScreen(viewModel: FinanceViewModel = viewModel()) {
    val financeCategories by viewModel.financeCategory.observeAsState(emptyList())

    // Calculate total income and expense
    val totalIncome = financeCategories.filter { it.type == "income" }.sumByDouble { it.amount }
    val totalExpense = financeCategories.filter { it.type == "expense" }.sumByDouble { it.amount }

    // Define colors
    val backgroundColor = Color.White
    val textColor = Color.Black // Set a specific color instead of using theme color

    Surface(
        color = backgroundColor,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            CardScreen(totalIncome, totalExpense, textColor)

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
            ) {
                items(financeCategories) { financeCategory ->
                    FinanceItem(financeCategory = financeCategory, textColor = textColor)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
@Composable
fun FinanceItem(financeCategory: FinanceCategory, textColor: Color) {
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
                Text(text = "Kategori: ${financeCategory.category}", color = textColor)
                Text(text = "Miktar: ${financeCategory.amount} TL", color = textColor)
                Text(text = "Açıklama: ${financeCategory.description}", color = textColor)
                Text(text = "Tip: ${if (financeCategory.type == "income") "Gelir" else "Gider"}", color = textColor)
            }
        }
    }
}

@Composable
fun CardScreen(totalIncome: Double, totalExpense: Double, textColor: Color) {
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
                text = "Toplam Gelir: ${totalIncome.toInt() - totalExpense.toInt()} TL",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
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
                    text = "Gelir",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    modifier = Modifier.padding(start = 22.dp)
                )
                Text(
                    text = "${totalIncome.toInt()} TL",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
            Column {
                Text(
                    text = "Gider",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    modifier = Modifier.padding(end = 30.dp)
                )
                Text(
                    text = "${totalExpense.toInt()} TL",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }
        }
    }
}