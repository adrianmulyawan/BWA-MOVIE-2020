package com.adrianmulyawan.bwamov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrianmulyawan.bwamov.checkout.PilihBangkuActivity
import com.adrianmulyawan.bwamov.home.dashboard.PlaysAdapter
import com.adrianmulyawan.bwamov.home.model.Film
import com.adrianmulyawan.bwamov.home.model.Plays
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    // Database Firebase
    lateinit var mDatabase: DatabaseReference
    // data list ini digunakan untuk menamqpung di bagian Who's Plays
    // yang isinya hanya nama dan url saja
    private var dataList = ArrayList<Plays>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val data = intent.getParcelableExtra<Film>("data")

        mDatabase = FirebaseDatabase.getInstance().getReference("Film")
            .child(data.judul.toString())
            .child("play")

        tv_kursi.text = data.judul
        tv_genre.text = data.genre
        tv_desc.text = data.desc
        tv_rate.text = data.rating

        Glide.with(this)
            .load(data.poster)
            .into(iv_poster)

        // Pada saat kita menekan tombol btn_pilih_bangku perpindahannya membawa data yang ada
        // mulai dari judul film, genre, storyboard. Kemudian kita lempar ke PilihBangkuActivity
        btn_pilih_bangku.setOnClickListener {
            val intent = Intent(this@DetailActivity,
                PilihBangkuActivity::class.java).putExtra("data", data)
            startActivity(intent)
        }

        iv_back.setOnClickListener {
            finish()
        }

        rv_who_play.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        getData()
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()){
                    val film = getdataSnapshot.getValue(Plays::class.java)
                    dataList.add(film!!)
                }

                rv_who_play.adapter = PlaysAdapter(dataList) {

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }

        })

    }
}
