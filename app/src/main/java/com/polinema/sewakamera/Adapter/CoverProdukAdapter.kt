package com.polinema.sewakamera.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.DetailProdukActivity

class CoverProdukAdapter (var ctx: Context, private val coverProdukList: ArrayList<Produk>) :
    RecyclerView.Adapter<CoverProdukAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView = LayoutInflater.from(parent.context).inflate(R.layout.cover_single,parent,false)
        return ViewHolder(productView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val coverPro: Produk = coverProdukList[position]

        holder.productNoteCover.text = coverPro.nama_produk
        holder.productIdCover.text = coverPro.id.toString()
        Glide.with(ctx)
            .load(coverPro.image)
            .into(holder.productImage_coverPage)


        holder.productCheck_coverPage.setOnClickListener {

            goDetailsPage(coverPro.id)


        }

    }


    override fun getItemCount(): Int {
        return coverProdukList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val productImage_coverPage: ImageView = itemView.findViewById(R.id.productImage_coverPage)
        val productNoteCover: TextView = itemView.findViewById(R.id.productNoteCover)
        val productIdCover: TextView = itemView.findViewById(R.id.productIdCover)
        val productCheck_coverPage: Button = itemView.findViewById(R.id.productCheck_coverPage)


    }

    private fun goDetailsPage(position: Int) {
        val intent = Intent(ctx , DetailProdukActivity::class.java)
        intent.putExtra("ProductIndex", position)
        intent.putExtra("ProductFrom", "cover")
        ctx.startActivity(intent)
    }
}