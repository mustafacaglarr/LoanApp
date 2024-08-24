package com.caglar.loanapp.signinsignup

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class SplashScreenViewModel : ViewModel() {
    private val _navigateTo = MutableLiveData<String?>()
    val navigateTo: LiveData<String?> get() = _navigateTo

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private fun getUserFromSharedPreferences(context: Context): Pair<String?, String?> {
        val sharedPreferences = context.getSharedPreferences("LoanAppPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        return Pair(email, password)
    }
    fun checkUserAuthentication(context: Context) {
        viewModelScope.launch {
            val (email, password) = getUserFromSharedPreferences(context)
            println(email+password)

            if (email != null && password != null) {
                try {
                    // Firebase Auth kullanarak giriş yapma
                    val authResult = FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .await()

                    // Giriş başarılıysa
                    _navigateTo.postValue("home") // veya uygun bir rota
                    updatePinOnLogin()
                } catch (e: Exception) {
                    // Giriş başarısızsa
                    Log.w("SplashScreenViewModel", "User login failed", e)
                    _navigateTo.postValue("signin")
                }
            } else {
                // Kullanıcı bilgileri yoksa
                _navigateTo.postValue("signin")
            }
        }
    }
    private fun generateRandomPin(): String {
        val pin = Random.nextInt(100000, 999999) // 6 haneli rastgele bir sayı oluştur
        return pin.toString()
    }
    fun updatePinOnLogin() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val usersRef = firebaseDatabase.reference.child("users")

        val newPin = generateRandomPin()
        usersRef.child(userId).child("pin").setValue(newPin)
            .addOnSuccessListener {
                Log.d("SignUpViewModel", "PIN updated successfully")
            }
            .addOnFailureListener { e ->
                Log.w("SignUpViewModel", "Error updating PIN", e)
            }
    }

}
