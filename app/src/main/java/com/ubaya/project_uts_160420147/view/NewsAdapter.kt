package com.ubaya.project_uts_160420147.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.project_uts_160420147.R

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var currentPage: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        currentPage?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = if (currentPage != null) 1 else 0

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.text_view_news_page)

        fun bind(page: String) {
            textView.text = page
        }
    }

    fun updatePage(newPage: String?) {
        currentPage = newPage
        notifyDataSetChanged()  // Notify that the data has changed
    }

    fun updatePages(newPages: List<String>) {
        currentPage = newPages.toString()
        notifyDataSetChanged()
    }
}
