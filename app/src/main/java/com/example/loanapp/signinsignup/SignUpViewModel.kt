package com.example.loanapp.signinsignup
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class SignUpViewModel: ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun registerUser(email: String, password: String,phoneNum:String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Kullanıcı başarıyla oluşturuldu
                    val pin = generateRandomPin()
                    saveUserDataToDatabase(email, pin,phoneNum)
                } else {
                    // Kullanıcı oluşturma başarısız oldu
                    Log.w("SignUpViewModel", "User registration failed", task.exception)
                }
            }
    }

    private fun saveUserDataToDatabase(email: String, pin: String,phoneNum:String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val usersRef = firebaseDatabase.reference.child("users")

        val user = User(userId, email, pin,phoneNum) // Kullanıcı modeline PIN ekledik

        usersRef.child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("SignUpViewModel", "User data saved to database successfully")
            }
            .addOnFailureListener { e ->
                Log.w("SignUpViewModel", "Error saving user data to database", e)
            }
    }

    private fun generateRandomPin(): String {
        val pin = Random.nextInt(100000, 999999) // 6 haneli rastgele bir sayı oluştur
        return pin.toString()
    }


}

