package com.adrianmulyawan.bwamov.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.adrianmulyawan.bwamov.R
import com.adrianmulyawan.bwamov.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    // inisiasi database sementara yang mendapat data dari saat kita login
    lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // pemanggilan data yang telah diinisiasi
        preferences = Preferences(context!!.applicationContext)

        iv_nama.text = preferences.getValue("nama")
        tv_email.text = preferences.getValue("email")

        Glide.with(this)
            .load(preferences.getValue("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile)
    }
}
