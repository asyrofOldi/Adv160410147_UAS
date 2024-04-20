package com.ubaya.project_uts_160420147.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    val username = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    init {
        username.value = prefs.getString("username", "")
        firstName.value = prefs.getString("first_name", "")
        lastName.value = prefs.getString("last_name", "")
        password.value = prefs.getString("password", "")
    }

    fun saveUserData(fName: String, lName: String, pwd: String) {
        prefs.edit().apply {
            putString("username", username.value ?: "") // Ensure username is not null
            putString("first_name", fName)
            putString("last_name", lName)
            putString("password", pwd)  // Reconsider saving passwords in SharedPreferences for security reasons.
            apply()
        }
        firstName.value = fName
        lastName.value = lName
        password.value = pwd
    }

    fun clearUserData() {
        prefs.edit().clear().apply()
        username.value = ""
        firstName.value = ""
        lastName.value = ""
        password.value = ""
    }
}



