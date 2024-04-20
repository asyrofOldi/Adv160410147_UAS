package com.ubaya.project_uts_160420147.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.ubaya.project_uts_160420147.model.Game

class GameListModel(application: Application) : AndroidViewModel(application) {
    val gameLD = MutableLiveData<List<Game>>()
    val gameLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    var dataFetched = false  // Flag to track if data has been fetched

    private val TAG = "volleyTag"
    private var queue: RequestQueue? = Volley.newRequestQueue(application)

    fun refresh() {
        if (!dataFetched || gameLD.value.isNullOrEmpty()) {
            gameLoadErrorLD.value = false
            loadingLD.value = true
            Log.d("CekMasuk", "masukvolley")
            val url = "http://192.168.1.80/anmp_uts/game.json"
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    try {
                        val gson = Gson()
                        val game = gson.fromJson(response, Array<Game>::class.java).toList()
                        gameLD.value = game
                        gameLoadErrorLD.value = false
                        loadingLD.value = false
                        dataFetched = true  // Set flag to true as data is now fetched
                        Log.d("showVolley", game.toString())
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing JSON: ", e)
                        gameLoadErrorLD.value = true
                        loadingLD.value = false
                    }
                },
                { error ->
                    Log.e(TAG, "Volley error: $error")
                    gameLoadErrorLD.value = true
                    loadingLD.value = false
                }
            )
            stringRequest.tag = TAG
            queue?.add(stringRequest)
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}
