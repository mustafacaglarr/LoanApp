package com.example.loanapp.home

data class Debt(
    val name: String,
    val phoneNumber: String,
    val debtAmount: Double,
    val description: String
){
    constructor() : this("", "", 0.0, "")
}
