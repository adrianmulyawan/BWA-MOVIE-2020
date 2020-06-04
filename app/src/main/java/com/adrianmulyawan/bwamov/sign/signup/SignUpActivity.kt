package com.adrianmulyawan.bwamov.sign.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.adrianmulyawan.bwamov.R
import com.adrianmulyawan.bwamov.sign.signin.User
import com.adrianmulyawan.bwamov.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    lateinit var sUsername: String
    lateinit var sPassword: String
    lateinit var sNama: String
    lateinit var sEmail: String

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Inisiasi Firebase
        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mFirebaseDatabase = mFirebaseInstance.getReference("User")

        preferences = Preferences(this)

        // Pada saat kita menekan tombol btn_home dia akan mengambil nilai dari EditText tersebut
        // dan ditampung kedalam sUsername, sPassword, sNama, dan sEmail
        btn_home.setOnClickListener {
            sUsername = et_username.text.toString()
            sPassword = et_password.text.toString()
            sNama = et_nama.text.toString()
            sEmail = et_email.text.toString()

            // Dilakukan pengecekan apabila nilai yang diisikan kosong akan menampilkan pesan error "Silahkan isi ...."
            // dan requestFocus akan mengarahkan kebagian mana yang error
                if (sUsername.equals("")) {
                et_username.error = "Silahkan isi Username !"
                et_username.requestFocus()
            } else if (sPassword.equals("")) {
                et_password.error = "Silahkan isi Password !"
                et_password.requestFocus()
            } else if (sNama.equals("")) {
                et_nama.error = "Silahkan isi Nama !"
                et_nama.requestFocus()
            } else if (sEmail.equals("")) {
                et_email.error = "Silahkan isi Email !"
                et_email.requestFocus()
            } else {
                // Jika tidak ada yang error akan di alihkan ke fungsi saveUser()
                saveUser(sUsername, sPassword, sNama, sEmail)
            }
        }
    }

    private fun saveUser(sUsername: String, sPassword: String, sNama: String, sEmail: String) {
        // Pada bagian ini dia akan mengeset berdasarkan kelas User
        val user = User()
        user.email = sEmail
        user.username = sUsername
        user.nama = sNama
        user.password = sPassword

        // kemudian jika tidak kosong datanya akan dilempar ke fungsi checkingUsername()
        if (sUsername != null) {
            checkingUsername(sUsername, user)
        }
    }

    // Disini akan dilakukan pengecekan usernamenya sudah ada atau belom ?
    //
    private fun checkingUsername(iUsername: String, data: User) {
        mFirebaseDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user == null) {
                    // jika user tersedia bisa dilanjutkan dan di set
                    mFirebaseDatabase.child(iUsername).setValue(data)

                    preferences.setValue("nama",data.nama.toString())
                    preferences.setValue("user",data.username.toString())
                    preferences.setValue("url","")
                    preferences.setValue("email",data.email.toString())
                    preferences.setValue("status","1")

                    val intent = Intent(this@SignUpActivity,
                        SignUpPhotoscreenActivity::class.java).putExtra("nama", data.nama)
                    startActivity(intent)
                } else {
                    // Jika user telah digunakan Toast akan dijalankan
                    Toast.makeText(this@SignUpActivity, "User sudah digunakan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
