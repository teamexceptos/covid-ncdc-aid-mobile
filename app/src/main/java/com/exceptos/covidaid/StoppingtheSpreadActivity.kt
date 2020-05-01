package com.exceptos.covidaid

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_stopping_the_spread.*

class StoppingtheSpreadActivity : AppCompatActivity() {

    val mActivity: Activity = this@StoppingtheSpreadActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        setContentView(R.layout.activity_stopping_the_spread)

        img_back_pressed.setOnClickListener {
            onBackPressed()
        }

    }
}