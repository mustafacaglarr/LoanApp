package com.example.loanapp.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loanapp.ui.theme.LoanAppTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PersonScreen(debtsViewModel: DebtsViewModel, navController: NavHostController) {
    val debtsandCreditsState by debtsViewModel.debtsandCredits.observeAsState()
    val debtsandCredits = debtsandCreditsState ?: emptyList()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid ?: return
    var showDialog by remember { mutableStateOf(false) }
    var pin by remember { mutableStateOf<String?>(null) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Çıkış Yap") },
            text = { Text("Çıkış yapmak istediğinizden emin misiniz?") },
            confirmButton = {
                Text(
                    text = "Evet",
                    modifier = Modifier
                        .clickable {
                            showDialog = false
                            navController.navigate("signin")
                        }
                        .padding(16.dp),
                    color = Color.Red
                )
            },
            dismissButton = {
                Text(
                    text = "Hayır",
                    modifier = Modifier
                        .clickable {
                            showDialog = false
                        }
                        .padding(16.dp),
                    color = Color.Gray
                )
            }
        )
    }

    LaunchedEffect(Unit) {
        debtsViewModel.getPin(uid) { pinCurrentUser ->
            pin = pinCurrentUser
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Kişiler",
                style = TextStyle(
                    fontSize = 39.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Çıkış Yap",
                    tint = Color.Black,
                )
            }
        }

        pin?.let {
            Text(
                text = "Pin Numaranız: $it",
                style = TextStyle(
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } ?: Text(
            text = "Yükleniyor...",
            style = TextStyle(
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
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
                            .clickable { }
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Adı: ${creditAndDebt.name}",
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = "Borç Miktarı: ${creditAndDebt.debtAmount}",
                            style = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

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

@Preview(showBackground = true)
@Composable
fun PersonScreenPreview() {
    LoanAppTheme {
        // PersonScreen()
    }
}
