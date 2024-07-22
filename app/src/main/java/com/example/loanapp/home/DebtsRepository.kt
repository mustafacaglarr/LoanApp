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

        // Mevcut kullanıcının creditsanddebts düğümüne veri ekleme
        val currentUserCreditRef = database.reference.child("users").child(uid).child("creditsanddebts")
        val creditId = currentUserCreditRef.push().key ?: return
        val credit = CreditAndDebt(creditId, name, phoneNumber, debtAmount, creditAmount, description)
        currentUserCreditRef.child(creditId).setValue(credit)
            .addOnSuccessListener {
                println("Mevcut kullanıcının alacak başarıyla kaydedildi")
            }
            .addOnFailureListener { e ->
                println("Mevcut kullanıcının alacak kaydedilirken bir hata oluştu: $e")
            }

        // Telefon numarasına sahip kullanıcının UID'sini al ve onun creditsanddebts düğümüne veri ekle
        getUserUidByPhoneNumber(phoneNumber) { otherUserUid ->
            otherUserUid?.let { otherUid ->
                val otherUserCreditRef = database.reference.child("users").child(otherUid).child("creditsanddebts")
                val otherUserCredit = CreditAndDebt(creditId, name, phoneNumber, debtAmount, creditAmount, description)
                otherUserCreditRef.child(creditId).setValue(otherUserCredit)
                    .addOnSuccessListener {
                        println("Diğer kullanıcının alacak başarıyla kaydedildi")
                    }
                    .addOnFailureListener { e ->
                        println("Diğer kullanıcının alacak kaydedilirken bir hata oluştu: $e")
                    }
            } ?: run {
                println("Telefon numarasına sahip kullanıcı bulunamadı")
            }
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
    fun updateCreditAndDebt(creditId: String, phoneNumber: String, newDebtAmount: Double, newCreditAmount: Double, newDescription: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: return

        // Mevcut kullanıcının creditsanddebts düğümünde güncelleme
        val currentUserCreditRef = database.reference.child("users").child(uid).child("creditsanddebts").child(creditId)
        val updates = mapOf(
            "debtAmount" to newDebtAmount,
            "creditAmount" to newCreditAmount,
            "description" to newDescription
        )

        currentUserCreditRef.updateChildren(updates)
            .addOnSuccessListener {
                println("Mevcut kullanıcının alacak başarıyla güncellendi")
            }
            .addOnFailureListener { e ->
                println("Mevcut kullanıcının alacak güncellenirken bir hata oluştu: $e")
            }

        // Telefon numarasına sahip kullanıcının UID'sini al ve onun creditsanddebts düğümünde güncelleme yap
        getUserUidByPhoneNumber(phoneNumber) { otherUserUid ->
            otherUserUid?.let { otherUid ->
                val otherUserCreditRef = database.reference.child("users").child(otherUid).child("creditsanddebts").child(creditId)
                otherUserCreditRef.updateChildren(updates)
                    .addOnSuccessListener {
                        println("Diğer kullanıcının alacak başarıyla güncellendi")
                    }
                    .addOnFailureListener { e ->
                        println("Diğer kullanıcının alacak güncellenirken bir hata oluştu: $e")
                    }
            } ?: run {
                println("Telefon numarasına sahip kullanıcı bulunamadı")
            }
        }
    }

    fun getPinByPhoneNumber(phoneNumber: String, callback: (String?) -> Unit) {
        val usersRef = database.reference.child("users")

        // Telefon numarası ile sorgu yap
        usersRef.orderByChild("phoneNum").equalTo(phoneNumber).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val pin = userSnapshot.child("pin").getValue(String::class.java)
                            callback(pin)
                            return
                        }
                    } else {
                        callback(null) // Kullanıcı bulunamadı
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DebtsRepository", "Error retrieving user data", error.toException())
                    callback(null)
                }
            }
        )
    }

    private fun getUserUidByPhoneNumber(phoneNumber: String, callback: (String?) -> Unit) {
        val usersRef = database.reference.child("users")

        usersRef.orderByChild("phoneNum").equalTo(phoneNumber).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val uid = userSnapshot.key
                            callback(uid)
                            return
                        }
                    } else {
                        callback(null) // Kullanıcı bulunamadı
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DebtsRepository", "Error retrieving user UID", error.toException())
                    callback(null)
                }
            }
        )
    }


}