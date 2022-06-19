package com.example.themovieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.R
import com.example.themovieapp.delegates.ShowcaseViewHolderDelegate
import com.example.themovieapp.viewholders.ShowcaseViewHolder

class ShowcaseAdapter(private val mDelegate: ShowcaseViewHolderDelegate) : RecyclerView.Adapter<ShowcaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowcaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_showcase, parent, false)
        return ShowcaseViewHolder(view, mDelegate)
    }

    override fun onBindViewHolder(holder: ShowcaseViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }
}