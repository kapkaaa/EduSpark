package com.example.edusparkapi.leaderboard

import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.edusparkapi.R
import com.example.edusparkapi.data.model.Leaderboard

class LeaderboardAdapter (
    private val list: MutableList<Leaderboard>
): RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nick: TextView = view.findViewById(R.id.nick)
        val point: TextView = view.findViewById(R.id.point)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        Log.d("ADAPTER", "bind ${position}")
        holder.nick.text = data.nickname
        holder.point.text = data.totalPoint.toString()
    }

    override fun getItemCount(): Int {
        Log.d("ADAPTER", "size ${list.size}")
        return list.size
    }
}