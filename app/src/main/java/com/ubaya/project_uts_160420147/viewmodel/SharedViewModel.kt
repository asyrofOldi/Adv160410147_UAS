package com.ubaya.project_uts_160420147.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ubaya.project_uts_160420147.model.Game

class SharedViewModel : ViewModel() {
    val lastVisitedGame = MutableLiveData<Game?>()

    fun setLastVisitedGame(game: Game) {
        lastVisitedGame.value = game
    }
}
