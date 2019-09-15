package com.rikerd.platelet

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.media.MediaPlayer
import android.media.SoundPool
import android.view.MotionEvent
import android.view.View
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*


class MainActivity : AppCompatActivity() {
    var backgroundMP: MediaPlayer? = null

    var pressed = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        backgroundMP = MediaPlayer.create(this, R.raw.platelet_bg)

        backgroundMP!!.setLooping(true)

        val pokeSoundPool = SoundPool.Builder().build()
        val pokeSoundId = pokeSoundPool.load(this, R.raw.poke_sound, 1)

        val tickleSoundPool = SoundPool.Builder().build()
        val tickleSoundId = tickleSoundPool.load(this, R.raw.tickles, 1)
        val tickleSoundId2 = tickleSoundPool.load(this, R.raw.again, 1)

        val maxCounter = 9
        var counter = 0

        val maxSecondaryCounter = 27
        var secondaryCounter = 0

        //val tickleSoundMP = MediaPlayer.create(this, R.raw.tickles)

        val imgButton = findViewById<Button>(R.id.imgButton)

        imgButton.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    imgButton.setBackgroundResource(R.drawable.poke)

                    if (!backgroundMP!!.isPlaying) {
                        pressed = true
                        backgroundMP!!.start()
                    }

                    pokeSoundPool.play(pokeSoundId, 0.8f, 0.8f, 1, 0, 1f)

                } else if (event?.action == MotionEvent.ACTION_UP) {
                    imgButton.setBackgroundResource(R.drawable.initial)

                    secondaryCounter++
                    counter++

                    if (secondaryCounter == maxSecondaryCounter) {
                        val rand = Random().nextInt(9) + 1

                        if (rand <= 7) {
                            tickleSoundPool.play(tickleSoundId, 1f, 1f, 1,0,1f)
                        } else {
                            tickleSoundPool.play(tickleSoundId2, 1f, 1f, 1,0,1f)
                        }

                        secondaryCounter = 0
                        counter = 0
                    } else if (counter == maxCounter) {
                        tickleSoundPool.play(tickleSoundId, 1f, 1f, 1,0,1f)
                        counter = 0
                    }
                }
                return true
            }
        })
    }

    override fun onPause() {
        super.onPause()

        if (backgroundMP!!.isPlaying) {
            backgroundMP!!.pause()
        }
    }

    override fun onResume() {
        super.onResume()

        if (pressed && !backgroundMP!!.isPlaying) {
            backgroundMP!!.start()
        }
    }
}
