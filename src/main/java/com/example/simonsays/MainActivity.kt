package com.example.simonsays

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val highscore: TextView = findViewById(R.id.highscore)
        val levelCounter: TextView = findViewById(R.id.levelCounter)

        var gameOn = false
        val sharedPref = getSharedPreferences("Simon Says", Context.MODE_PRIVATE)
        highscore.text = getString(R.string.highscore_display, sharedPref.toString())
        val gamePattern: MutableList<Int> = mutableListOf()
        var level= 0
        var usrClickPattern: MutableList<Int> = mutableListOf()


        fun highlight(id: Int){
            when(id){
                1 -> button1.setBackgroundResource(R.drawable.green_highlight)
                2 -> button2.setBackgroundResource(R.drawable.yellow_highlight)
                3 -> button3.setBackgroundResource(R.drawable.red_highlight)
                else -> button4.setBackgroundResource(R.drawable.blue_highlight)

            }

            CoroutineScope(Dispatchers.Main).launch {
                delay(1000) // Delay for 1 second

                // Reset the button background to the original state
                when (id) {
                    1 -> button1.setBackgroundResource(R.drawable.green_highlight)
                    2 -> button2.setBackgroundResource(R.drawable.yellow_highlight)
                    3 -> button3.setBackgroundResource(R.drawable.red_highlight)
                    else -> button4.setBackgroundResource(R.drawable.blue_highlight)
                }
            }
        }

        fun nextSequence(){
            usrClickPattern = mutableListOf()
            level++
            levelCounter.text = getString(R.string.level_display, level.toString())
            val newChoice: Int = (1..4).random()
            gamePattern.add(newChoice)
            when(newChoice){
                1 -> highlight(1)
                2 -> highlight(2)
                3 -> highlight(3)
                else -> highlight(4)
            }
        }

        fun startOver() {
            level = 0
            gamePattern.clear()
            gameOn = false
        }

        fun checkPattern(){
            if (gamePattern[level]== usrClickPattern[level]) {
                if (usrClickPattern.size == gamePattern.size) {
                    //add a wait
                    nextSequence()
                }
            }else {
                gameOn = false
                val usrRecord = 0
                levelCounter.text = getString(R.string.game_over_display, level.toString())
                var record = sharedPref.getString("highscore", null)
                if(record == null) {
                    record = usrRecord.toString()
                }
                if (level > record.toInt()) {
                    record = level.toString()
                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putString("highscore", record)
                    editor.apply()
                }
                highscore.text = getString(R.string.recordDisplay, record.toString())
                startOver()
            }
        }

        button1.setOnClickListener{
            if(!gameOn){
                gameOn = true
                nextSequence()
                levelCounter.text = getString(R.string.level, level.toString())
                checkPattern()
            }else{
                usrClickPattern += 1
                highlight(1)
            }
        }

        button2.setOnClickListener{
            if(!gameOn){
                gameOn = true
                nextSequence()
                levelCounter.text = getString(R.string.level, level.toString())
                checkPattern()
            }else{
                usrClickPattern += 2
                highlight(2)
            }
        }

        button3.setOnClickListener{
            if(!gameOn){
                gameOn = true
                nextSequence()
                levelCounter.text = getString(R.string.level, level.toString())
                checkPattern()
            }else{
                usrClickPattern += 3
                highlight(3)
            }
        }

        button4.setOnClickListener{
            if(!gameOn){
                gameOn = true
                nextSequence()
                levelCounter.text = getString(R.string.level, level.toString())
                checkPattern()
            }else{
                usrClickPattern += 4
                highlight(4)
            }
        }
    }
}