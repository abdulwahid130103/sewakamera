package com.polinema.sewakamera.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.DetailProdukActivity

class ProdukTerlarisAdapter (private val produkTerlarisList: ArrayList<Produk>, context: Context):  RecyclerView.Adapter<ProdukTerlarisAdapter.ViewHolder>()  {

    val ctx: Context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView = LayoutInflater.from(parent.context).inflate(R.layout.single_product,parent,false)
        return ViewHolder(productView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product: Produk = produkTerlarisList[position]
        val helper = DataHelper(ctx)
        holder.productBrandName_singleProduct.text = product.id_category
        holder.productIdSingle.text = product.id.toString()
        holder.productName_singleProduct.text = product.nama_produk
        holder.productPrice_singleProduct.text = helper.formatRupiah(product.harga)
        Glide.with(ctx)
            .load(product.image)
            .placeholder(R.drawable.bn)
            .into(holder.productImage_singleProduct)



        holder.itemView.setOnClickListener {
            goDetailsPage(product.id)
        }

        holder.productRating_singleProduct.rating = product.rating
    }

    override fun getItemCount(): Int {
        return produkTerlarisList.size
    }





    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val productImage_singleProduct: ImageView = itemView.findViewById(R.id.productImage_singleProduct)
        val productRating_singleProduct: RatingBar = itemView.findViewById(R.id.productRating_singleProduct)
        val productBrandName_singleProduct: TextView = itemView.findViewById(R.id.productBrandName_singleProduct)
        val discountTv_singleProduct: TextView = itemView.findViewById(R.id.discountTv_singleProduct)
        val productName_singleProduct: TextView = itemView.findViewById(R.id.productName_singleProduct)
        val productPrice_singleProduct: TextView = itemView.findViewById(R.id.productPrice_singleProduct)
        val productIdSingle: TextView = itemView.findViewById(R.id.productIdSingle)
        val discount_singleProduct = itemView.findViewById<LinearLayout>(R.id.discount_singleProduct)


    }

    private fun goDetailsPage(position: Int) {
        val intent = Intent(ctx , DetailProdukActivity::class.java)
        intent.putExtra("ProductIndex", position)
        intent.putExtra("ProductFrom", "Cover")
        ctx.startActivity(intent)
    }
}