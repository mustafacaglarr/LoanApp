package com.example.loanapp.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.loanapp.BottomBarScreen

@Composable
fun CustomAlertDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onItemClick: (String) -> Unit,
    navController: NavHostController
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = Modifier
                    .size(400.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White,

                ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text("Lürfen Yapmak İstediğiniz İşlemi Seçiniz")
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AlertCard("Tanımlı Borç / Alacak", onItemClick = {
                            navController.navigate("defineDebts")
                            onDismiss()
                        }, navController = navController)

                        AlertCard("Yeni Borç / Alacak", onItemClick = {
                            navController.navigate(BottomBarScreen.Debts.route)
                            onDismiss()
                        }, navController = navController)


                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AlertCard(text = "Gelir Ekle", onItemClick = {

                        }, navController = navController)

                        AlertCard("Gider Ekle", onItemClick = {

                        }, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun AlertCard(text: String, onItemClick: () -> Unit, navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(20.dp),
        modifier = Modifier
            .size(110.dp)
            .clickable { onItemClick() }
            .background(Color.White),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), // Box ile içeriği tam kaplamasını sağladık
            contentAlignment = Alignment.Center // İçeriği merkeze aldık
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center
            )
        }
    }
}