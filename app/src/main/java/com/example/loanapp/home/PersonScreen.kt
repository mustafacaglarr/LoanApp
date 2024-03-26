package com.example.loanapp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bumptech.glide.Glide.init
import com.example.loanapp.R
import com.example.loanapp.ui.theme.LoanAppTheme

@Composable
fun PersonScreen(debtsViewModel: DebtsViewModel) {


    //val debtsState by debtsViewModel.debts.observeAsState()
    //val debts = debtsState ?: emptyList()
    //val creditsState by debtsViewModel.credits.observeAsState()
    //val credits = creditsState ?: emptyList()
    //val creditsState by debtsViewModel.credits.observeAsState()
    //val credits = creditsState ?: emptyList()
    //val debtsAndCredits: List<Any> = (debts + credits).toList()
    val debtsandCreditsState by debtsViewModel.debtsandCredits.observeAsState()
    val debtsandCredits = debtsandCreditsState ?: emptyList()
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Kişiler",
            modifier = Modifier.padding(16.dp)
                .align(Alignment.CenterHorizontally),
            style = TextStyle(
                fontSize = 39.sp,
                fontWeight = FontWeight.Bold,
            ),

            )

        var searchText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Ara") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        )

            MyLazyColumn(debtsandCredits)

    }
}

@Composable
fun MyLazyColumn(creditAndDebt: List<CreditAndDebt>) {
    LazyColumn {
        items(creditAndDebt) { creditAndDebt ->
            Box(
                modifier = Modifier
                    .clickable { } // Tıklama işlemi için
                    .padding(16.dp)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Gölgeli kart
                Card(
                    shape = RoundedCornerShape(16.dp), // Kenarları eğmek için RoundedCornerShape kullanılır
                    elevation = 10.dp, // Gölgelendirme
                    backgroundColor = Color.Green, // Kart arka plan rengi
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = if (creditAndDebt.debtAmount > creditAndDebt.creditAmount) Color.Red else Color.Green
                        )
                ) {
                    // Kart içeriği
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Adı: ${creditAndDebt.name}",
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        if (creditAndDebt.debtAmount > creditAndDebt.creditAmount) {
                            Text(
                                text = "Borç Miktarı: ${creditAndDebt.debtAmount}",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        } else {
                            Text(
                                text = "Alacak Miktarı: ${creditAndDebt.creditAmount}",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PersonScreenPreview() {
    LoanAppTheme {
        //PersonScreen()
    }
}
