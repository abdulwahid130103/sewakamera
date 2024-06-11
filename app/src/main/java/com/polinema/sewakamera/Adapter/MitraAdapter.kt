package com.polinema.sewakamera.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Model.Category
import com.polinema.sewakamera.Model.Mitra
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.DetailMitraActivity
import com.polinema.sewakamera.View.Activity.ProdukListActivity

class MitraAdapter(var ctx: Context, private val mitraList: ArrayList<Mitra>) :
    RecyclerView.Adapter<MitraAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val categoryView = LayoutInflater.from(parent.context).inflate(R.layout.mitra_single,parent,false)
        return ViewHolder(categoryView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val item: Mitra = mitraList[position]

        holder.nameMitra.text = item.name
        holder.idMitraView.text = item.id.toString()

        Glide.with(ctx)
            .load(item.image)
            .into(holder.mitraImageView)

        holder.itemView.setOnClickListener{
            val intent = Intent(ctx, DetailMitraActivity::class.java)
            intent.putExtra("data_mitra", item.id.toString())
            ctx.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mitraList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val mitraImageView: ImageView = itemView.findViewById(R.id.mitraImageView)
        val nameMitra: TextView = itemView.findViewById(R.id.NamaMitraView)
        val idMitraView: TextView = itemView.findViewById(R.id.idMitraView)

    }

}