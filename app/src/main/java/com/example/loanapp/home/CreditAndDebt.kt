package com.example.loanapp.home

data class CreditAndDebt(
    val creditId: String,
    val name: String,
    val phoneNumber: String,
    val debtAmount: Double,
    val creditAmount: Double,
    val description: String
){
    constructor() : this("","", "", 0.0,0.0, "")
}

