package com.example.edusparkapi.ui.game

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.edusparkapi.R

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_detail)

        val txtjudul = findViewById<TextView>(R.id.txtJudul)

        val gameName = intent.getStringExtra("GAME_NAME")
        txtjudul.text = gameName

        val gameid = intent.getIntExtra("GAME_ID", -1)
        Log.d("GAME_DETAIL", "id $gameid")
    }
}