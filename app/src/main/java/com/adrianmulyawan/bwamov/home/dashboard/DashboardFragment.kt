package com.adrianmulyawan.bwamov.home.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrianmulyawan.bwamov.DetailActivity
import com.adrianmulyawan.bwamov.R
import com.adrianmulyawan.bwamov.home.model.Film
import com.adrianmulyawan.bwamov.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    // Database sementara
    private lateinit var preferences: Preferences
    // Database Firebase
    lateinit var mDatabase: DatabaseReference

    // penampung datanya nanti ketika ditampilkan
    private var dataList = ArrayList<Film>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // inisiasi pertama
        preferences = Preferences(activity!!.applicationContext)
        // sedangkan film key nya berasal dari FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")

        // mengambil value dari database sementara dan ditampilkan di TextView "tv_nama"
        tv_nama.setText(preferences.getValue("nama"))
        // mengambil value dari database untuk menampilkan saldo yang dimiliki
        if (!preferences.getValue("saldo").equals("")){
            currecy(preferences.getValue("saldo")!!.toDouble(), tv_saldo)
        }

        // digunakan untuk menampilkan objek photo
        Glide.with(this)
            .load(preferences.getValue("url")) // menampilkan photo dari si usernya
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile)

        // menampilkan data (Now Playing) dalam bentuk linear layout secara horizontal
        rv_now_playing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // menampilkan data (Coming Soon) dalam bentuk linear layout secara vertical
        rv_coming_soon.layoutManager = LinearLayoutManager(context!!.applicationContext)
        getData() // mengambil data dari firebase

    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // fungsi utamanya ada didalam sini
                // pertama penampungnya kita bersihkan dahulu sehingga data cachenya tidak ada
                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {
                    // setelah data tampungnya ada kita tangkap value dari firebase dengan menggunakan parameter film
                    val film = getdataSnapshot.getValue(Film::class.java!!)
                    // dan kita masukan kedalam dataList ini dengan cara perulangan sesuai banyaknya data didalam firebase
                    dataList.add(film!!)
                }

                // setelah kelar proses perulangannya dia memasukan data tsb kedalam adapter (NowPlayingAdapter & ComingSoonAdapter)
                // Kenapa 2? karena item masing2 didalamnya berbeda karena itu kita membutuhkan adapter yang berbeda juga
                rv_now_playing.adapter = NowPlayingAdapter(dataList) {
                    val intent = Intent(context,
                        DetailActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }

                rv_coming_soon.adapter = ComingSoonAdapter(dataList) {
                    val intent = Intent(context,
                        DetailActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }

                // Fungsi dari putExtra("data", it) = data ini digunakan untuk melakukan reload data
                // di dalam DetailActivity tanpa harus memangil database dari Firebase
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    // Fungsi ini digunakan untuk mengubah value saldo yang awalnya angka menjadi huruf
    // diharapkan user mudah membacanya
    private fun currecy(harga:Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.setText(formatRupiah.format(harga as Double))

    }

}
