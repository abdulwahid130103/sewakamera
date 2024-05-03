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
import com.polinema.sewakamera.Model.Booking
import com.polinema.sewakamera.Model.Terverifikasi
import com.polinema.sewakamera.R

class TerverifikasiAdapter(var ctx: Context, private val verifList: ArrayList<Terverifikasi>) :
    RecyclerView.Adapter<TerverifikasiAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val categoryView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_booking, parent, false)
        return ViewHolder(categoryView)
    }

    interface OnItemPayClickListener {
        fun onPayClick(user_id: Int,id_transaksi: Int,total_all_produk: Int)
    }

    var payClickListener: OnItemPayClickListener? = null


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Terverifikasi = verifList[position]

        val hp = DataHelper(ctx)
        holder.idTransaksiBooking.text = item.id.toString()
        holder.namaMitraBooking.text = item.nama_mitra
        holder.statusTransaksiBooking.text = item.status

        Log.d("gambar", item.image)

        Glide.with(ctx)
            .load(item.image)
            .into(holder.imageProdukBooking)

        if(item.status == "terverifikasi"){
            holder.btnDetailBooking.visibility = View.GONE
            holder.btnBayarTerverifikasi.visibility = View.VISIBLE
        }
        holder.namaProdukBooking.text = item.nama_produk
        holder.hargaProdukBooking.text = hp.formatRupiah(item.harga.toString().toInt())
        holder.jumlahProdukBooking.text = "x ${item.jumlah.toString()}"
        holder.subHargaProdukBooking.text = hp.formatRupiah(item.sub_harga.toString().toInt())
        holder.jumlahSemuaProdukBooking.text = "x ${item.jumlah_all_produk.toString()}"
        holder.totalPesananHasilBooking.text =
            hp.formatRupiah(item.total_all_produk.toString().toInt())

        holder.btnBayarTerverifikasi.setOnClickListener {
//            Log.d("posisi",item.id.toString())
            payClickListener?.onPayClick(
                item.id_user.toString().toInt(),
                item.id.toString().toInt(),
                item.total_all_produk.toString().toInt()
            )
        }

    }

    override fun getItemCount(): Int {
        return verifList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idTransaksiBooking: TextView = itemView.findViewById(R.id.idTransaksiBooking)
        val namaMitraBooking: TextView = itemView.findViewById(R.id.namaMitraBooking)
        val statusTransaksiBooking: TextView = itemView.findViewById(R.id.statusTransaksiBooking)
        val imageProdukBooking: ImageView = itemView.findViewById(R.id.imageProdukBooking)
        val namaProdukBooking: TextView = itemView.findViewById(R.id.namaProdukBooking)
        val hargaProdukBooking: TextView = itemView.findViewById(R.id.hargaProdukBooking)
        val jumlahProdukBooking: TextView = itemView.findViewById(R.id.jumlahProdukBooking)
        val subHargaProdukBooking: TextView = itemView.findViewById(R.id.subHargaProdukBooking)
        val tampilkanLebihBanyakBooking: TextView =
            itemView.findViewById(R.id.tampilkanLebihBanyakBooking)
        val jumlahSemuaProdukBooking: TextView =
            itemView.findViewById(R.id.jumlahSemuaProdukBooking)
        val totalPesananHasilBooking: TextView =
            itemView.findViewById(R.id.totalPesananHasilBooking)
        val btnDetailBooking: Button = itemView.findViewById(R.id.btnDetailBooking)
        val btnBayarTerverifikasi: Button = itemView.findViewById(R.id.btnBayarTerverifikasi)

    }
}