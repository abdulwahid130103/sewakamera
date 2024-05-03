package com.polinema.sewakamera.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.polinema.sewakamera.R

class DetailTransaksiAdapter(private val ctx: Context, private val products: ArrayList<HashMap<String, Any>>) :
    RecyclerView.Adapter<DetailTransaksiAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_detail_transaksi_produk, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.namaDetailTransaksiProduk.text = product["nama_produk"].toString()
        holder.hargaDetailTransaksiProduk.text = product["harga"].toString()
        holder.quantityDetailTransaksiProduk.text = "x ${product["qty"].toString()}"

        Glide.with(ctx)
            .load(product["image"])
            .into(holder.imageDetailTransaksiProduk)

    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaDetailTransaksiProduk: TextView =
            itemView.findViewById(R.id.namaDetailTransaksiProduk)
        val hargaDetailTransaksiProduk: TextView =
            itemView.findViewById(R.id.hargaDetailTransaksiProduk)
        val quantityDetailTransaksiProduk: TextView =
            itemView.findViewById(R.id.quantityDetailTransaksiProduk)
        val imageDetailTransaksiProduk: ImageView =
            itemView.findViewById(R.id.imageDetailTransaksiProduk)
    }
}
