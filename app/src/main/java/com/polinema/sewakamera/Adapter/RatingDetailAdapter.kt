package com.polinema.sewakamera.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Model.Rating
import com.polinema.sewakamera.R

class RatingDetailAdapter (context: Context, private val ratingList: ArrayList<Rating>): RecyclerView.Adapter<RatingDetailAdapter.ViewHolder>()  {

    val ctx: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val productView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_detail_rating,parent,false)
        return ViewHolder(productView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val helper = DataHelper(ctx)
        val product: Rating = ratingList[position]
        holder.idDetailPenilaian.text = product.id.toString()
        holder.namaUserDetailPenilaian.text = product.nama_user
        holder.deskripsiDetailPenilaian.text = product.deskripsi
        holder.ratingDetailPenilaian.rating = product.rating

        Glide.with(ctx)
            .load(product.image)
            .into(holder.imageDetailPenilaian)


    }

    override fun getItemCount(): Int {
        return ratingList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val imageDetailPenilaian: ImageView = itemView.findViewById(R.id.imageDetailPenilaian)
        val ratingDetailPenilaian: RatingBar = itemView.findViewById(R.id.ratingDetailPenilaian)
        val namaUserDetailPenilaian: TextView = itemView.findViewById(R.id.namaUserDetailPenilaian)
        val deskripsiDetailPenilaian: TextView = itemView.findViewById(R.id.deskripsiDetailPenilaian)
        val idDetailPenilaian: TextView = itemView.findViewById(R.id.idDetailPenilaian)


    }
}