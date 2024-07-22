package com.example.loanapp.signinsignup

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
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
    private val _signInError = MutableLiveData<String?>()
    val signInError: LiveData<String?> get() = _signInError

    fun signInUser(email:String,password:String, navController: NavController, context: Context){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    _signInResult.value=true
                    _currentUserEmail.value = email // Kullanıcı oturum açtığında email'i güncelle
                    saveUserToSharedPreferencess(email, password, context)
                    navController.navigate("home")

                    Log.d(TAG, "SignInViewModel: Kullanıcı oturum açtı. Email: $email")
                }else{
                    _signInError.value = "Üzgünüz, e-posta veya şifren yanlış."
                    _signInResult.value = false
                    Log.e(TAG, "Giriş başarısız", task.exception)
                }
            }
    }

    private fun saveUserToSharedPreferencess(email: String, password: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences("LoanAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }




}
