package com.caglar.loanapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FinanceViewModel : ViewModel() {
    private val repository = FinanceRepository()
    private val _financeCategory = MutableLiveData<List<FinanceCategory>>()

    val financeCategory: LiveData<List<FinanceCategory>> get() = _financeCategory

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val user = auth.currentUser
        if (user != null) {
            fetchFinanceCategory(user)
        } else {
            _financeCategory.value = emptyList() // User is not logged in, clear the data
        }
    }
    init {
        auth.addAuthStateListener(authStateListener)
    }
    private fun fetchFinanceCategory(user: FirebaseUser) {
        val creditsLiveData = repository.getFinanceCategories(user.uid)
        creditsLiveData.observeForever { debtsAndCreditsList ->
            _financeCategory.value = debtsAndCreditsList
        }
    }

    fun saveFinance(category: String, amount: Double, description: String, type: String) {
        repository.saveFinance(category, amount, description, type)
    }
}