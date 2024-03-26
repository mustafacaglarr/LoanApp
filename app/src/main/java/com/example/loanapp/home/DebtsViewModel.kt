package com.example.loanapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DebtsViewModel : ViewModel() {
    private val repository = DebtsRepository()
    private val _debtsandCredits = MutableLiveData<List<CreditAndDebt>>()
    val debtsandCredits: LiveData<List<CreditAndDebt>> get() = _debtsandCredits

    init {
        fetchCreditandDebts()
    }
    private fun fetchCreditandDebts() {
        val creditsLiveData = repository.getDebtsandCredits()
        creditsLiveData.observeForever { DebtsandCreditsList ->
            _debtsandCredits.value = DebtsandCreditsList
        }
    }

    fun saveCreditandDebt(name: String, phoneNumber: String,debtAmount: Double, creditAmount: Double, description: String) {
        repository.saveCreditandDebt(name, phoneNumber,debtAmount, creditAmount, description)

    }

}