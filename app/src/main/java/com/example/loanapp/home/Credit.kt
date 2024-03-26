package com.example.loanapp.home

data class Credit(
    val name: String,
    val phoneNumber: String,
    val creditAmount: Double,
    val description: String
){
    constructor() : this("", "", 0.0, "")
}
