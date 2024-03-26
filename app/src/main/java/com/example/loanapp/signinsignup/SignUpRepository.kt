package com.example.loanapp.signinsignup

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpRepository {
    fun  registerUser(email : String, password : String):Task<AuthResult>{
        return  Firebase.auth.createUserWithEmailAndPassword(email,password)
    }
}