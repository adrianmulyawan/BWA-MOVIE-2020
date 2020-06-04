package com.adrianmulyawan.bwamov.checkout

import android.content.Intent
import android.icu.text.NumberFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrianmulyawan.bwamov.R
import com.adrianmulyawan.bwamov.checkout.adapter.CheckoutAdapter
import com.adrianmulyawan.bwamov.checkout.model.Checkout
import com.adrianmulyawan.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.activity_checkout.*
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total:Int = 0

    private lateinit var preferences:Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        preferences = Preferences(this)
        // kemudian di tangkap lagi dengan menggunakan ArrayList
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>

        // kemudian di ekstrak untuk mengetahui total tiket yang harus dibayarkan
        for (a in dataList.indices){
            total += dataList[a].harga!!.toInt()
        }

        // kemudian setelah di ekstrak baru dimasukan kedalam dataList
        dataList.add(Checkout("Total Harus Dibayar", total.toString()))

        btn_tiket.setOnClickListener {
            val intent = Intent(this@CheckoutActivity,
                CheckoutSuccessActivity::class.java)
            startActivity(intent)
        }

        rc_checkout.layoutManager = LinearLayoutManager(this)
        // dan disini juga terdapat adapter didalamnya terdapat permainan mengconvert angka biasa menjadi angka harga
        rc_checkout.adapter = CheckoutAdapter(dataList) {
        }

        val localeID = Locale("in", "ID")
        val formatRupiah = java.text.NumberFormat.getCurrencyInstance(localeID)
        tv_saldo.setText(formatRupiah.format(preferences.getValue("saldo")!!.toDouble()))
    }
}
