package com.example.androidnetworking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidnetworking.R
import com.example.androidnetworking.activity.MainActivity
import com.example.androidnetworking.model.Poster

class PosterAdapter(var activity: MainActivity, var items: ArrayList<Poster>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_poster_list, parent, false)
        return PosterViewHolder(view)
    }

    class PosterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var llForeground: LinearLayout = view.findViewById(R.id.ll_foreground)
        var tvTitle: TextView = view.findViewById(R.id.tv_title)
        var tvBody: TextView = view.findViewById(R.id.tv_body)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is PosterViewHolder) {
            holder.tvTitle.text = item.title.toUpperCase()
            holder.tvBody.text = item.body

            holder.llForeground.setOnLongClickListener {
                activity.dialogChooseAction(item, position)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}