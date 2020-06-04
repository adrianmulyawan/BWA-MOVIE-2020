package com.adrianmulyawan.bwamov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adrianmulyawan.bwamov.R
import com.adrianmulyawan.bwamov.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_three.*

class OnboardingThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_three)

        btn_home.setOnClickListener {
            finishAffinity()
            // Menghapus semua activity yang sudah ada sebelumnya
            // Kenapa harus menghapus Activity sebelumnya ?
            // Karna pada saat menekan tombol "back" kita bisa langsung exit tanpa harus balik ke activity sebelumnya

            val intent = Intent(this@OnboardingThreeActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
