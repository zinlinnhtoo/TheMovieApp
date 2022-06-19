package com.example.themovieapp.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.delegates.ShowcaseViewHolderDelegate

class ShowcaseViewHolder(itemView: View, mDelegate: ShowcaseViewHolderDelegate) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener { mDelegate.onTapMovieFromShowcase() }
    }
}