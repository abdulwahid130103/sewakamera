package com.polinema.sewakamera.Model

data class Terverifikasi(
    val id:Int = 0,
    val id_user: Int,
    val nama_mitra: String,
    val status: String,
    val image: String,
    val nama_produk: String,
    val harga: Int,
    val jumlah: Int,
    val sub_harga: Int,
    val jumlah_all_produk: Int,
    val total_all_produk: Int,
)
