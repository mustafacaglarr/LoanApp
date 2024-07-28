package com.example.loanapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class DebtsViewModel : ViewModel() {
    private val repository = DebtsRepository()
    private val _debtsandCredits = MutableLiveData<List<CreditAndDebt>>()

    val debtsandCredits: LiveData<List<CreditAndDebt>> get() = _debtsandCredits

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val user = auth.currentUser
        if (user != null) {
            fetchCreditandDebts(user)
        } else {
            _debtsandCredits.value = emptyList() // User is not logged in, clear the data
        }
    }


    init {
        auth.addAuthStateListener(authStateListener)
    }

    private fun fetchCreditandDebts(user: FirebaseUser) {
        val creditsLiveData = repository.getDebtsandCredits(user.uid)
        creditsLiveData.observeForever { debtsAndCreditsList ->
            _debtsandCredits.value = debtsAndCreditsList
        }
    }

    fun saveCreditandDebt(name: String, phoneNumber: String, debtAmount: Double, creditAmount: Double, description: String) {
        repository.saveCreditandDebt(name, phoneNumber, debtAmount, creditAmount, description)

    }

    fun updateCreditAndDebt(creditId: String,phoneNumber: String, newDebtAmount: Double, newCreditAmount: Double, newDescription: String) {
        repository.updateCreditAndDebt(creditId,phoneNumber ,newDebtAmount, newCreditAmount, newDescription)
        auth.currentUser?.let { fetchCreditandDebts(it) } // Fetch the latest data after updating
    }

    fun getPinByPhoneNumber(phoneNumber: String, callback: (String?) -> Unit) {
        repository.getPinByPhoneNumber(phoneNumber, callback)
    }
    fun getPin(uid: String, callback: (String?) -> Unit) {
        repository.getPin(uid, callback)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }



}
