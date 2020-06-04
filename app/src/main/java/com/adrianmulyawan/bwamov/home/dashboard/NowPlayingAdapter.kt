package com.adrianmulyawan.bwamov.home.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adrianmulyawan.bwamov.R
import com.adrianmulyawan.bwamov.home.model.Film
import com.bumptech.glide.Glide

class NowPlayingAdapter(private var data: List<Film>,
                        private val listener: (Film) -> Unit)
    : RecyclerView.Adapter<NowPlayingAdapter.LeagueViewHolder>() {

    lateinit var ContextAdapter : Context

    // fungsi yang digunakan untuk membuat sebuah viewnya
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        ContextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_now_playing, parent, false)

        return LeagueViewHolder(inflatedView)
    }

    // fungsi yang digunakan untuk mengaitkan view dan item yang ada
    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    // fungsi yang digunakan untuk mengecek seberapa banyak data yang ada
    override fun getItemCount(): Int = data.size

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // inisiasi dari sebuah TextView dan disimpan kedalam variabel baru
        private val tvTitle: TextView = view.findViewById(R.id.tv_kursi)
        private val tvGenre: TextView = view.findViewById(R.id.tv_genre)
        private val tvRate: TextView = view.findViewById(R.id.tv_rate)

        private val tvImage: ImageView = view.findViewById(R.id.iv_poster_image)

        fun bindItem(data: Film, listener: (Film) -> Unit, context : Context, position : Int) {
            // dan disini kita melakukan pengesetan data
            tvTitle.text = data.judul
            tvGenre.text = data.genre
            tvRate.text = data.rating

            Glide.with(context)
                .load(data.poster)
                .into(tvImage);

            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}
