package com.example.loanapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DebtsViewModel : ViewModel() {
    private val repository = DebtsRepository()
    private val _debts = MutableLiveData<List<Debt>>()
    val debts: LiveData<List<Debt>> get() = _debts
    private val _credits = MutableLiveData<List<Credit>>()
    val credits: LiveData<List<Credit>> get() = _credits
    private val _debtsandCredits = MutableLiveData<List<CreditAndDebt>>()
    val debtsandCredits: LiveData<List<CreditAndDebt>> get() = _debtsandCredits

    init {
        fetchCreditandDebts()
        //fetchDebts()
        //fetchCredit()
    }

    private fun fetchDebts() {
        val debtsLiveData = repository.getDebts()
        debtsLiveData.observeForever { debtsList ->
            _debts.value = debtsList
        }
    }
    private fun fetchCredit() {
        val creditsLiveData = repository.getCredits()
        creditsLiveData.observeForever { creditsList ->
            _credits.value = creditsList
        }
    }
    private fun fetchCreditandDebts() {
        val creditsLiveData = repository.getDebtsandCredits()
        creditsLiveData.observeForever { DebtsandCreditsList ->
            _debtsandCredits.value = DebtsandCreditsList
        }
    }


    //fun saveDebt(name: String, phoneNumber: String, debtAmount: Double, description: String) {
    //    repository.saveDebt(name, phoneNumber, debtAmount, description)
    //
    //}
    //fun saveCredit(name: String, phoneNumber: String, creditAmount: Double, description: String) {
    //    repository.saveCredit(name, phoneNumber, creditAmount, description)
    //
    //}

    fun saveCreditandDebt(name: String, phoneNumber: String,debtAmount: Double, creditAmount: Double, description: String) {
        repository.saveCreditandDebt(name, phoneNumber,debtAmount, creditAmount, description)

    }



}