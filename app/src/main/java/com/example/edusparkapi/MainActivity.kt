package com.example.edusparkapi

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edusparkapi.data.model.Game
import com.example.edusparkapi.ui.game.GameAdapter
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var rvGame: RecyclerView
    private val gameList = mutableListOf<Game>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGame = findViewById(R.id.rvGame)
        rvGame.layoutManager = LinearLayoutManager(this)

        fetchGames()
    }

    private fun fetchGames() {
        Thread {
            try {
                val url = URL("http://10.0.2.2:5000/api/games")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                val response = reader.readText()

                val jsonArray = JSONArray(response)
                gameList.clear()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    gameList.add(
                        Game(
                            obj.getInt("id"),
                            obj.getString("name"),
                            obj.getString("category"),
                            obj.getInt("totalPlayer")
                        )
                    )
                }

                runOnUiThread {
                    rvGame.adapter = GameAdapter(gameList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
        Log.d("API", "jalan")
    }
}