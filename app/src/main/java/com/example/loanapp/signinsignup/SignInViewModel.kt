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
    fun signInUser(email:String,password:String, navController: NavController){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    _signInResult.value=true
                    navController.navigate("home")

                    println("başarılı")
                }else{
                    _signInResult.value = false
                    Log.e(TAG, "Giriş başarısız", task.exception)
                }

            }
    }

    //fun fetchUserId(callback: (String?) -> Unit) {
    //    signInResult.observeForever { signInSuccess ->
    //        if (signInSuccess) {
    //            val user = FirebaseAuth.getInstance().currentUser
    //            val uid = user?.uid
    //            callback(uid)
    //        }
    //    }
    //}
}
