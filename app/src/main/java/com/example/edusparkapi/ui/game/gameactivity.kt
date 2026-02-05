package com.example.edusparkapi.ui.game

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.edusparkapi.R
import com.example.edusparkapi.data.model.Words
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class GameActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var hint: TextView
    private lateinit var input: EditText
    private lateinit var prev: Button
    private lateinit var next: Button
    private lateinit var txtjudul: TextView

    private val wordList = mutableListOf<Words>()
    private var currentIndex = 0
    private var score = 0
    private var gameId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_detail)

        imageView = findViewById(R.id.imageView)
        hint = findViewById((R.id.hint))
        input = findViewById(R.id.input)
        prev = findViewById(R.id.prev)
        next = findViewById(R.id.next)
        txtjudul = findViewById(R.id.txtJudul)

        gameId = intent.getIntExtra("GAME_ID", 0)
        val gameName = intent.getStringExtra("GAME_NAME")
        txtjudul.text = gameName

        fetchWords()

        next.setOnClickListener{
            checkAnswer()
            input.setText(null)

            if(currentIndex < wordList.size -1) {
                currentIndex++
                showQuestion()
            }else {
                Toast.makeText(this, "Finish Score: $score", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        prev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showQuestion()
            } else {
                AlertDialog.Builder(this)
                    .setMessage("Apakah anda yakin ingin keluar dari game?")
                    .setPositiveButton("Ya") { _, _ -> finish()}
                    .setNegativeButton("Tidak", null)
                    .show()

            }
        }

        Log.d("GAME_DETAIL", "id $gameId")
    }

    private fun fetchWords() {
        Thread {
            try {
                val url = URL("http://10.0.2.2:5000/api/words/$gameId")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val response = BufferedReader(
                    InputStreamReader(conn.inputStream)
                ).readText()

                val jsonArray = JSONArray (response)
                wordList.clear()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    Log.d("DEBUG_JSON", obj.toString())
                    wordList.add(
                        Words(
                            obj.getInt("id"),
                            obj.getString("image"),
                            obj.getString("word"),
                            obj.getInt("point")
                        )
                    )
                }

                runOnUiThread {
                    if (wordList.isNotEmpty()) {
                        showQuestion()
                    } else {
                        Toast.makeText(this, "kosong", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showQuestion() {
        val words = wordList[currentIndex]

        val imageUrl = "http://10.0.2.2:5000/images/${words.image}"
        Log.d("image", imageUrl)
        Glide.with(this).load(imageUrl).into(imageView)

        hint.text = words.word
            .toCharArray()
            .toList()
            .shuffled()
            .joinToString("")

        next.text = if (currentIndex == wordList.size - 1) "Finish" else "Next"
        prev.text = if(currentIndex == 0 ) "Keluar" else "Prev"
    }

    private fun checkAnswer() {
        val userAnswer = input.text.toString().trim()
        val correctAnswer = wordList[currentIndex].word

        if (userAnswer.equals(correctAnswer, true)) {
            score += wordList[currentIndex].point
        }
    }
}