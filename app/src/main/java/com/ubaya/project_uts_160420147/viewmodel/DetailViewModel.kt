package com.ubaya.project_uts_160420147.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.ubaya.project_uts_160420147.model.Game
import org.json.JSONException

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _gameData = MutableLiveData<Game?>()
    val gameData: LiveData<Game?> = _gameData

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(application.applicationContext)
    }

    fun fetch(gameId: Int) {
        val url = "http://192.168.1.80/anmp_uts/games.php?gameID=$gameId"
        Log.d("DetailViewModel", "Fetching data from URL: $url")
        val gameRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val game = Gson().fromJson(response, Game::class.java)
                    _gameData.postValue(game)
                } catch (e: Exception) {
                    Log.e("JSON Parse Error", "Error parsing game details", e)
                }
            },
            { error ->
                Log.e("API Error", "Error fetching game details: ${error.message}")
            }
        )
        requestQueue.add(gameRequest)
    }

}

