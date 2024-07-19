package com.example.loanapp.signinsignup

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class SignInViewModel : ViewModel() {
    private val _signInResult = MutableLiveData<Boolean>()
    val signInResult : LiveData<Boolean> = _signInResult

    private val _currentUserEmail = MutableLiveData<String?>()
    val currentUserEmail: LiveData<String?> = _currentUserEmail

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    fun signInUser(email:String,password:String, navController: NavController){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    _signInResult.value=true
                    _currentUserEmail.value = email // Kullanıcı oturum açtığında email'i güncelle

                    navController.navigate("home")

                    Log.d(TAG, "SignInViewModel: Kullanıcı oturum açtı. Email: $email")
                }else{
                    _signInResult.value = false
                    Log.e(TAG, "Giriş başarısız", task.exception)
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
