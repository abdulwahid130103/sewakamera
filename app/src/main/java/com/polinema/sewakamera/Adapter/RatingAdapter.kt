package com.polinema.sewakamera.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Model.DetailHistory
import com.polinema.sewakamera.Model.ProdukRating
import com.polinema.sewakamera.R

class RatingAdapter  (var ctx: Context, private val ratingList: ArrayList<ProdukRating>) :
    RecyclerView.Adapter<RatingAdapter.ViewHolder>() {

    private val viewHolderList = mutableListOf<ViewHolder>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val categoryView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_rating, parent, false)
        return ViewHolder(categoryView)
    }

    fun getData(): ArrayList<HashMap<String, String>> {
        val dataList = ArrayList<HashMap<String, String>>()
        for (i in 0 until ratingList.size) {
            val holder = viewHolderList[i]
            val rating = holder.ratingBar.rating.toString()
            val deskripsi = holder.deskripsiRating.text.toString()
            val id = ratingList[i].id.toString()

            val data = HashMap<String, String>()
            data["id"] = id
            data["rating"] = rating
            data["deskripsi"] = deskripsi

            dataList.add(data)
        }
        return dataList
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ProdukRating = ratingList[position]

        val hp = DataHelper(ctx)
        holder.bind(item, hp)
        viewHolderList.add(holder)
    }

    fun getRatingAndDescription(position: Int): Pair<Float, String> {
        val holder = viewHolderList[position]
        val rating = holder.ratingBar.rating
        val deskripsi = holder.deskripsiRating.text.toString()
        return Pair(rating, deskripsi)
    }



    override fun getItemCount(): Int {
        return ratingList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idRatingProduk: TextView = itemView.findViewById(R.id.idRatingProduk)
        val namaRatingProduk: TextView = itemView.findViewById(R.id.namaRatingProduk)
        val hargaRatingProduk: TextView = itemView.findViewById(R.id.hargaRatingProduk)
        val imageRatingProduk: ImageView = itemView.findViewById(R.id.imageRatingProduk)
        var ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        var deskripsiRating: TextView = itemView.findViewById(R.id.deskripsiRating)

        fun bind(item: ProdukRating, hp: DataHelper) {
            idRatingProduk.text = item.id.toString()
            namaRatingProduk.text = item.nama_produk

            Glide.with(ctx)
                .load(item.image)
                .into(imageRatingProduk)

            hargaRatingProduk.text = hp.formatRupiah(item.harga.toString().toInt())

            ratingBar.rating = item.rating
            deskripsiRating.text = item.deskripsi
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                item.rating = rating
            }
            deskripsiRating.addTextChangedListener { text ->
                item.deskripsi = text.toString()
            }
        }
    }

}