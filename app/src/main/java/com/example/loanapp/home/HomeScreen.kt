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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.loanapp.ui.theme.LoanAppTheme

@Composable
fun HomeScreen(){
        Surface(
            color = Color.White,
            modifier = Modifier.fillMaxSize()

        ) {
            Column {


            Row(
                modifier = Modifier.padding(all = 20.dp),
                verticalAlignment = Alignment.Top

            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Merhaba,",
                        color = Color.Black,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "  Mustafa",
                        color = Color.Black,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profil",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(55.dp),
                )

            }
                Spacer(modifier = Modifier.width(16.dp))
                CardScreen()
        }

        }
}

@Composable
fun CardScreen(){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),

        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
            .height(190.dp)
            .shadow(spotColor = Color.Black, elevation = 45.dp)
        ,

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "2250 $",
                fontSize = 35.sp,
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
                    text = "Gelir",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                     modifier = Modifier
                     .padding(start = 22.dp),
                )
                Text(
                    text = "2500 $",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(start = 20.dp),
                )
            }
            Column {
                Text(
                    text = "Gider",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(end = 30.dp),
                )
                Text(
                    text = "250 $",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreeenPreview() {
    LoanAppTheme {
        //HomeScreen()
    }
}
