package com.example.loanapp.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DebtsRepository {
    private val database = Firebase.database
    fun saveCreditandDebt(name: String, phoneNumber: String,debtAmount: Double, creditAmount: Double, description: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return

        val creditRef = database.reference.child("users").child(uid).child("creditsanddebts")

        val creditId = creditRef.push().key ?: return
        val credit = CreditAndDebt(name, phoneNumber,debtAmount, creditAmount,description)
        creditRef.child(creditId).setValue(credit)
            .addOnSuccessListener {
                println("Alacak başarıyla kaydedildi")
            }
            .addOnFailureListener { e ->
                println("Alacak kaydedilirken bir hata oluştu: $e")
            }
    }
    fun getDebtsandCredits(): LiveData<List<CreditAndDebt>> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return MutableLiveData<List<CreditAndDebt>>().apply { value = emptyList() }

        val debtsRef = database.reference.child("users").child(uid).child("creditsanddebts")
        val creditAndDebtLiveData = MutableLiveData<List<CreditAndDebt>>()

        debtsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                println("veri geldi")
                val creditAndDebtlist = mutableListOf<CreditAndDebt>()
                dataSnapshot.children.forEach { snapshot ->
                    val creditAndDebt = snapshot.getValue(CreditAndDebt::class.java)
                    creditAndDebt?.let { creditAndDebtlist.add(it) }
                }
                creditAndDebtLiveData.value = creditAndDebtlist // Update LiveData with the fetched debts
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle cancellation if needed
                println("onCancelled hata")
            }
        })

        return creditAndDebtLiveData
    }

}