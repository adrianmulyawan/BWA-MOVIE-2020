package com.adrianmulyawan.bwamov.sign.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adrianmulyawan.bwamov.home.HomeActivity
import com.adrianmulyawan.bwamov.R
import com.adrianmulyawan.bwamov.sign.signup.SignUpActivity
import com.adrianmulyawan.bwamov.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    lateinit var iUsername: String
    lateinit var iPassword: String

    lateinit var mDatabase: DatabaseReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Inisiasi Database
        mDatabase = FirebaseDatabase.getInstance().getReference("User")

        // Inisiasi Preferences
        preferences = Preferences(this)

        preferences.setValue("onboarding", "1")
        // kenapa onboarding juga di tambahkan ? Karna onboarding muncul hanya saat pertama kali menjalankan aplikasi
        if (preferences.getValue("status").equals("1")) { // jika status = 1 -> HomeActivity
            finishAffinity()

            val intent = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        btn_home.setOnClickListener {
            iUsername = et_username.text.toString()
            iPassword = et_password.text.toString()

            if (iUsername.equals("")) {
                et_username.error = "Silahkan Tulis Username Anda"
                et_username.requestFocus()
            } else if (iPassword.equals("")) {
                et_password.error = "Silahkan Tulis Password Anda"
                et_password.requestFocus()
            } else {
                pushLogin(iUsername, iPassword)
            }
        }

        btn_daftar.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null) {
                    Toast.makeText(this@SignInActivity, "User tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    if (user.password.equals(iPassword)) {
                        Toast.makeText(this@SignInActivity, "Selamat Datang", Toast.LENGTH_LONG).show()

                        // maksud dari baris ini kita menyimpan data dari nama hingga status
                        // datanya dari firebase
                        preferences.setValue("nama", user.nama.toString())
                        preferences.setValue("user", user.username.toString())
                        preferences.setValue("url", user.url.toString())
                        preferences.setValue("email", user.email.toString())
                        preferences.setValue("saldo", user.saldo.toString())

                        // status digunakan untuk login
                        preferences.setValue("status", "1")

                        finishAffinity()

                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignInActivity, "Password Anda Salah", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
