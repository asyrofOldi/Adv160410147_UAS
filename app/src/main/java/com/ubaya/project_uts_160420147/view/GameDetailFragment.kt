package com.ubaya.project_uts_160420147.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.ubaya.project_uts_160420147.databinding.FragmentGameDetailBinding
import com.ubaya.project_uts_160420147.model.Game
import com.ubaya.project_uts_160420147.viewmodel.DetailViewModel
import com.ubaya.project_uts_160420147.viewmodel.SharedViewModel
import java.util.logging.Logger

class GameDetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentGameDetailBinding
    private var currentPageIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: GameDetailFragmentArgs by navArgs()
        val gameId = args.gameId
        viewModel.fetch(gameId)

        val adapter = NewsAdapter()
        binding.recyclerViewNews.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewNews.adapter = adapter

        viewModel.gameData.observe(viewLifecycleOwner, Observer { game ->
            game?.let {
                Log.d("GameDetailFragment", "Game data received: ${it.title}")
                sharedViewModel.setLastVisitedGame(it)
                updateUI(it)
                updatePage(adapter)
            }
        })

        sharedViewModel.lastVisitedGame.observe(viewLifecycleOwner, Observer { game ->
            game?.let {
                updateUI(it)
                updatePages(adapter, it.news.pages)

                if (it.news.pages.size > 1) {
                    binding.buttonNext.visibility = View.VISIBLE
                    binding.buttonPrev.visibility = View.VISIBLE
                } else {
                    binding.buttonNext.visibility = View.GONE
                    binding.buttonPrev.visibility = View.GONE
                }

            } ?: run {
                Toast.makeText(context, "No game details available. Please select a game from the list.", Toast.LENGTH_LONG).show()
                // Hide buttons as there's no data to navigate
                binding.buttonNext.visibility = View.GONE
                binding.buttonPrev.visibility = View.GONE
            }
        })


        setupButtonListeners(adapter)
    }

    private fun setupButtonListeners(adapter: NewsAdapter) {
        binding.buttonNext.setOnClickListener {
            val pages = viewModel.gameData.value?.news?.pages ?: emptyList()
            if (currentPageIndex < pages.size - 1) {
                currentPageIndex++
                updatePage(adapter)
            }
        }

        binding.buttonPrev.setOnClickListener {
            if (currentPageIndex > 0) {
                currentPageIndex--
                updatePage(adapter)
            }
        }
    }

    private fun updateUI(game: Game) {
        binding.textViewTitle.text = game.title
        binding.textViewAuthor.text = game.news.author
        Picasso.get().load(game.news.imageURL).into(binding.imageViewNews)
    }

    private fun updatePage(adapter: NewsAdapter) {
        val pages = viewModel.gameData.value?.news?.pages ?: emptyList()
        if (pages.isNotEmpty()) {
            adapter.updatePage(pages[currentPageIndex])
        }
    }

    private fun updatePages(adapter: NewsAdapter, pages: List<String>) {
        adapter.updatePages(pages)
    }

}
