package com.adrianmulyawan.bwamov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.adrianmulyawan.bwamov.onboarding.OnboardingOneActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var handler = Handler()
        // Fungsi Handler() digunakan untuk menahan suatu activity (SplashScreenActivity())
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity, OnboardingOneActivity::class.java)
            startActivity(intent)
            finish() // untuk mengakhiri activity apabila telah selesai
        }, 5000) // 5000 = waktu delay (5 detik)
    }
}
