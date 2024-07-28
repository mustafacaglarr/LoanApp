package com.example.loanapp.signinsignup
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.random.Random

class SignUpViewModel: ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    // Hata mesajları ve başarı durumları için LiveData
    val registrationStatus = MutableLiveData<String>()
    val isRegistrationSuccessful = MutableLiveData<Boolean>()

    fun registerUser(email: String, password: String, phoneNum: String, context: Context) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val pin = generateRandomPin()
                    saveUserDataToDatabase(email, pin, phoneNum, context)
                    saveUserToSharedPreferences(email, password, context)
                } else {
                    Log.w("SignUpViewModel", "User registration failed", task.exception)
                    registrationStatus.value = "Bu e-posta adresine ait kullanıcı zaten var!"
                    isRegistrationSuccessful.value = false
                }
            }
    }

    private fun saveUserDataToDatabase(email: String, pin: String, phoneNum: String, context: Context) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val usersRef = firebaseDatabase.reference.child("users")

        val user = User(userId, email, pin, phoneNum)

        usersRef.child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("SignUpViewModel", "User data saved to database successfully")
                saveFcmTokenToDatabase(userId)
            }
            .addOnFailureListener { e ->
                Log.w("SignUpViewModel", "Error saving user data to database", e)
                registrationStatus.value = "Kullanıcı verileri kaydedilirken hata oluştu!"
                isRegistrationSuccessful.value = false
            }
    }

    private fun saveFcmTokenToDatabase(userId: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("SignUpViewModel", "Fetching FCM registration token failed", task.exception)
                registrationStatus.value = "FCM token alınamadı!"
                isRegistrationSuccessful.value = false
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            val userTokenRef = firebaseDatabase.reference.child("users").child(userId).child("fcmToken")

            userTokenRef.setValue(token)
                .addOnSuccessListener {
                    Log.d("SignUpViewModel", "FCM token saved to database successfully")
                    registrationStatus.value = "Kayıt başarılı!"
                    isRegistrationSuccessful.value = true
                }
                .addOnFailureListener { e ->
                    Log.w("SignUpViewModel", "Error saving FCM token to database", e)
                    registrationStatus.value = "FCM token kaydedilirken hata oluştu!"
                    isRegistrationSuccessful.value = false
                }
        }
    }

    private fun generateRandomPin(): String {
        val pin = Random.nextInt(100000, 999999) // 6 haneli rastgele bir sayı oluştur
        return pin.toString()
    }

    private fun saveUserToSharedPreferences(email: String, password: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences("LoanAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }
}
