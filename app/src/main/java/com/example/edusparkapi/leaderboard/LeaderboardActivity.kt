package com.example.edusparkapi.leaderboard

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edusparkapi.R
import com.example.edusparkapi.data.model.Leaderboard
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var rvlead: RecyclerView
    private lateinit var adapter: LeaderboardAdapter
    private val leaderboardList = mutableListOf<Leaderboard>()

    private var gameId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard)

        val sbmit = findViewById<Button>(R.id.submit)
        val input = findViewById<TextInputLayout>(R.id.nick)

        sbmit.setOnClickListener {
            val name = input.editText?.text.toString().trim()

            if (name.isEmpty()) {
                input.error = "Nickname wajib di isi"
                return@setOnClickListener
            }

            postLeaderboard(name)
        }

        gameId = intent.getIntExtra("GAME_ID", -1)
        Log.d("LEADERBOARD", "id ${gameId}")

        rvlead = findViewById(R.id.rvLead)
        rvlead.layoutManager = LinearLayoutManager(this)

        adapter = LeaderboardAdapter(leaderboardList)
        rvlead.adapter = adapter

        if (gameId != -1) {
            fetchLeaderboard()
        }
    }

    private fun fetchLeaderboard() {
        thread {
            try {
                val url = URL("http://10.0.2.2:5000/api/leaderboards/${gameId}")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val response = conn.inputStream.bufferedReader().readText()
                Log.d("LEADERBOARD", response)

                val  jsonArray = JSONArray (response)
                leaderboardList.clear()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    leaderboardList.add(
                        Leaderboard(
                            obj.getInt("id"),
                            obj.getString("nickname"),
                            obj.getInt("totalPoint")
                        )
                    )
                }
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun postLeaderboard(nickname: String) {
        thread {
            try {
                val url = URL("http://10.0.2.2:5000/api/ledearboards")
                val conn = url.openConnection()as HttpURLConnection

                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                val jsonBody = """
                {
                    "nickname": "$nickname",
                    "totalPoint": ${intent.getIntExtra("SCORE", 0)},
                    "gameID": $gameId
                }""".trimIndent()

                conn.outputStream.use {
                    it.write(jsonBody.toByteArray())
                }

                val respondCode = conn.responseCode
                Log.d("POST", "mbo $respondCode")

                if (respondCode == HttpURLConnection.HTTP_OK ||
                    respondCode == HttpURLConnection.HTTP_CREATED
                ) {
                    runOnUiThread {
                        fetchLeaderboard()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}