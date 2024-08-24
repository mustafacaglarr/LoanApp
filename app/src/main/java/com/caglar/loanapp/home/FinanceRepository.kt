package com.caglar.loanapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.caglar.loanapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FinanceRepository {
    private val database = FirebaseDatabase.getInstance()

    fun saveFinance(category: String, amount: Double, description: String, type: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return

        val financeRef = database.reference.child("users").child(uid).child("finance")

        val financeId = financeRef.push().key ?: return
        val iconResId = determineIconResId(category)
        val timestamp = System.currentTimeMillis()
        val finance = FinanceCategory(financeId, category, amount, description, iconResId, type, timestamp)
        financeRef.child(financeId).setValue(finance)
            .addOnSuccessListener {
                println("Veri başarıyla kaydedildi")
            }
            .addOnFailureListener { e ->
                println("Veri kaydedilirken bir hata oluştu: $e")
            }
    }

    fun getFinanceCategories(uid: String): LiveData<List<FinanceCategory>> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return MutableLiveData<List<FinanceCategory>>().apply { value = emptyList() }

        val financeCategoryLiveData = MutableLiveData<List<FinanceCategory>>()
        val financeRef = database.reference.child("users").child(userId).child("finance")

        financeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val financeCategoryList = mutableListOf<FinanceCategory>()
                dataSnapshot.children.forEach { snapshot ->
                    val financeCategory = snapshot.getValue(FinanceCategory::class.java)
                    financeCategory?.let { financeCategoryList.add(it) }
                }
                // Sort by timestamp in descending order (newest first)
                financeCategoryList.sortByDescending { it.timestamp }
                financeCategoryLiveData.value = financeCategoryList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle cancellation if needed
                // Log.e(ContentValues.TAG, "getFinanceCategories:onCancelled", databaseError.toException())
            }
        })

        return financeCategoryLiveData
    }


    private fun determineIconResId(category: String): Int {
        return when (category) {
            "Maaş" -> R.drawable.money
            "Kira Getirisi" -> R.drawable.building
            "Yatırım" -> R.drawable.profit
            "Kira" -> R.drawable.contract
            "Market" -> R.drawable.shop
            "Alışveriş" -> R.drawable.bag
            "Ulaşım" -> R.drawable.bus
            "Eğlence" -> R.drawable.`fun`
            else -> R.drawable.money  // Default icon
        }
    }
}