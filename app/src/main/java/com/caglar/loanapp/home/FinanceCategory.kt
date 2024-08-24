package com.caglar.loanapp.home

data class FinanceCategory(
    val id: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val iconResId: Int = 0,
    val type: String = "",  // "income" veya "expense"
    val timestamp: Long = 0L
){
    constructor() : this("","",0.0 ,"", 0,"", 0)
}