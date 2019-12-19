package com.sanjog.lanatest.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sanjog.lanatest.data.ProductRepository

/**
 * Created by sanjogstha on 2019-12-11.
 * sanjogshrestha.nepal@gmail.com
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}