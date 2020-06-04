package com.adrianmulyawan.bwamov.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.adrianmulyawan.bwamov.R
import com.adrianmulyawan.bwamov.home.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // inisiasi DashboardFragment di HomeActivity
        val fragmentHome =
            DashboardFragment()
        // inisiasi TiketFragment di HomeActivity
        val fragmentTiket = TiketFragment()
        // inisiasi SettingFragment di HomeActivity
        val fragmentSetting = SettingFragment()

        setFragment(fragmentHome) // yang ditampilkan pertama di halaman Home

        iv_menu1.setOnClickListener {
            setFragment(fragmentHome)

            changeIcon(iv_menu1, R.drawable.icon_in_home)
            changeIcon(iv_menu2, R.drawable.icon_tiket)
            changeIcon(iv_menu3, R.drawable.icon_profile)
        }

        iv_menu2.setOnClickListener {
            setFragment(fragmentTiket)

            changeIcon(iv_menu1, R.drawable.icon_home)
            changeIcon(iv_menu2, R.drawable.icon_in_tiket)
            changeIcon(iv_menu3, R.drawable.icon_profile)
        }

        iv_menu3.setOnClickListener {
            setFragment(fragmentSetting)

            changeIcon(iv_menu1, R.drawable.icon_home)
            changeIcon(iv_menu2, R.drawable.icon_tiket)
            changeIcon(iv_menu3, R.drawable.icon_in_profile)
        }
    }

    // mengatur perpindahan fragment
    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_frame, fragment)
        fragmentTransaction.commit()
    }

    private fun changeIcon(imageView: ImageView, int: Int) {
        imageView.setImageResource(int)
    }
}
