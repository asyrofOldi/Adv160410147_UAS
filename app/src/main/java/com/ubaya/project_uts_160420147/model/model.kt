package com.ubaya.project_uts_160420147.model

import android.util.Log
import java.util.Date

data class Game(
    val gameID: Int,
    val title: String,
    val developer: String,
    val genre: String,
    val description: String,
    val imageURL: String,
    val releaseDate: Date,
    val news: News
)

data class News (
    val title: String,
    val author: String,
    val imageURL: String,
    val pages: List<String>
)

data class userData(
    val username: String,
    val firstName: String,
    val lastName: String,
    val password: String  // Note: Be cautious with logging passwords.
) {
    fun printInfo() {
        Log.d("UserModel", "User: $username, Name: $firstName $lastName")
    }
}


