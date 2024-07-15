package com.example.loanapp.signinsignup

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {
    private val _signInResult = MutableLiveData<Boolean>()
    val signInResult : LiveData<Boolean> = _signInResult

    private val _currentUserEmail = MutableLiveData<String?>()
    val currentUserEmail: LiveData<String?> = _currentUserEmail

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

}
