package com.example.loanapp.home

import android.content.ContentValues.TAG
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
        val credit = CreditAndDebt(creditId,name, phoneNumber,debtAmount, creditAmount,description)
        creditRef.child(creditId).setValue(credit)
            .addOnSuccessListener {
                println("Alacak başarıyla kaydedildi")
            }
            .addOnFailureListener { e ->
                println("Alacak kaydedilirken bir hata oluştu: $e")
            }
    }
    fun getDebtsandCredits(uid: String): LiveData<List<CreditAndDebt>>  {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return MutableLiveData<List<CreditAndDebt>>().apply { value = emptyList() }

        val debtsRef = database.reference.child("users").child(uid).child("creditsanddebts")
        val creditAndDebtLiveData = MutableLiveData<List<CreditAndDebt>>()

        debtsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val creditAndDebtlist = mutableListOf<CreditAndDebt>()
                dataSnapshot.children.forEach { snapshot ->
                    val creditAndDebt = snapshot.getValue(CreditAndDebt::class.java)
                    creditAndDebt?.let { creditAndDebtlist.add(it) }
                }
                creditAndDebtLiveData.value = creditAndDebtlist
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle cancellation if needed
                Log.e(TAG, "getDebtsandCredits:onCancelled", databaseError.toException())
            }
        })

        return creditAndDebtLiveData
    }
    // Yeni fonksiyon: Borç ve kredi bilgilerini güncelle
    fun updateCreditAndDebt(creditId: String, newDebtAmount: Double, newCreditAmount: Double, newDescription: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return

        val creditRef = database.reference.child("users").child(uid).child("creditsanddebts").child(creditId)

        val updates = mapOf(
            "debtAmount" to newDebtAmount,
            "creditAmount" to newCreditAmount,
            "description" to newDescription
        )

        creditRef.updateChildren(updates)
            .addOnSuccessListener {
                println("Alacak başarıyla güncellendi")
            }
            .addOnFailureListener { e ->
                println("Alacak güncellenirken bir hata oluştu: $e")
            }
    }



}