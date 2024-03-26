package com.example.loanapp.signinsignup

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel:ViewModel() {
    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> = _authResult

    fun registerUser(email : String, password : String){
        SignUpRepository().registerUser(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Kullanıcı başarıyla oluşturuldu
                    _authResult.value=task.isSuccessful
                    Log.d(ContentValues.TAG, "User registration successful")
                } else {
                    // Kullanıcı oluşturma başarısız oldu
                    Log.w(ContentValues.TAG, "User registration failed", task.exception)
                }
            }
    }
}

