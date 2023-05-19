package com.ggg.gggapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.gggapp.R
import com.ggg.gggapp.model.NewsClass

class TaskAdapter(val data: ArrayList<NewsClass>, val context: Context) :
    RecyclerView.Adapter<TaskAdapter.VHH>() {

    class VHH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.titleText)
        var description: TextView = itemView.findViewById(R.id.descriptionText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHH {
        return VHH(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false))
    }

    override fun onBindViewHolder(holder: VHH, position: Int) {

        holder.title.text = data[holder.adapterPosition].titleText
        holder.description.text = data[holder.adapterPosition].descriptionText
    }


    override fun getItemCount(): Int {
        return data.size
    }
}