package com.polinema.sewakamera.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Model.DetailHistory
import com.polinema.sewakamera.Model.Dibayar
import com.polinema.sewakamera.R

class DetailHistoryAdapter (var ctx: Context, private val detailHistoryList: ArrayList<DetailHistory>) :
    RecyclerView.Adapter<DetailHistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val categoryView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_history_transaksi, parent, false)
        return ViewHolder(categoryView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: DetailHistory = detailHistoryList[position]

        val hp = DataHelper(ctx)
        holder.idDetailHistoryProduk.text = item.id.toString()
        holder.namaDetailHistoryProduk.text = item.nama_produk

        Glide.with(ctx)
            .load(item.image)
            .into(holder.imageDetailHistoryProduk)

        holder.hargaDetailHistoryProduk.text = hp.formatRupiah(item.harga.toString().toInt())
        holder.jumlahDetailHistoryProduk.text = "x ${item.jumlah.toString()}"
    }

    override fun getItemCount(): Int {
        return detailHistoryList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idDetailHistoryProduk: TextView = itemView.findViewById(R.id.idDetailHistoryProduk)
        val namaDetailHistoryProduk: TextView = itemView.findViewById(R.id.namaDetailHistoryProduk)
        val hargaDetailHistoryProduk: TextView = itemView.findViewById(R.id.hargaDetailHistoryProduk)
        val imageDetailHistoryProduk: ImageView = itemView.findViewById(R.id.imageDetailHistoryProduk)
        val jumlahDetailHistoryProduk: TextView = itemView.findViewById(R.id.jumlahDetailHistoryProduk)
    }
}