package com.dong.repository.CustomView.TuyaAir

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dong.repository.R
import kotlin.concurrent.thread

class TuYaAirActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tu_ya_air)
        thread {
            Thread.sleep(2000)
            runOnUiThread {
                findViewById<TuYaAirBg>(R.id.tuya_bg).startAnimation()
            }
        }
    }
}