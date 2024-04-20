package com.ubaya.project_uts_160420147.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.project_uts_160420147.databinding.GamesCardItemsBinding
import com.ubaya.project_uts_160420147.model.Game
import com.ubaya.project_uts_160420147.util.loadImage

class GameListAdapter(private var games: List<Game>) : RecyclerView.Adapter<GameListAdapter.GameViewHolder>() {

    class GameViewHolder(val binding: GamesCardItemsBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateGameList(newGames: List<Game>) {
        games = newGames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = GamesCardItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        with(holder.binding) {
            textViewTitle.text = game.title
            textViewAuthor.text = game.developer
            textViewGenre.text = game.genre
            textViewShortDescription.text = game.description
            imageViewCard.loadImage(game.imageURL, holder.binding.progressBar)

            buttonDetail.setOnClickListener {
                Log.d("GameListAdapter", "Navigating with game ID: ${game.gameID}")
                val action = GameListFragmentDirections.actionGameListFragmentToGameDetailFragment(game.gameID)
                it.findNavController().navigate(action)
            }

        }
    }


    override fun getItemCount(): Int = games.size
}
