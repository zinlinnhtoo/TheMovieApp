package com.example.themovieapp.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.delegates.MovieViewHolderDelegate

class MovieViewHolder(itemView: View, private val mDelegate: MovieViewHolderDelegate) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener { mDelegate.onTapMovie() }
    }
}