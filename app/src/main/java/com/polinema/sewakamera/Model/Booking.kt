package com.polinema.sewakamera.Model

data class Booking(
    val id:Int = 0,
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
