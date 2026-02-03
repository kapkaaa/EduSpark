package com.example.edusparkapi.ui.game

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edusparkapi.R
import com.example.edusparkapi.data.model.Category
import com.example.edusparkapi.data.model.Game

class GameAdapter (
    private val list: List  <Game>
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.list_item)
        val txtCategory: TextView = view.findViewById(R.id.category)
        val txtCount: TextView = view.findViewById(R.id.count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(view  )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = list[position]
        holder.txtName.text = game.name
        holder.txtCategory.text = game.category
        holder.txtCount.text = "${game.totalPlayer.toString()} Players"

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, GameActivity::class.java)
            intent.putExtra("GAME_ID", game.id)
            intent.putExtra("GAME NAME", game.name)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = list.size
}