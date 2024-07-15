package com.example.loanapp.signinsignup

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpViewModel:ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Kullanıcı başarıyla oluşturuldu
                    saveUserDataToDatabase(email)
                } else {
                    // Kullanıcı oluşturma başarısız oldu
                    Log.w("SignUpViewModel", "User registration failed", task.exception)
                }
            }
    }
    private fun saveUserDataToDatabase(email: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val usersRef = firebaseDatabase.reference.child("users")

        val user = User(userId, email) // Assuming User is a data class or model

        usersRef.child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("SignUpViewModel", "User data saved to database successfully")
            }
            .addOnFailureListener { e ->
                Log.w("SignUpViewModel", "Error saving user data to database", e)
            }
    }
}

